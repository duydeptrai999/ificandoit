package com.xiaomi.base.ui.screens.camera.components

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaomi.base.R
import com.xiaomi.base.ui.theme.BaseTheme
import com.xiaomi.base.ui.screens.camera.utils.PhotoUtils
import kotlinx.coroutines.launch

data class AdjustmentValues(
    val brightness: Float = 0f, // -100 to 100
    val contrast: Float = 0f,   // -100 to 100
    val saturation: Float = 0f, // -100 to 100
    val hue: Float = 0f,        // -180 to 180
    val highlights: Float = 0f, // -100 to 100
    val shadows: Float = 0f,    // -100 to 100
    val warmth: Float = 0f,     // -100 to 100
    val tint: Float = 0f        // -100 to 100
)

@Composable
fun PhotoAdjustView(
    originalBitmap: Bitmap,
    onAdjustmentApplied: (Bitmap) -> Unit,
    onCancel: () -> Unit,
    onPreviewUpdate: ((Bitmap) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var adjustmentValues by remember { mutableStateOf(AdjustmentValues()) }
    var adjustedBitmap by remember { mutableStateOf(originalBitmap) }
    var isProcessing by remember { mutableStateOf(false) }
    
    // Apply adjustments when values change
    LaunchedEffect(adjustmentValues) {
        scope.launch {
            isProcessing = true
            try {
                val newBitmap = PhotoUtils.applyAdjustments(originalBitmap, adjustmentValues)
                adjustedBitmap = newBitmap ?: originalBitmap
                // Cập nhật ảnh preview ở trên theo thời gian thực
                onPreviewUpdate?.invoke(adjustedBitmap)
            } catch (e: Exception) {
                // Handle error
                adjustedBitmap = originalBitmap
                onPreviewUpdate?.invoke(originalBitmap)
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
        // Processing indicator overlay
        if (isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Adjustment controls - chiếm toàn bộ chiều cao còn lại
        AdjustmentControlsPanel(
            adjustmentValues = adjustmentValues,
            onAdjustmentChanged = { newValues ->
                adjustmentValues = newValues
            },
            onCancel = onCancel,
            onReset = {
                adjustmentValues = AdjustmentValues()
            },
            onApply = {
                onAdjustmentApplied(adjustedBitmap)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.7f)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun AdjustTopBar(
    onCancel: () -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.9f),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel button
            TextButton(
                onClick = onCancel
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Title
            Text(
                text = stringResource(R.string.adjust),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            
            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = onReset
                ) {
                    Text(
                        text = stringResource(R.string.reset),
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                TextButton(
                    onClick = onApply
                ) {
                    Text(
                    text = stringResource(R.string.apply),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                }
            }
        }
    }
}

@Composable
private fun AdjustmentControlsPanel(
    adjustmentValues: AdjustmentValues,
    onAdjustmentChanged: (AdjustmentValues) -> Unit,
    onCancel: () -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Basic adjustments
                Text(
                    text = stringResource(R.string.basic),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.brightness),
                    value = adjustmentValues.brightness,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(brightness = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Brightness6
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.contrast),
                    value = adjustmentValues.contrast,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(contrast = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Contrast
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.saturation),
                    value = adjustmentValues.saturation,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(saturation = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Palette
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Advanced adjustments
                Text(
                    text = stringResource(R.string.advanced),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.highlights),
                    value = adjustmentValues.highlights,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(highlights = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.LightMode
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.shadows),
                    value = adjustmentValues.shadows,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(shadows = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.DarkMode
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.warmth),
                    value = adjustmentValues.warmth,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(warmth = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Thermostat
                )
                
                AdjustmentSlider(
                    label = stringResource(R.string.tint),
                    value = adjustmentValues.tint,
                    onValueChange = { value ->
                        onAdjustmentChanged(adjustmentValues.copy(tint = value))
                    },
                    valueRange = -100f..100f,
                    icon = Icons.Default.Colorize
                )
                
                // Add some bottom padding for better scrolling
                Spacer(modifier = Modifier.height(12.dp))
            }
            

        }
    }
}

@Composable
private fun AdjustmentSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp)
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
                    contentDescription = label,
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
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = Color.White.copy(alpha = 0.4f)
            )
        )
    }
}