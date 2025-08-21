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
     * Áp dụng color adjustments lên bitmap với thuật toán tối ưu
     */
    suspend fun applyColorAdjustments(
        originalBitmap: Bitmap,
        adjustments: ColorAdjustmentValues
    ): Bitmap = withContext(Dispatchers.Default) {
        // Kiểm tra nếu không có điều chỉnh nào
        if (!hasAnyAdjustments(adjustments)) {
            return@withContext originalBitmap.copy(originalBitmap.config ?: Bitmap.Config.ARGB_8888, false)
        }
        
        val width = originalBitmap.width
        val height = originalBitmap.height
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        val pixels = IntArray(width * height)
        originalBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        // Tối ưu hiệu suất với parallel processing cho ảnh lớn
        if (pixels.size > 100000) { // Ảnh lớn hơn 100k pixels
            processPixelsParallel(pixels, adjustments)
        } else {
            // Process pixels in optimized chunks
            val chunkSize = 2000
            for (i in pixels.indices step chunkSize) {
                val endIndex = minOf(i + chunkSize, pixels.size)
                for (j in i until endIndex) {
                    pixels[j] = adjustPixelColor(pixels[j], adjustments)
                }
            }
        }
        
        resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        resultBitmap
    }
    
    /**
     * Xử lý pixels song song cho ảnh lớn
     */
    private fun processPixelsParallel(pixels: IntArray, adjustments: ColorAdjustmentValues) {
        val numThreads = minOf(4, Runtime.getRuntime().availableProcessors())
        val chunkSize = pixels.size / numThreads
        val threads = mutableListOf<Thread>()
        
        for (i in 0 until numThreads) {
            val startIndex = i * chunkSize
            val endIndex = if (i == numThreads - 1) pixels.size else (i + 1) * chunkSize
            
            val thread = Thread {
                for (j in startIndex until endIndex) {
                    pixels[j] = adjustPixelColor(pixels[j], adjustments)
                }
            }
            threads.add(thread)
            thread.start()
        }
        
        // Đợi tất cả threads hoàn thành
        threads.forEach { it.join() }
    }

    /**
     * Điều chỉnh màu của một pixel với thuật toán tối ưu
     */
    private fun adjustPixelColor(pixel: Int, adjustments: ColorAdjustmentValues): Int {
        val alpha = Color.alpha(pixel)
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)
        
        // Skip transparent pixels
        if (alpha == 0) return pixel
        
        // Skip grayscale pixels (no color information to adjust)
        if (red == green && green == blue) return pixel
        
        // Convert RGB to HSL
        val hsl = FloatArray(3)
        ColorUtils.RGBToHSL(red, green, blue, hsl)
        
        val originalHue = hsl[0]
        val originalSaturation = hsl[1]
        val originalLightness = hsl[2]
        
        // Skip pixels with very low saturation (nearly grayscale)
        if (originalSaturation < 0.05f) return pixel
        
        // Determine which color channel this pixel belongs to based on hue
        val colorChannel = determineColorChannel(originalHue)
        
        // Get adjustments for this color channel
        val (hueAdjust, satAdjust, lumAdjust) = getAdjustmentsForChannel(colorChannel, adjustments)
        
        // Skip if no adjustments needed for this channel
        if (hueAdjust == 0f && satAdjust == 0f && lumAdjust == 0f) return pixel
        
        // Apply adjustments with improved algorithm
        var newHue = originalHue + hueAdjust
        
        // Improved saturation adjustment with natural curve
        var newSaturation = if (satAdjust >= 0) {
            // Positive adjustment: use exponential curve for natural enhancement
            originalSaturation + (1f - originalSaturation) * (satAdjust / 100f)
        } else {
            // Negative adjustment: linear reduction
            originalSaturation * (1f + satAdjust / 100f)
        }.coerceIn(0f, 1f)
        
        // Improved luminance adjustment with gamma correction
        var newLightness = if (lumAdjust >= 0) {
            // Brighten: preserve highlights
            originalLightness + (1f - originalLightness) * (lumAdjust / 100f) * 0.8f
        } else {
            // Darken: preserve shadows
            originalLightness * (1f + lumAdjust / 100f)
        }.coerceIn(0f, 1f)
        
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
     * Xác định color channel dựa trên hue value với thuật toán tối ưu
     * Sử dụng phân vùng màu chính xác hơn dựa trên cảm nhận màu sắc tự nhiên
     */
    private fun determineColorChannel(hue: Float): ColorChannel {
        return when {
            // Red: 345-15° (bao gồm cả đỏ thuần và đỏ hồng)
            hue >= 345f || hue < 15f -> ColorChannel.RED
            
            // Orange: 15-35° (cam thuần, cam đỏ)
            hue >= 15f && hue < 35f -> ColorChannel.ORANGE
            
            // Yellow: 35-75° (vàng cam, vàng thuần, vàng lục)
            hue >= 35f && hue < 75f -> ColorChannel.YELLOW
            
            // Green: 75-150° (lục vàng, lục thuần, lục lam)
            hue >= 75f && hue < 150f -> ColorChannel.GREEN
            
            // Cyan: 150-210° (lục lam, lam lục, xanh da trời nhạt)
            hue >= 150f && hue < 210f -> ColorChannel.CYAN
            
            // Blue: 210-270° (xanh da trời, xanh dương thuần)
            hue >= 210f && hue < 270f -> ColorChannel.BLUE
            
            // Purple: 270-315° (tím xanh, tím thuần)
            hue >= 270f && hue < 315f -> ColorChannel.PURPLE
            
            // Magenta: 315-345° (tím đỏ, hồng tím)
            hue >= 315f && hue < 345f -> ColorChannel.MAGENTA
            
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
     * Tạo preview bitmap đã được điều chỉnh màu sắc với tối ưu hiệu suất
     */
    suspend fun createPreviewBitmap(
        originalBitmap: Bitmap, 
        adjustments: ColorAdjustmentValues
    ): Bitmap = withContext(Dispatchers.Default) {
        // Kiểm tra nếu không có điều chỉnh nào
        if (!hasAnyAdjustments(adjustments)) {
            return@withContext scaleDownBitmap(originalBitmap, 400)
        }
        
        // Thu nhỏ bitmap để tăng tốc độ xử lý preview
        val maxPreviewSize = 400 // Max width/height cho preview
        val scaledBitmap = scaleDownBitmap(originalBitmap, maxPreviewSize)
        
        // Áp dụng color adjustments lên bitmap đã thu nhỏ
        applyColorAdjustments(scaledBitmap, adjustments)
    }

    /**
     * Thu nhỏ bitmap với chất lượng tối ưu
     */
    private fun scaleDownBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // Không cần scale nếu ảnh đã nhỏ hơn maxSize
        if (width <= maxSize && height <= maxSize) {
            return bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, false)
        }
        
        // Tính toán tỷ lệ scale để giữ nguyên aspect ratio
        val scale = minOf(
            maxSize.toFloat() / width,
            maxSize.toFloat() / height
        )
        
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        
        // Sử dụng Matrix để có chất lượng scaling tốt hơn
        val matrix = Matrix().apply {
            setScale(scale, scale)
        }
        
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
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
     * Chuyển đổi mượt mà giữa các giá trị điều chỉnh màu sắc với easing function
     */
    fun interpolateAdjustments(
        from: ColorAdjustmentValues,
        to: ColorAdjustmentValues,
        progress: Float,
        useEasing: Boolean = true
    ): ColorAdjustmentValues {
        val t = progress.coerceIn(0f, 1f)
        val easedT = if (useEasing) easeInOutCubic(t) else t
        
        return ColorAdjustmentValues(
            redHue = lerpHue(from.redHue, to.redHue, easedT),
            redSaturation = lerp(from.redSaturation, to.redSaturation, easedT),
            redLuminance = lerp(from.redLuminance, to.redLuminance, easedT),
            
            orangeHue = lerpHue(from.orangeHue, to.orangeHue, easedT),
            orangeSaturation = lerp(from.orangeSaturation, to.orangeSaturation, easedT),
            orangeLuminance = lerp(from.orangeLuminance, to.orangeLuminance, easedT),
            
            yellowHue = lerpHue(from.yellowHue, to.yellowHue, easedT),
            yellowSaturation = lerp(from.yellowSaturation, to.yellowSaturation, easedT),
            yellowLuminance = lerp(from.yellowLuminance, to.yellowLuminance, easedT),
            
            greenHue = lerpHue(from.greenHue, to.greenHue, easedT),
            greenSaturation = lerp(from.greenSaturation, to.greenSaturation, easedT),
            greenLuminance = lerp(from.greenLuminance, to.greenLuminance, easedT),
            
            cyanHue = lerpHue(from.cyanHue, to.cyanHue, easedT),
            cyanSaturation = lerp(from.cyanSaturation, to.cyanSaturation, easedT),
            cyanLuminance = lerp(from.cyanLuminance, to.cyanLuminance, easedT),
            
            blueHue = lerpHue(from.blueHue, to.blueHue, easedT),
            blueSaturation = lerp(from.blueSaturation, to.blueSaturation, easedT),
            blueLuminance = lerp(from.blueLuminance, to.blueLuminance, easedT),
            
            purpleHue = lerpHue(from.purpleHue, to.purpleHue, easedT),
            purpleSaturation = lerp(from.purpleSaturation, to.purpleSaturation, easedT),
            purpleLuminance = lerp(from.purpleLuminance, to.purpleLuminance, easedT),
            
            magentaHue = lerpHue(from.magentaHue, to.magentaHue, easedT),
            magentaSaturation = lerp(from.magentaSaturation, to.magentaSaturation, easedT),
            magentaLuminance = lerp(from.magentaLuminance, to.magentaLuminance, easedT)
        )
    }

    /**
     * Linear interpolation cho giá trị thông thường
     */
    private fun lerp(start: Float, end: Float, t: Float): Float {
        return start + (end - start) * t
    }
    
    /**
     * Hue interpolation với xử lý đặc biệt cho góc 360°
     */
    private fun lerpHue(startHue: Float, endHue: Float, t: Float): Float {
        val diff = endHue - startHue
        return when {
            diff > 180f -> startHue + (diff - 360f) * t
            diff < -180f -> startHue + (diff + 360f) * t
            else -> startHue + diff * t
        }
    }
    
    /**
     * Easing function cho chuyển đổi mượt mà
     */
    private fun easeInOutCubic(t: Float): Float {
        return if (t < 0.5f) {
            4f * t * t * t
        } else {
            1f - (-2f * t + 2f).let { it * it * it } / 2f
        }
    }
}