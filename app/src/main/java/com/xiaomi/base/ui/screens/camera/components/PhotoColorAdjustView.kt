package com.xiaomi.base.ui.screens.camera.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaomi.base.R
import com.xiaomi.base.ui.screens.camera.utils.PhotoUtils
import kotlinx.coroutines.launch

/**
 * Data class để lưu trữ giá trị điều chỉnh màu cho từng màu cụ thể
 */
data class ColorAdjustmentValues(
    val redHue: Float = 0f,        // -180 to 180
    val redSaturation: Float = 0f, // -100 to 100
    val redLuminance: Float = 0f,  // -100 to 100
    
    val orangeHue: Float = 0f,
    val orangeSaturation: Float = 0f,
    val orangeLuminance: Float = 0f,
    
    val yellowHue: Float = 0f,
    val yellowSaturation: Float = 0f,
    val yellowLuminance: Float = 0f,
    
    val greenHue: Float = 0f,
    val greenSaturation: Float = 0f,
    val greenLuminance: Float = 0f,
    
    val cyanHue: Float = 0f,
    val cyanSaturation: Float = 0f,
    val cyanLuminance: Float = 0f,
    
    val blueHue: Float = 0f,
    val blueSaturation: Float = 0f,
    val blueLuminance: Float = 0f,
    
    val purpleHue: Float = 0f,
    val purpleSaturation: Float = 0f,
    val purpleLuminance: Float = 0f,
    
    val magentaHue: Float = 0f,
    val magentaSaturation: Float = 0f,
    val magentaLuminance: Float = 0f
) {
    /**
     * Kiểm tra xem có bất kỳ điều chỉnh nào được áp dụng hay không
     */
    fun hasAnyAdjustments(): Boolean {
        return redHue != 0f || redSaturation != 0f || redLuminance != 0f ||
               orangeHue != 0f || orangeSaturation != 0f || orangeLuminance != 0f ||
               yellowHue != 0f || yellowSaturation != 0f || yellowLuminance != 0f ||
               greenHue != 0f || greenSaturation != 0f || greenLuminance != 0f ||
               cyanHue != 0f || cyanSaturation != 0f || cyanLuminance != 0f ||
               blueHue != 0f || blueSaturation != 0f || blueLuminance != 0f ||
               purpleHue != 0f || purpleSaturation != 0f || purpleLuminance != 0f ||
               magentaHue != 0f || magentaSaturation != 0f || magentaLuminance != 0f
    }
}

/**
 * Enum class định nghĩa các màu có thể điều chỉnh
 */
enum class ColorChannel(val displayName: String, val color: Color) {
    RED("Red", Color(0xFFFF0000)),
    ORANGE("Orange", Color(0xFFFF8000)),
    YELLOW("Yellow", Color(0xFFFFFF00)),
    GREEN("Green", Color(0xFF00FF00)),
    CYAN("Cyan", Color(0xFF00FFFF)),
    BLUE("Blue", Color(0xFF0000FF)),
    PURPLE("Purple", Color(0xFF8000FF)),
    MAGENTA("Magenta", Color(0xFFFF00FF))
}

/**
 * Component chính cho việc điều chỉnh màu chi tiết
 */
@Composable
fun PhotoColorAdjustView(
    originalBitmap: Bitmap,
    initialColorValues: ColorAdjustmentValues = ColorAdjustmentValues(),
    onColorAdjustmentApplied: (ColorAdjustmentValues) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var colorValues by remember { mutableStateOf(initialColorValues) }
    var selectedColor by remember { mutableStateOf(ColorChannel.RED) }
    var isProcessing by remember { mutableStateOf(false) }

    // Apply color adjustments automatically when values change
    LaunchedEffect(colorValues) {
        scope.launch {
            isProcessing = true
            try {
                // Automatically apply changes in real-time
                onColorAdjustmentApplied(colorValues)
            } catch (e: Exception) {
                // Handle error - reset to default values
                onColorAdjustmentApplied(ColorAdjustmentValues())
            } finally {
                isProcessing = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Processing indicator
        if (isProcessing) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }

        // Color selection bar
        ColorSelectionBar(
            selectedColor = selectedColor,
            onColorSelected = { selectedColor = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Color adjustment controls
        ColorAdjustmentControlsPanel(
            selectedColor = selectedColor,
            colorValues = colorValues,
            onColorValuesChanged = { newValues ->
                colorValues = newValues
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

/**
 * Thanh chọn màu ngang
 */
@Composable
private fun ColorSelectionBar(
    selectedColor: ColorChannel,
    onColorSelected: (ColorChannel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(ColorChannel.values()) { color ->
            ColorSelectionItem(
                color = color,
                isSelected = selectedColor == color,
                onClick = { onColorSelected(color) }
            )
        }
    }
}

/**
 * Item màu trong thanh chọn màu
 */
@Composable
private fun ColorSelectionItem(
    color: ColorChannel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Color circle
        Box(
            modifier = Modifier
                .size(if (isSelected) 36.dp else 30.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color.White else Color.Transparent,
                    CircleShape
                )
                .padding(if (isSelected) 3.dp else 0.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                drawCircle(
                    color = color.color,
                    radius = size.minDimension / 2
                )
            }
        }
        
        Spacer(modifier = Modifier.height(2.dp))
        
        // Color name
        Text(
            text = color.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 8.sp
        )
    }
}

/**
 * Panel điều khiển cho màu đã chọn
 */
@Composable
private fun ColorAdjustmentControlsPanel(
    selectedColor: ColorChannel,
    colorValues: ColorAdjustmentValues,
    onColorValuesChanged: (ColorAdjustmentValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
                // Selected color title
                Text(
                    text = "${selectedColor.displayName} Adjustments",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Get current values for selected color
                val (hue, saturation, luminance) = getCurrentValuesForColor(selectedColor, colorValues)

                // Hue slider
                ColorAdjustmentSlider(
                    label = stringResource(R.string.hue),
                    value = hue,
                    onValueChange = { value ->
                        onColorValuesChanged(updateColorValues(selectedColor, colorValues, hue = value))
                    },
                    valueRange = -180f..180f,
                    icon = Icons.Default.Palette
                )

                // Saturation slider
                ColorAdjustmentSlider(
                    label = stringResource(R.string.saturation),
                    value = saturation,
                    onValueChange = { value ->
                        onColorValuesChanged(updateColorValues(selectedColor, colorValues, saturation = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Colorize
                )

                // Luminance slider
                ColorAdjustmentSlider(
                    label = "Luminance",
                    value = luminance,
                    onValueChange = { value ->
                        onColorValuesChanged(updateColorValues(selectedColor, colorValues, luminance = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Brightness6
                )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Slider điều chỉnh màu tùy chỉnh
 */
@Composable
private fun ColorAdjustmentSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${value.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = Color.White.copy(alpha = 0.4f)
            )
        )
    }
}

/**
 * Helper function để lấy giá trị hiện tại cho màu đã chọn
 */
private fun getCurrentValuesForColor(
    color: ColorChannel,
    values: ColorAdjustmentValues
): Triple<Float, Float, Float> {
    return when (color) {
        ColorChannel.RED -> Triple(values.redHue, values.redSaturation, values.redLuminance)
        ColorChannel.ORANGE -> Triple(values.orangeHue, values.orangeSaturation, values.orangeLuminance)
        ColorChannel.YELLOW -> Triple(values.yellowHue, values.yellowSaturation, values.yellowLuminance)
        ColorChannel.GREEN -> Triple(values.greenHue, values.greenSaturation, values.greenLuminance)
        ColorChannel.CYAN -> Triple(values.cyanHue, values.cyanSaturation, values.cyanLuminance)
        ColorChannel.BLUE -> Triple(values.blueHue, values.blueSaturation, values.blueLuminance)
        ColorChannel.PURPLE -> Triple(values.purpleHue, values.purpleSaturation, values.purpleLuminance)
        ColorChannel.MAGENTA -> Triple(values.magentaHue, values.magentaSaturation, values.magentaLuminance)
    }
}

/**
 * Helper function để cập nhật giá trị cho màu đã chọn
 */
private fun updateColorValues(
    color: ColorChannel,
    values: ColorAdjustmentValues,
    hue: Float? = null,
    saturation: Float? = null,
    luminance: Float? = null
): ColorAdjustmentValues {
    return when (color) {
        ColorChannel.RED -> values.copy(
            redHue = hue ?: values.redHue,
            redSaturation = saturation ?: values.redSaturation,
            redLuminance = luminance ?: values.redLuminance
        )
        ColorChannel.ORANGE -> values.copy(
            orangeHue = hue ?: values.orangeHue,
            orangeSaturation = saturation ?: values.orangeSaturation,
            orangeLuminance = luminance ?: values.orangeLuminance
        )
        ColorChannel.YELLOW -> values.copy(
            yellowHue = hue ?: values.yellowHue,
            yellowSaturation = saturation ?: values.yellowSaturation,
            yellowLuminance = luminance ?: values.yellowLuminance
        )
        ColorChannel.GREEN -> values.copy(
            greenHue = hue ?: values.greenHue,
            greenSaturation = saturation ?: values.greenSaturation,
            greenLuminance = luminance ?: values.greenLuminance
        )
        ColorChannel.CYAN -> values.copy(
            cyanHue = hue ?: values.cyanHue,
            cyanSaturation = saturation ?: values.cyanSaturation,
            cyanLuminance = luminance ?: values.cyanLuminance
        )
        ColorChannel.BLUE -> values.copy(
            blueHue = hue ?: values.blueHue,
            blueSaturation = saturation ?: values.blueSaturation,
            blueLuminance = luminance ?: values.blueLuminance
        )
        ColorChannel.PURPLE -> values.copy(
            purpleHue = hue ?: values.purpleHue,
            purpleSaturation = saturation ?: values.purpleSaturation,
            purpleLuminance = luminance ?: values.purpleLuminance
        )
        ColorChannel.MAGENTA -> values.copy(
            magentaHue = hue ?: values.magentaHue,
            magentaSaturation = saturation ?: values.magentaSaturation,
            magentaLuminance = luminance ?: values.magentaLuminance
        )
    }
}