package com.xiaomi.base.ui.screens.camera.utils

import android.graphics.*
import androidx.core.graphics.ColorUtils
import com.xiaomi.base.ui.screens.camera.components.ColorAdjustmentValues
import com.xiaomi.base.ui.screens.camera.components.ColorChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.*

/**
 * Utility class để xử lý việc áp dụng color adjustments lên bitmap
 */
object ColorAdjustmentUtils {

    /**
     * Áp dụng color adjustments lên bitmap
     */
    suspend fun applyColorAdjustments(
        originalBitmap: Bitmap,
        adjustments: ColorAdjustmentValues
    ): Bitmap = withContext(Dispatchers.Default) {
        val width = originalBitmap.width
        val height = originalBitmap.height
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        val pixels = IntArray(width * height)
        originalBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        // Process pixels in chunks for better performance
        val chunkSize = 1000
        for (i in pixels.indices step chunkSize) {
            val endIndex = minOf(i + chunkSize, pixels.size)
            for (j in i until endIndex) {
                pixels[j] = adjustPixelColor(pixels[j], adjustments)
            }
        }
        
        resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        resultBitmap
    }

    /**
     * Điều chỉnh màu của một pixel
     */
    private fun adjustPixelColor(pixel: Int, adjustments: ColorAdjustmentValues): Int {
        val alpha = Color.alpha(pixel)
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)
        
        // Convert RGB to HSL
        val hsl = FloatArray(3)
        ColorUtils.RGBToHSL(red, green, blue, hsl)
        
        val originalHue = hsl[0]
        val originalSaturation = hsl[1]
        val originalLightness = hsl[2]
        
        // Determine which color channel this pixel belongs to based on hue
        val colorChannel = determineColorChannel(originalHue)
        
        // Get adjustments for this color channel
        val (hueAdjust, satAdjust, lumAdjust) = getAdjustmentsForChannel(colorChannel, adjustments)
        
        // Apply adjustments
        var newHue = originalHue + hueAdjust
        var newSaturation = (originalSaturation + satAdjust / 100f).coerceIn(0f, 1f)
        var newLightness = (originalLightness + lumAdjust / 100f).coerceIn(0f, 1f)
        
        // Normalize hue to 0-360 range
        newHue = ((newHue % 360f) + 360f) % 360f
        
        // Convert back to RGB
        val newColor = ColorUtils.HSLToColor(floatArrayOf(newHue, newSaturation, newLightness))
        
        return Color.argb(
            alpha,
            Color.red(newColor),
            Color.green(newColor),
            Color.blue(newColor)
        )
    }

    /**
     * Xác định color channel dựa trên hue value
     */
    private fun determineColorChannel(hue: Float): ColorChannel {
        return when {
            hue >= 345f || hue < 15f -> ColorChannel.RED        // 345-15°
            hue >= 15f && hue < 45f -> ColorChannel.ORANGE      // 15-45°
            hue >= 45f && hue < 75f -> ColorChannel.YELLOW      // 45-75°
            hue >= 75f && hue < 165f -> ColorChannel.GREEN      // 75-165°
            hue >= 165f && hue < 195f -> ColorChannel.CYAN      // 165-195°
            hue >= 195f && hue < 255f -> ColorChannel.BLUE      // 195-255°
            hue >= 255f && hue < 285f -> ColorChannel.PURPLE    // 255-285°
            hue >= 285f && hue < 345f -> ColorChannel.MAGENTA   // 285-345°
            else -> ColorChannel.RED
        }
    }

    /**
     * Lấy adjustment values cho color channel cụ thể
     */
    private fun getAdjustmentsForChannel(
        channel: ColorChannel,
        adjustments: ColorAdjustmentValues
    ): Triple<Float, Float, Float> {
        return when (channel) {
            ColorChannel.RED -> Triple(
                adjustments.redHue,
                adjustments.redSaturation,
                adjustments.redLuminance
            )
            ColorChannel.ORANGE -> Triple(
                adjustments.orangeHue,
                adjustments.orangeSaturation,
                adjustments.orangeLuminance
            )
            ColorChannel.YELLOW -> Triple(
                adjustments.yellowHue,
                adjustments.yellowSaturation,
                adjustments.yellowLuminance
            )
            ColorChannel.GREEN -> Triple(
                adjustments.greenHue,
                adjustments.greenSaturation,
                adjustments.greenLuminance
            )
            ColorChannel.CYAN -> Triple(
                adjustments.cyanHue,
                adjustments.cyanSaturation,
                adjustments.cyanLuminance
            )
            ColorChannel.BLUE -> Triple(
                adjustments.blueHue,
                adjustments.blueSaturation,
                adjustments.blueLuminance
            )
            ColorChannel.PURPLE -> Triple(
                adjustments.purpleHue,
                adjustments.purpleSaturation,
                adjustments.purpleLuminance
            )
            ColorChannel.MAGENTA -> Triple(
                adjustments.magentaHue,
                adjustments.magentaSaturation,
                adjustments.magentaLuminance
            )
        }
    }

    /**
     * Tạo preview bitmap với kích thước nhỏ hơn để tăng hiệu năng
     */
    suspend fun createPreviewBitmap(
        originalBitmap: Bitmap,
        adjustments: ColorAdjustmentValues,
        maxSize: Int = 512
    ): Bitmap = withContext(Dispatchers.Default) {
        // Scale down bitmap for preview
        val scaledBitmap = scaleDownBitmap(originalBitmap, maxSize)
        
        // Apply adjustments to scaled bitmap
        applyColorAdjustments(scaledBitmap, adjustments)
    }

    /**
     * Scale down bitmap để tăng hiệu năng preview
     */
    private fun scaleDownBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }
        
        val scale = minOf(
            maxSize.toFloat() / width,
            maxSize.toFloat() / height
        )
        
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * Kiểm tra xem có adjustment nào được áp dụng không
     */
    fun hasAnyAdjustments(adjustments: ColorAdjustmentValues): Boolean {
        return adjustments.redHue != 0f || adjustments.redSaturation != 0f || adjustments.redLuminance != 0f ||
                adjustments.orangeHue != 0f || adjustments.orangeSaturation != 0f || adjustments.orangeLuminance != 0f ||
                adjustments.yellowHue != 0f || adjustments.yellowSaturation != 0f || adjustments.yellowLuminance != 0f ||
                adjustments.greenHue != 0f || adjustments.greenSaturation != 0f || adjustments.greenLuminance != 0f ||
                adjustments.cyanHue != 0f || adjustments.cyanSaturation != 0f || adjustments.cyanLuminance != 0f ||
                adjustments.blueHue != 0f || adjustments.blueSaturation != 0f || adjustments.blueLuminance != 0f ||
                adjustments.purpleHue != 0f || adjustments.purpleSaturation != 0f || adjustments.purpleLuminance != 0f ||
                adjustments.magentaHue != 0f || adjustments.magentaSaturation != 0f || adjustments.magentaLuminance != 0f
    }

    /**
     * Tạo color matrix để áp dụng adjustments (alternative approach)
     */
    fun createColorMatrix(adjustments: ColorAdjustmentValues): ColorMatrix {
        val colorMatrix = ColorMatrix()
        
        // This is a simplified approach - for more complex per-color adjustments,
        // pixel-level processing (as implemented above) is more accurate
        
        return colorMatrix
    }

    /**
     * Smooth transition giữa các adjustment values
     */
    fun interpolateAdjustments(
        from: ColorAdjustmentValues,
        to: ColorAdjustmentValues,
        progress: Float
    ): ColorAdjustmentValues {
        val t = progress.coerceIn(0f, 1f)
        
        return ColorAdjustmentValues(
            redHue = lerp(from.redHue, to.redHue, t),
            redSaturation = lerp(from.redSaturation, to.redSaturation, t),
            redLuminance = lerp(from.redLuminance, to.redLuminance, t),
            
            orangeHue = lerp(from.orangeHue, to.orangeHue, t),
            orangeSaturation = lerp(from.orangeSaturation, to.orangeSaturation, t),
            orangeLuminance = lerp(from.orangeLuminance, to.orangeLuminance, t),
            
            yellowHue = lerp(from.yellowHue, to.yellowHue, t),
            yellowSaturation = lerp(from.yellowSaturation, to.yellowSaturation, t),
            yellowLuminance = lerp(from.yellowLuminance, to.yellowLuminance, t),
            
            greenHue = lerp(from.greenHue, to.greenHue, t),
            greenSaturation = lerp(from.greenSaturation, to.greenSaturation, t),
            greenLuminance = lerp(from.greenLuminance, to.greenLuminance, t),
            
            cyanHue = lerp(from.cyanHue, to.cyanHue, t),
            cyanSaturation = lerp(from.cyanSaturation, to.cyanSaturation, t),
            cyanLuminance = lerp(from.cyanLuminance, to.cyanLuminance, t),
            
            blueHue = lerp(from.blueHue, to.blueHue, t),
            blueSaturation = lerp(from.blueSaturation, to.blueSaturation, t),
            blueLuminance = lerp(from.blueLuminance, to.blueLuminance, t),
            
            purpleHue = lerp(from.purpleHue, to.purpleHue, t),
            purpleSaturation = lerp(from.purpleSaturation, to.purpleSaturation, t),
            purpleLuminance = lerp(from.purpleLuminance, to.purpleLuminance, t),
            
            magentaHue = lerp(from.magentaHue, to.magentaHue, t),
            magentaSaturation = lerp(from.magentaSaturation, to.magentaSaturation, t),
            magentaLuminance = lerp(from.magentaLuminance, to.magentaLuminance, t)
        )
    }

    /**
     * Linear interpolation helper
     */
    private fun lerp(start: Float, end: Float, t: Float): Float {
        return start + (end - start) * t
    }
}