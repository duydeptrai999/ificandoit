package com.xiaomi.base.ui.screens.camera.camera2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Camera2 API manager for high-performance camera operations
 * Optimized for real-time filter processing with OpenGL
 */
class Camera2Manager(private val context: Context) {
    companion object {
        private const val TAG = "Camera2Manager"
        private const val CAMERA_THREAD_NAME = "CameraThread"
    }

    private var cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    
    private var previewSize: Size? = null
    private var cameraId: String? = null
    
    // Camera state callbacks
    private var onCameraOpened: ((CameraDevice) -> Unit)? = null
    private var onCameraError: ((Int, String) -> Unit)? = null

    /**
     * Initialize camera manager and start background thread
     */
    fun initialize() {
        startBackgroundThread()
        setupCamera()
    }

    /**
     * Setup camera with optimal configuration for filter processing
     */
    private fun setupCamera() {
        try {
            // Get back camera ID
            cameraId = getCameraId(CameraCharacteristics.LENS_FACING_BACK)
            if (cameraId == null) {
                Log.e(TAG, "No back camera found")
                return
            }

            // Get camera characteristics
            val characteristics = cameraManager.getCameraCharacteristics(cameraId!!)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            
            // Choose optimal preview size (1080p for balance of quality and performance)
            previewSize = chooseOptimalSize(
                map?.getOutputSizes(SurfaceTexture::class.java) ?: emptyArray(),
                1920, 1080
            )
            
            Log.d(TAG, "Camera setup completed. Preview size: ${previewSize?.width}x${previewSize?.height}")
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Camera access exception during setup", e)
        }
    }

    /**
     * Open camera asynchronously
     */
    suspend fun openCamera(): CameraDevice = suspendCancellableCoroutine { continuation ->
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            continuation.resumeWithException(SecurityException("Camera permission not granted"))
            return@suspendCancellableCoroutine
        }

        if (cameraId == null) {
            continuation.resumeWithException(IllegalStateException("Camera not initialized"))
            return@suspendCancellableCoroutine
        }

        val stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                Log.d(TAG, "Camera opened successfully")
                cameraDevice = camera
                onCameraOpened?.invoke(camera)
                continuation.resume(camera)
            }

            override fun onDisconnected(camera: CameraDevice) {
                Log.w(TAG, "Camera disconnected")
                camera.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                val errorMsg = "Camera error: $error"
                Log.e(TAG, errorMsg)
                camera.close()
                cameraDevice = null
                onCameraError?.invoke(error, errorMsg)
                continuation.resumeWithException(RuntimeException(errorMsg))
            }
        }

        try {
            cameraManager.openCamera(cameraId!!, stateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            continuation.resumeWithException(e)
        }
    }

    /**
     * Create capture session with surface for OpenGL rendering
     */
    suspend fun createCaptureSession(surface: Surface): CameraCaptureSession = suspendCancellableCoroutine { continuation ->
        val device = cameraDevice ?: run {
            continuation.resumeWithException(IllegalStateException("Camera not opened"))
            return@suspendCancellableCoroutine
        }

        val sessionCallback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                Log.d(TAG, "Capture session configured")
                captureSession = session
                continuation.resume(session)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e(TAG, "Capture session configuration failed")
                continuation.resumeWithException(RuntimeException("Failed to configure capture session"))
            }
        }

        try {
            device.createCaptureSession(
                listOf(surface),
                sessionCallback,
                backgroundHandler
            )
        } catch (e: CameraAccessException) {
            continuation.resumeWithException(e)
        }
    }

    /**
     * Start preview with continuous auto-focus and auto-exposure
     */
    fun startPreview(surface: Surface) {
        val session = captureSession ?: run {
            Log.e(TAG, "Capture session not created")
            return
        }

        val device = cameraDevice ?: run {
            Log.e(TAG, "Camera device not available")
            return
        }

        try {
            val requestBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            requestBuilder.addTarget(surface)
            
            // Set auto-focus and auto-exposure for optimal image quality
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)
            
            // Enable image stabilization if available
            requestBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON)
            requestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON)

            val request = requestBuilder.build()
            session.setRepeatingRequest(request, null, backgroundHandler)
            
            Log.d(TAG, "Preview started successfully")
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to start preview", e)
        }
    }

    /**
     * Stop preview
     */
    fun stopPreview() {
        try {
            captureSession?.stopRepeating()
            Log.d(TAG, "Preview stopped")
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to stop preview", e)
        }
    }

    /**
     * Close camera and cleanup resources
     */
    fun close() {
        captureSession?.close()
        captureSession = null
        
        cameraDevice?.close()
        cameraDevice = null
        
        stopBackgroundThread()
        
        Log.d(TAG, "Camera closed and resources cleaned up")
    }

    /**
     * Get camera ID for specified lens facing
     */
    private fun getCameraId(lensFacing: Int): String? {
        return try {
            cameraManager.cameraIdList.find { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                characteristics.get(CameraCharacteristics.LENS_FACING) == lensFacing
            }
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to get camera ID", e)
            null
        }
    }

    /**
     * Choose optimal size for preview based on target dimensions
     * Prioritizes common aspect ratios (16:9, 4:3) for better compatibility
     */
    private fun chooseOptimalSize(choices: Array<Size>, targetWidth: Int, targetHeight: Int): Size {
        // Common aspect ratios in order of preference
        val preferredRatios = listOf(
            16.0 / 9.0,  // 16:9 - most common for modern devices
            4.0 / 3.0,   // 4:3 - traditional camera ratio
            18.0 / 9.0,  // 18:9 - newer tall screens
            19.5 / 9.0   // 19.5:9 - iPhone X style
        )
        
        // Filter sizes that are reasonable for preview (not too large)
        val reasonableSizes = choices.filter { 
            it.width <= 1920 && it.height <= 1080 && it.width >= 640 && it.height >= 480
        }
        
        // Find best match for preferred aspect ratios
        for (preferredRatio in preferredRatios) {
            val bestForRatio = reasonableSizes.minByOrNull { size ->
                val ratio = size.width.toDouble() / size.height
                kotlin.math.abs(ratio - preferredRatio)
            }
            
            if (bestForRatio != null) {
                val ratio = bestForRatio.width.toDouble() / bestForRatio.height
                // Accept if ratio is close enough (within 5% tolerance)
                if (kotlin.math.abs(ratio - preferredRatio) < 0.05) {
                    Log.d(TAG, "Selected preview size: ${bestForRatio.width}x${bestForRatio.height} (ratio: ${String.format("%.2f", ratio)})")
                    return bestForRatio
                }
            }
        }
        
        // Fallback: choose largest reasonable size
        return reasonableSizes.maxByOrNull { it.width * it.height } 
            ?: choices.maxByOrNull { it.width * it.height } 
            ?: Size(1280, 720) // Safe fallback
    }

    /**
     * Start background thread for camera operations
     */
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread(CAMERA_THREAD_NAME).apply {
            start()
            backgroundHandler = Handler(looper)
        }
    }

    /**
     * Stop background thread
     */
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, "Failed to stop background thread", e)
        }
    }

    /**
     * Get current preview size
     */
    fun getPreviewSize(): Size? = previewSize

    /**
     * Set camera state callbacks
     */
    fun setCameraCallbacks(
        onOpened: ((CameraDevice) -> Unit)? = null,
        onError: ((Int, String) -> Unit)? = null
    ) {
        this.onCameraOpened = onOpened
        this.onCameraError = onError
    }
}