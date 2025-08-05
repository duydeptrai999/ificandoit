package com.xiaomi.base.ui.screens.camera.opengl

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.xiaomi.base.ui.screens.camera.filter.FilterShader
import com.xiaomi.base.ui.screens.camera.filter.FilterType
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Data class for returning 6 values from destructuring assignment
 */
data class Tuple6<T1, T2, T3, T4, T5, T6>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4,
    val fifth: T5,
    val sixth: T6
)

/**
 * High-performance OpenGL ES 3.0 renderer for camera preview with real-time filters
 * Optimized for smooth 60fps rendering with minimal latency
 */
class GLRenderer(private val context: Context) : GLSurfaceView.Renderer {
    companion object {
        private const val TAG = "GLRenderer"
        
        // Vertex coordinates for full-screen quad
        private val VERTEX_COORDS = floatArrayOf(
            -1.0f, -1.0f, 0.0f,  // Bottom left
             1.0f, -1.0f, 0.0f,  // Bottom right
            -1.0f,  1.0f, 0.0f,  // Top left
             1.0f,  1.0f, 0.0f   // Top right
        )
        
        // Texture coordinates (corrected for proper camera orientation)
        private val TEXTURE_COORDS = floatArrayOf(
            0.0f, 0.0f,  // Bottom left
            1.0f, 0.0f,  // Bottom right
            0.0f, 1.0f,  // Top left
            1.0f, 1.0f   // Top right
        )
        
        // Default vertex shader for camera texture
        private const val DEFAULT_VERTEX_SHADER = """
            #version 300 es
            layout (location = 0) in vec4 aPosition;
            layout (location = 1) in vec2 aTextureCoord;
            
            uniform mat4 uMVPMatrix;
            uniform mat4 uTexMatrix;
            
            out vec2 vTextureCoord;
            
            void main() {
                gl_Position = uMVPMatrix * aPosition;
                vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
            }
        """
        
        // Default fragment shader for camera texture
        private const val DEFAULT_FRAGMENT_SHADER = """
            #version 300 es
            #extension GL_OES_EGL_image_external_essl3 : require
            precision mediump float;
            
            in vec2 vTextureCoord;
            uniform samplerExternalOES uTexture;
            
            out vec4 fragColor;
            
            void main() {
                fragColor = texture(uTexture, vTextureCoord);
            }
        """
    }

    // OpenGL objects
    private var program = 0
    private var vertexBuffer: FloatBuffer
    private var textureBuffer: FloatBuffer
    private var cameraTexture = 0
    
    // Matrix transformations
    private val mvpMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val textureMatrix = FloatArray(16)
    
    // Shader handles
    private var positionHandle = 0
    private var textureCoordHandle = 0
    private var mvpMatrixHandle = 0
    private var textureMatrixHandle = 0
    private var textureHandle = 0
    
    // Filter system
    private var currentFilter: FilterShader? = null
    private var filterProgram = 0
    private val filterUniforms = mutableMapOf<String, Int>()
    
    // Surface texture for camera
    var surfaceTexture: SurfaceTexture? = null
        private set
    
    // Callbacks
    var onSurfaceTextureReady: ((SurfaceTexture) -> Unit)? = null
    var onFrameRendered: (() -> Unit)? = null
    var onFrameCaptured: ((ByteArray) -> Unit)? = null
    
    // Performance monitoring
    private var frameCount = 0
    private var lastFpsTime = System.currentTimeMillis()
    
    // Capture state
    private var shouldCaptureFrame = false
    private var captureWidth = 0
    private var captureHeight = 0
    
    init {
        // Initialize vertex buffer
        vertexBuffer = ByteBuffer.allocateDirect(VERTEX_COORDS.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(VERTEX_COORDS)
                position(0)
            }
        
        // Initialize texture coordinate buffer
        textureBuffer = ByteBuffer.allocateDirect(TEXTURE_COORDS.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(TEXTURE_COORDS)
                position(0)
            }
        
        // Initialize matrices
        Matrix.setIdentityM(textureMatrix, 0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.d(TAG, "Surface created")
        
        // Set clear color to black
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        
        // Enable depth testing
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        
        // Load and compile shader program from assets
        val vertexShader = ShaderLoader.loadVertexShader(context) ?: DEFAULT_VERTEX_SHADER
        val fragmentShader = ShaderLoader.loadFragmentShader(context) ?: DEFAULT_FRAGMENT_SHADER
        
        program = createShaderProgram(vertexShader, fragmentShader)
        if (program == 0) {
            Log.e(TAG, "Failed to create shader program")
            return
        }
        
        // Get shader handles (using camelCase names)
        positionHandle = GLES30.glGetAttribLocation(program, "aPosition")
        textureCoordHandle = GLES30.glGetAttribLocation(program, "aTextureCoord")
        mvpMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        textureMatrixHandle = GLES30.glGetUniformLocation(program, "uTexMatrix")
        textureHandle = GLES30.glGetUniformLocation(program, "uTexture")
        
        // Create camera texture
        createCameraTexture()
        
        Log.d(TAG, "OpenGL initialization completed")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.d(TAG, "Surface changed: ${width}x${height}")
        
        // Set viewport
        GLES30.glViewport(0, 0, width, height)
        
        // Use orthographic projection to avoid distortion
        // This ensures the camera preview maintains its aspect ratio
        Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -1f, 1f, -1f, 1f)
        
        // Set identity view matrix (no camera transformation needed)
        Matrix.setIdentityM(viewMatrix, 0)
        
        // Calculate MVP matrix (just use projection since view is identity)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear buffers
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        
        // Update surface texture
        surfaceTexture?.updateTexImage()
        surfaceTexture?.getTransformMatrix(textureMatrix)
        
        // Use appropriate shader program and get handles
        val (currentProgram, currentPositionHandle, currentTextureCoordHandle, 
             currentMvpMatrixHandle, currentTextureMatrixHandle, currentTextureHandle) = 
            if (filterProgram != 0 && currentFilter != null) {
                // Use filter program with its handles
                val filterPositionHandle = GLES30.glGetAttribLocation(filterProgram, "aPosition")
                val filterTextureCoordHandle = GLES30.glGetAttribLocation(filterProgram, "aTextureCoord")
                val filterMvpMatrixHandle = filterUniforms["uMVPMatrix"] ?: -1
                val filterTextureMatrixHandle = filterUniforms["uTexMatrix"] ?: -1
                val filterTextureHandle = filterUniforms["uTexture"] ?: -1
                
                Tuple6(filterProgram, filterPositionHandle, filterTextureCoordHandle,
                      filterMvpMatrixHandle, filterTextureMatrixHandle, filterTextureHandle)
            } else {
                // Use default program with its handles
                Tuple6(program, positionHandle, textureCoordHandle,
                      mvpMatrixHandle, textureMatrixHandle, textureHandle)
            }
        
        GLES30.glUseProgram(currentProgram)
        
        // Set vertex attributes
        if (currentPositionHandle >= 0) {
            GLES30.glEnableVertexAttribArray(currentPositionHandle)
            GLES30.glVertexAttribPointer(currentPositionHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        }
        
        if (currentTextureCoordHandle >= 0) {
            GLES30.glEnableVertexAttribArray(currentTextureCoordHandle)
            GLES30.glVertexAttribPointer(currentTextureCoordHandle, 2, GLES30.GL_FLOAT, false, 0, textureBuffer)
        }
        
        // Set matrices
        if (currentMvpMatrixHandle >= 0) {
            GLES30.glUniformMatrix4fv(currentMvpMatrixHandle, 1, false, mvpMatrix, 0)
        }
        if (currentTextureMatrixHandle >= 0) {
            GLES30.glUniformMatrix4fv(currentTextureMatrixHandle, 1, false, textureMatrix, 0)
        }
        
        // Bind camera texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexture)
        if (currentTextureHandle >= 0) {
            GLES30.glUniform1i(currentTextureHandle, 0)
        }
        
        // Apply filter uniforms if available
        currentFilter?.let { filter ->
            applyFilterUniforms(filter)
        }
        
        // Draw quad
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        
        // Disable vertex arrays
        if (currentPositionHandle >= 0) {
            GLES30.glDisableVertexAttribArray(currentPositionHandle)
        }
        if (currentTextureCoordHandle >= 0) {
            GLES30.glDisableVertexAttribArray(currentTextureCoordHandle)
        }
        
        // Capture frame if requested
        if (shouldCaptureFrame) {
            captureCurrentFrame()
            shouldCaptureFrame = false
        }
        
        // Update FPS counter
        updateFpsCounter()
        
        // Notify frame rendered
        onFrameRendered?.invoke()
        
        // Check for OpenGL errors
        checkGLError("onDrawFrame")
    }

    /**
     * Set filter for real-time processing
     */
    fun setFilter(filter: FilterShader?) {
        currentFilter = filter
        
        if (filter != null) {
            // Create filter shader program
            filterProgram = createShaderProgram(filter.vertexShader, filter.fragmentShader)
            if (filterProgram != 0) {
                // Cache uniform locations
                cacheFilterUniforms(filter)
                Log.d(TAG, "Filter applied: ${filter.javaClass.simpleName}")
            } else {
                Log.e(TAG, "Failed to create filter shader program")
            }
        } else {
            // Clear filter
            if (filterProgram != 0) {
                GLES30.glDeleteProgram(filterProgram)
                filterProgram = 0
            }
            filterUniforms.clear()
            Log.d(TAG, "Filter cleared")
        }
    }

    /**
     * Create camera texture for SurfaceTexture
     */
    private fun createCameraTexture() {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        cameraTexture = textures[0]
        
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexture)
        
        // Set texture parameters for optimal performance
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
        
        // Create SurfaceTexture
        surfaceTexture = SurfaceTexture(cameraTexture).apply {
            setOnFrameAvailableListener { 
                // Request render on new frame
            }
        }
        
        // Notify that surface texture is ready
        onSurfaceTextureReady?.invoke(surfaceTexture!!)
        
        Log.d(TAG, "Camera texture created: $cameraTexture")
    }

    /**
     * Create and compile shader program
     */
    private fun createShaderProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
        val vertexShader = compileShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        if (vertexShader == 0) return 0
        
        val fragmentShader = compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)
        if (fragmentShader == 0) {
            GLES30.glDeleteShader(vertexShader)
            return 0
        }
        
        val program = GLES30.glCreateProgram()
        if (program == 0) {
            Log.e(TAG, "Failed to create program")
            GLES30.glDeleteShader(vertexShader)
            GLES30.glDeleteShader(fragmentShader)
            return 0
        }
        
        GLES30.glAttachShader(program, vertexShader)
        GLES30.glAttachShader(program, fragmentShader)
        GLES30.glLinkProgram(program)
        
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
        
        if (linkStatus[0] != GLES30.GL_TRUE) {
            val error = GLES30.glGetProgramInfoLog(program)
            Log.e(TAG, "Failed to link program: $error")
            GLES30.glDeleteProgram(program)
            GLES30.glDeleteShader(vertexShader)
            GLES30.glDeleteShader(fragmentShader)
            return 0
        }
        
        // Clean up shaders
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)
        
        return program
    }

    /**
     * Compile individual shader
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        val shader = GLES30.glCreateShader(type)
        if (shader == 0) {
            Log.e(TAG, "Failed to create shader")
            return 0
        }
        
        GLES30.glShaderSource(shader, shaderCode)
        GLES30.glCompileShader(shader)
        
        val compileStatus = IntArray(1)
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
        
        if (compileStatus[0] != GLES30.GL_TRUE) {
            val error = GLES30.glGetShaderInfoLog(shader)
            Log.e(TAG, "Failed to compile shader: $error")
            GLES30.glDeleteShader(shader)
            return 0
        }
        
        return shader
    }

    /**
     * Cache filter uniform locations for performance
     */
    private fun cacheFilterUniforms(filter: FilterShader) {
        filterUniforms.clear()
        
        // Cache common uniforms
        filterUniforms["uMVPMatrix"] = GLES30.glGetUniformLocation(filterProgram, "uMVPMatrix")
        filterUniforms["uTexMatrix"] = GLES30.glGetUniformLocation(filterProgram, "uTexMatrix")
        filterUniforms["uTexture"] = GLES30.glGetUniformLocation(filterProgram, "uTexture")
        
        // Cache filter-specific uniforms
        filter.getUniformValues().keys.forEach { uniformName ->
            val location = GLES30.glGetUniformLocation(filterProgram, uniformName)
            if (location >= 0) {
                filterUniforms[uniformName] = location
            }
        }
    }

    /**
     * Apply filter uniform values
     */
    private fun applyFilterUniforms(filter: FilterShader) {
        filter.getUniformValues().forEach { (name, value) ->
            val location = filterUniforms[name] ?: return@forEach
            
            when (value) {
                is Float -> GLES30.glUniform1f(location, value)
                is Int -> GLES30.glUniform1i(location, value)
                is FloatArray -> {
                    when (value.size) {
                        2 -> GLES30.glUniform2fv(location, 1, value, 0)
                        3 -> GLES30.glUniform3fv(location, 1, value, 0)
                        4 -> GLES30.glUniform4fv(location, 1, value, 0)
                    }
                }
            }
        }
    }

    /**
     * Update FPS counter for performance monitoring
     */
    private fun updateFpsCounter() {
        frameCount++
        val currentTime = System.currentTimeMillis()
        
        if (currentTime - lastFpsTime >= 1000) {
            val fps = frameCount * 1000.0 / (currentTime - lastFpsTime)
            Log.d(TAG, "FPS: ${String.format("%.1f", fps)}")
            
            frameCount = 0
            lastFpsTime = currentTime
        }
    }

    /**
     * Check for OpenGL errors
     */
    private fun checkGLError(operation: String) {
        val error = GLES30.glGetError()
        if (error != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "OpenGL error in $operation: $error")
        }
    }

    /**
     * Request frame capture on next render
     */
    fun captureFrame(width: Int = 1080, height: Int = 1920) {
        captureWidth = width
        captureHeight = height
        shouldCaptureFrame = true
        Log.d(TAG, "Frame capture requested: ${width}x${height}")
    }
    
    /**
     * Capture current frame as bitmap data
     */
    private fun captureCurrentFrame() {
        try {
            val width = if (captureWidth > 0) captureWidth else 1080
            val height = if (captureHeight > 0) captureHeight else 1920
            
            // Create buffer for pixel data
            val pixelBuffer = ByteBuffer.allocateDirect(width * height * 4)
            pixelBuffer.order(ByteOrder.nativeOrder())
            
            // Read pixels from current framebuffer
            GLES30.glReadPixels(
                0, 0, width, height,
                GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE,
                pixelBuffer
            )
            
            checkGLError("glReadPixels")
            
            // Convert to byte array
            val pixelData = ByteArray(pixelBuffer.remaining())
            pixelBuffer.rewind()
            pixelBuffer.get(pixelData)
            
            // Notify capture complete
            onFrameCaptured?.invoke(pixelData)
            
            Log.d(TAG, "Frame captured successfully: ${width}x${height}, ${pixelData.size} bytes")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error capturing frame", e)
            onFrameCaptured?.invoke(ByteArray(0))
        }
    }

    /**
     * Release OpenGL resources
     */
    fun release() {
        surfaceTexture?.release()
        surfaceTexture = null
        
        if (program != 0) {
            GLES30.glDeleteProgram(program)
            program = 0
        }
        
        if (filterProgram != 0) {
            GLES30.glDeleteProgram(filterProgram)
            filterProgram = 0
        }
        
        if (cameraTexture != 0) {
            GLES30.glDeleteTextures(1, intArrayOf(cameraTexture), 0)
            cameraTexture = 0
        }
        
        Log.d(TAG, "OpenGL resources released")
    }
}