package com.xiaomi.base.ui.screens.camera.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for photo operations
 * Handles bitmap conversion, saving, and file management
 */
object PhotoUtils {
    private const val TAG = "PhotoUtils"
    private const val PHOTO_QUALITY = 95
    
    /**
     * Convert OpenGL pixel data to Bitmap
     * Handles proper orientation and color format conversion
     */
    fun pixelDataToBitmap(
        pixelData: ByteArray,
        width: Int,
        height: Int
    ): Bitmap? {
        return try {
            // Create bitmap from pixel data
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            
            // Convert RGBA to ARGB and flip vertically (OpenGL coordinates)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    val pixelIndex = (i * width + j) * 4
                    val bitmapIndex = ((height - 1 - i) * width + j) // Flip vertically
                    
                    if (pixelIndex + 3 < pixelData.size) {
                        val r = pixelData[pixelIndex].toInt() and 0xFF
                        val g = pixelData[pixelIndex + 1].toInt() and 0xFF
                        val b = pixelData[pixelIndex + 2].toInt() and 0xFF
                        val a = pixelData[pixelIndex + 3].toInt() and 0xFF
                        
                        pixels[bitmapIndex] = (a shl 24) or (r shl 16) or (g shl 8) or b
                    }
                }
            }
            
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            
            // Rotate bitmap to correct orientation (portrait)
            rotateBitmap(bitmap, 90f)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error converting pixel data to bitmap", e)
            null
        }
    }
    
    /**
     * Rotate bitmap by specified degrees
     */
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        return try {
            val matrix = Matrix().apply {
                postRotate(degrees)
            }
            
            Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width, bitmap.height,
                matrix, true
            ).also {
                if (it != bitmap) {
                    bitmap.recycle()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error rotating bitmap", e)
            bitmap
        }
    }
    
    /**
     * Save bitmap to gallery
     * Uses MediaStore API for Android 10+ and legacy method for older versions
     */
    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        filename: String = generatePhotoFilename()
    ): Uri? = withContext(Dispatchers.IO) {
        return@withContext try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveBitmapToGalleryQ(context, bitmap, filename)
            } else {
                saveBitmapToGalleryLegacy(context, bitmap, filename)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving bitmap to gallery", e)
            null
        }
    }
    
    /**
     * Save bitmap using MediaStore API (Android 10+)
     */
    private fun saveBitmapToGalleryQ(
        context: Context,
        bitmap: Bitmap,
        filename: String
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/CameraApp")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        
        return uri?.let { imageUri ->
            try {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, outputStream)
                }
                
                // Mark as not pending
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
                
                Log.d(TAG, "Photo saved to gallery: $imageUri")
                imageUri
            } catch (e: Exception) {
                Log.e(TAG, "Error writing to MediaStore", e)
                resolver.delete(imageUri, null, null)
                null
            }
        }
    }
    
    /**
     * Save bitmap using legacy file system method (Android 9 and below)
     */
    private fun saveBitmapToGalleryLegacy(
        context: Context,
        bitmap: Bitmap,
        filename: String
    ): Uri? {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appDir = File(picturesDir, "CameraApp")
        
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        
        val file = File(appDir, filename)
        
        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, outputStream)
            }
            
            // Add to MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DATA, file.absolutePath)
                put(MediaStore.Images.Media.TITLE, filename)
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            }
            
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            
            Log.d(TAG, "Photo saved to gallery (legacy): $uri")
            uri
        } catch (e: Exception) {
            Log.e(TAG, "Error saving photo (legacy)", e)
            null
        }
    }
    
    /**
     * Generate unique filename for photo
     */
    private fun generatePhotoFilename(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        return "IMG_${timestamp}.jpg"
    }
    
    /**
     * Get file size in human readable format
     */
    fun getFileSizeString(sizeInBytes: Long): String {
        return when {
            sizeInBytes < 1024 -> "${sizeInBytes} B"
            sizeInBytes < 1024 * 1024 -> "${sizeInBytes / 1024} KB"
            else -> "${sizeInBytes / (1024 * 1024)} MB"
        }
    }
    
    /**
     * Clean up temporary files
     */
    fun cleanupTempFiles(context: Context) {
        try {
            val tempDir = File(context.cacheDir, "temp_photos")
            if (tempDir.exists()) {
                tempDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.lastModified() < System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                        file.delete()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up temp files", e)
        }
    }
}