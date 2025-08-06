package com.xiaomi.base.ui.screens.camera.camera2

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.xiaomi.base.ui.screens.camera.filter.FilterConfig
import com.xiaomi.base.ui.screens.camera.filter.FilterManager
import com.xiaomi.base.ui.screens.camera.filter.FilterShader
import com.xiaomi.base.ui.screens.camera.filter.FilterType
import com.xiaomi.base.ui.screens.camera.opengl.GLRenderer
import com.xiaomi.base.ui.screens.camera.utils.PhotoUtils
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Custom GLSurfaceView that integrates Camera2 + SurfaceTexture + OpenGL
 * Optimized for Jetpack Compose integration with lifecycle awareness
 */
class CameraTextureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs), DefaultLifecycleObserver {
    
    companion object {
        private const val TAG = "CameraTextureView"
    }
    
    // Core components
    private val camera2Manager = Camera2Manager(context)
    private val glRenderer = GLRenderer(context)
    private val filterManager = FilterManager()
    
    // State management
    private var isInitialized = false
    private var isPaused = false
    private var currentFilter: FilterType = FilterType.ORIGINAL
    
    // Callbacks
    var onCameraReady: (() -> Unit)? = null
    var onCameraError: ((String) -> Unit)? = null
    var onFilterChanged: ((FilterType) -> Unit)? = null
    var onPhotoCaptured: ((Bitmap) -> Unit)? = null
    
    // Coroutine scope for async operations
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    
    // Photo capture state
    private var pendingCaptureCallback: ((Bitmap?) -> Unit)? = null
    
    init {
        setupGLSurfaceView()
        setupRenderer()
        setupCamera()
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "CameraTextureView attached to window")
        // Ensure GLSurfaceView starts rendering when view is attached
        if (!isPaused) {
            post {
                super<GLSurfaceView>.onResume()
            }
        }
    }
    
    /**
     * Configure GLSurfaceView for optimal performance
     */
    private fun setupGLSurfaceView() {
        // Use OpenGL ES 3.0
        setEGLContextClientVersion(3)
        
        // Enable depth buffer
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        
        // Preserve EGL context on pause
        preserveEGLContextOnPause = true
        
        Log.d(TAG, "GLSurfaceView configured")
    }
    
    /**
     * Setup OpenGL renderer with callbacks
     */
    private fun setupRenderer() {
        glRenderer.onSurfaceTextureReady = { surfaceTexture ->
            Log.d(TAG, "SurfaceTexture ready, starting camera")
            startCamera(surfaceTexture)
        }
        
        glRenderer.onFrameRendered = {
            // Frame rendered callback for performance monitoring
        }
        
        glRenderer.onFrameCaptured = { pixelData ->
            handleCapturedFrame(pixelData)
        }
        
        setRenderer(glRenderer)
        
        // Set render mode to continuous for smooth preview
        // This must be called after setRenderer to ensure GLThread is initialized
        renderMode = RENDERMODE_CONTINUOUSLY
        
        Log.d(TAG, "Renderer configured")
    }
    
    /**
     * Setup camera manager
     */
    private fun setupCamera() {
        camera2Manager.setCameraCallbacks(
            onOpened = { 
                Log.d(TAG, "Camera opened successfully")
                onCameraReady?.invoke()
            },
            onError = { errorCode, errorMsg ->
                Log.e(TAG, "Camera error: $errorCode - $errorMsg")
                onCameraError?.invoke(errorMsg)
            }
        )
        
        camera2Manager.initialize()
        Log.d(TAG, "Camera manager configured")
    }
    
    /**
     * Start camera with surface texture
     */
    private fun startCamera(surfaceTexture: SurfaceTexture) {
        coroutineScope.launch {
            try {
                // Set surface texture size based on camera preview size
                val previewSize = camera2Manager.getPreviewSize()
                if (previewSize != null) {
                    surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
                    Log.d(TAG, "Surface texture size set to: ${previewSize.width}x${previewSize.height}")
                    
                    // Update aspect ratio for proper display
                    updateAspectRatio(previewSize.width, previewSize.height)
                }
                
                // Open camera
                val cameraDevice = camera2Manager.openCamera()
                Log.d(TAG, "Camera device opened: ${cameraDevice.id}")
                
                // Create surface from texture
                val surface = Surface(surfaceTexture)
                
                // Create capture session
                val captureSession = camera2Manager.createCaptureSession(surface)
                Log.d(TAG, "Capture session created")
                
                // Start preview
                camera2Manager.startPreview(surface)
                Log.d(TAG, "Camera preview started")
                
                isInitialized = true
                
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start camera", e)
                withContext(Dispatchers.Main) {
                    onCameraError?.invoke("Failed to start camera: ${e.message}")
                }
            }
        }
    }
    
    /**
     * Update aspect ratio for natural camera preview like default camera app
     */
    private fun updateAspectRatio(previewWidth: Int, previewHeight: Int) {
        post {
            val viewWidth = width
            val viewHeight = height
            
            if (viewWidth > 0 && viewHeight > 0) {
                val previewRatio = previewWidth.toFloat() / previewHeight.toFloat()
                val viewRatio = viewWidth.toFloat() / viewHeight.toFloat()
                
                Log.d(TAG, "Preview ratio: $previewRatio, View ratio: $viewRatio")
                
                // Use center crop scaling like default camera apps
                // This fills the entire view while maintaining aspect ratio
                val layoutParams = this.layoutParams
                
                if (previewRatio > viewRatio) {
                    // Preview is wider, scale to fill height and crop sides
                    layoutParams.width = (viewHeight * previewRatio).toInt()
                    layoutParams.height = viewHeight
                } else {
                    // Preview is taller, scale to fill width and crop top/bottom
                    layoutParams.width = viewWidth
                    layoutParams.height = (viewWidth / previewRatio).toInt()
                }
                
                this.layoutParams = layoutParams
                Log.d(TAG, "Natural camera aspect ratio applied: ${layoutParams.width}x${layoutParams.height}")
            }
        }
    }
    
    /**
     * Apply filter to camera preview
     */
    fun applyFilter(filterType: FilterType) {
        if (currentFilter == filterType) return
        
        currentFilter = filterType
        
        val filterShader = if (filterType == FilterType.ORIGINAL) {
            null
        } else {
            filterManager.getFilter(filterType)?.shader
        }
        
        // Apply filter on GL thread
        queueEvent {
            glRenderer.setFilter(filterShader)
            Log.d(TAG, "Filter applied: ${filterType.name}")
        }
        
        onFilterChanged?.invoke(filterType)
    }
    
    /**
     * Get current filter
     */
    fun getCurrentFilter(): FilterType = currentFilter
    
    /**
     * Get available filters
     */
    fun getAvailableFilters(): List<FilterType> = filterManager.getAvailableFilters()
    
    /**
     * Add custom filter
     */
    fun addCustomFilter(filterType: FilterType, shader: FilterShader) {
        filterManager.addCustomFilter(
            FilterConfig(
                type = filterType,
                name = filterType.name,
                shader = shader
            )
        )
    }
    
    /**
     * Check if camera is ready
     */
    fun isCameraReady(): Boolean = isInitialized && !isPaused
    
    /**
     * Lifecycle: Resume
     */
    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        Log.d(TAG, "Resuming camera view")
        
        if (isPaused) {
            super<GLSurfaceView>.onResume()
            isPaused = false
        }
    }
    
    /**
     * Lifecycle: Pause
     */
    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "Pausing camera view")
        
        if (!isPaused) {
            super<GLSurfaceView>.onPause()
            isPaused = true
        }
        
        super<DefaultLifecycleObserver>.onPause(owner)
    }
    
    /**
     * Lifecycle: Destroy
     */
    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "Destroying camera view")
        
        // Stop camera
        camera2Manager.stopPreview()
        camera2Manager.close()
        
        // Release OpenGL resources
        queueEvent {
            glRenderer.release()
        }
        
        isInitialized = false
        
        super.onDestroy(owner)
    }
    
    /**
     * Handle surface destroyed
     */
    override fun surfaceDestroyed(holder: android.view.SurfaceHolder) {
        Log.d(TAG, "Surface destroyed")
        super.surfaceDestroyed(holder)
    }
    
    /**
     * Get camera preview size
     */
    fun getPreviewSize() = camera2Manager.getPreviewSize()
    
    /**
     * Capture photo (future implementation)
     */
    /**
     * Capture raw photo without any filter applied
     */
    fun captureRawPhoto(callback: (Bitmap?) -> Unit) {
        if (!isInitialized) {
            Log.w(TAG, "Camera not ready for photo capture")
            callback(null)
            return
        }
        
        Log.d(TAG, "Capturing raw photo (no filter)")
        
        // Get actual preview size from camera
        val previewSize = camera2Manager.getPreviewSize()
        if (previewSize == null) {
            Log.e(TAG, "Preview size not available for capture")
            callback(null)
            return
        }
        
        Log.d(TAG, "Using preview size for raw capture: ${previewSize.width}x${previewSize.height}")
        
        // Store callback for when capture completes
        val captureCallback: (Bitmap?) -> Unit = { bitmap ->
            callback(bitmap)
            bitmap?.let { onPhotoCaptured?.invoke(it) }
        }
        
        // Store callback temporarily
        pendingCaptureCallback = captureCallback
        
        // Request raw frame capture from GL renderer (without filter)
        glRenderer.captureRawFrame(previewSize.width, previewSize.height)
    }
    
    /**
     * Capture photo with current filter applied
     */
    fun capturePhoto(callback: (Bitmap?) -> Unit) {
        if (!isInitialized) {
            Log.w(TAG, "Camera not ready for photo capture")
            callback(null)
            return
        }
        
        Log.d(TAG, "Capturing photo with filter: $currentFilter")
        
        // Get actual preview size from camera
        val previewSize = camera2Manager.getPreviewSize()
        if (previewSize == null) {
            Log.e(TAG, "Preview size not available for capture")
            callback(null)
            return
        }
        
        Log.d(TAG, "Using preview size for capture: ${previewSize.width}x${previewSize.height}")
        
        // Store callback for when capture completes
        val captureCallback: (Bitmap?) -> Unit = { bitmap ->
            callback(bitmap)
            bitmap?.let { onPhotoCaptured?.invoke(it) }
        }
        
        // Store callback temporarily
        pendingCaptureCallback = captureCallback
        
        // Request frame capture from GL renderer with actual preview size
        glRenderer.captureFrame(previewSize.width, previewSize.height)
    }
    
    /**
     * Handle captured frame data from OpenGL
     */
    private fun handleCapturedFrame(pixelData: ByteArray) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                if (pixelData.isEmpty()) {
                    Log.e(TAG, "Received empty pixel data")
                    withContext(Dispatchers.Main) {
                        pendingCaptureCallback?.invoke(null)
                        pendingCaptureCallback = null
                    }
                    return@launch
                }
                
                // Get actual capture dimensions from GL renderer
                val (actualWidth, actualHeight) = glRenderer.getActualCaptureSize()
                if (actualWidth <= 0 || actualHeight <= 0) {
                    Log.e(TAG, "Invalid capture dimensions: ${actualWidth}x${actualHeight}")
                    withContext(Dispatchers.Main) {
                        pendingCaptureCallback?.invoke(null)
                        pendingCaptureCallback = null
                    }
                    return@launch
                }
                
                Log.d(TAG, "Converting pixel data to bitmap with actual capture size: ${actualWidth}x${actualHeight}")
                
                // Convert pixel data to bitmap using actual capture dimensions
                val bitmap = PhotoUtils.pixelDataToBitmap(pixelData, actualWidth, actualHeight)
                
                withContext(Dispatchers.Main) {
                    if (bitmap != null) {
                        Log.d(TAG, "Photo captured successfully: ${bitmap.width}x${bitmap.height}")
                        pendingCaptureCallback?.invoke(bitmap)
                    } else {
                        Log.e(TAG, "Failed to convert pixel data to bitmap")
                        pendingCaptureCallback?.invoke(null)
                    }
                    pendingCaptureCallback = null
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error processing captured frame", e)
                withContext(Dispatchers.Main) {
                    pendingCaptureCallback?.invoke(null)
                    pendingCaptureCallback = null
                }
            }
        }
    }
    
    /**
     * Switch camera (future implementation)
     */
    fun switchCamera() {
        // TODO: Implement camera switching
        Log.d(TAG, "Camera switching not implemented yet")
    }
    
    /**
     * Set zoom level (future implementation)
     */
    fun setZoom(zoomLevel: Float) {
        // TODO: Implement zoom control
        Log.d(TAG, "Zoom control not implemented yet")
    }
    
    /**
     * Enable/disable flash (future implementation)
     */
    fun setFlashEnabled(enabled: Boolean) {
        // TODO: Implement flash control
        Log.d(TAG, "Flash control not implemented yet")
    }
}