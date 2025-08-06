package com.xiaomi.base.ui.screens.camera.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaomi.base.R
import kotlin.math.*

/**
 * Photo crop view component with interactive crop rectangle
 * Allows users to select crop area by dragging corners and edges
 */
@Composable
fun PhotoCropView(
    bitmap: Bitmap,
    onCropConfirmed: (Bitmap) -> Unit,
    onCropCancelled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var cropRect by remember { mutableStateOf(Rect.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var dragHandle by remember { mutableStateOf(DragHandle.NONE) }
    
    // Crop aspect ratios
    val aspectRatios = listOf(
        CropAspectRatio("Free", null),
        CropAspectRatio("1:1", 1f),
        CropAspectRatio("4:3", 4f/3f),
        CropAspectRatio("16:9", 16f/9f),
        CropAspectRatio("3:4", 3f/4f),
        CropAspectRatio("9:16", 9f/16f)
    )
    
    var selectedAspectRatio by remember { mutableStateOf(aspectRatios[0]) }
    // Removed isAspectRatioLocked - no longer needed
    
    // Initialize crop rect when canvas size changes
    LaunchedEffect(canvasSize) {
        if (canvasSize != IntSize.Zero && cropRect == Rect.Zero) {
            val padding = 40f
            cropRect = Rect(
                offset = Offset(padding, padding),
                size = Size(
                    canvasSize.width - padding * 2,
                    canvasSize.height - padding * 2
                )
            )
        }
    }
    
    // Update crop rect when aspect ratio changes
    LaunchedEffect(selectedAspectRatio) {
        if (canvasSize != IntSize.Zero && selectedAspectRatio.ratio != null) {
            val centerX = canvasSize.width / 2f
            val centerY = canvasSize.height / 2f
            val maxWidth = canvasSize.width - 80f
            val maxHeight = canvasSize.height - 80f
            
            val ratio = selectedAspectRatio.ratio!!
            val newWidth: Float
            val newHeight: Float
            
            if (ratio > 1) {
                // Landscape
                newWidth = minOf(maxWidth, maxHeight * ratio)
                newHeight = newWidth / ratio
            } else {
                // Portrait
                newHeight = minOf(maxHeight, maxWidth / ratio)
                newWidth = newHeight * ratio
            }
            
            cropRect = Rect(
                offset = Offset(
                    centerX - newWidth / 2,
                    centerY - newHeight / 2
                ),
                size = Size(newWidth, newHeight)
            )
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top toolbar
        CropTopBar(
            onCancel = onCropCancelled,
            onConfirm = {
                val croppedBitmap = cropBitmap(bitmap, cropRect, canvasSize)
                onCropConfirmed(croppedBitmap)
            },
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        
        // Crop canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { canvasSize = it }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                dragHandle = getDragHandle(offset, cropRect)
                                isDragging = true
                            },
                            onDragEnd = {
                                isDragging = false
                                dragHandle = DragHandle.NONE
                            },
                            onDrag = { _, dragAmount ->
                                // Always use free form crop for dragging
                                // Aspect ratio is only applied when selecting, not when dragging
                                cropRect = updateCropRect(
                                    cropRect,
                                    dragHandle,
                                    dragAmount,
                                    canvasSize
                                )
                            }
                        )
                    }
            ) {
                // Draw bitmap
                val imageBitmap = bitmap.asImageBitmap()
                val imageRect = calculateImageRect(imageBitmap, size)
                drawImage(
                    image = imageBitmap,
                    dstOffset = IntOffset(imageRect.left.toInt(), imageRect.top.toInt()),
                    dstSize = IntSize(imageRect.width.toInt(), imageRect.height.toInt())
                )
                
                // Draw overlay
                drawCropOverlay(cropRect, size)
                
                // Draw crop handles
                drawCropHandles(cropRect)
            }
        }
        
        // Bottom controls
        CropBottomBar(
            aspectRatios = aspectRatios,
            selectedAspectRatio = selectedAspectRatio,
            onAspectRatioSelected = { newRatio ->
                selectedAspectRatio = newRatio
                // Apply aspect ratio to current crop rect immediately
                if (newRatio.ratio != null && canvasSize != IntSize.Zero) {
                    cropRect = applyCropAspectRatio(cropRect, newRatio.ratio!!, canvasSize)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun CropTopBar(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.9f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            
            Text(
                text = stringResource(R.string.crop),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.done),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CropBottomBar(
    aspectRatios: List<CropAspectRatio>,
    selectedAspectRatio: CropAspectRatio,
    onAspectRatioSelected: (CropAspectRatio) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.9f)
    ) {
        Column {
            // Aspect ratio buttons
            LazyRow(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(aspectRatios) { ratio ->
                    AspectRatioButton(
                        aspectRatio = ratio,
                        isSelected = selectedAspectRatio == ratio,
                        onClick = { onAspectRatioSelected(ratio) }
                    )
                }
            }
            
            // Removed lock/unlock toggle - aspect ratio is applied once when selected,
            // then user can freely adjust the crop area
        }
    }
}

@Composable
private fun AspectRatioButton(
    aspectRatio: CropAspectRatio,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else Color.Transparent
            )
    ) {
        Text(
            text = aspectRatio.name,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

// Helper functions
private fun DrawScope.drawCropOverlay(cropRect: Rect, canvasSize: Size) {
    // Draw dark overlay outside crop area
    val overlayColor = Color.Black.copy(alpha = 0.5f)
    
    // Top
    drawRect(
        color = overlayColor,
        topLeft = Offset.Zero,
        size = Size(canvasSize.width, cropRect.top)
    )
    
    // Bottom
    drawRect(
        color = overlayColor,
        topLeft = Offset(0f, cropRect.bottom),
        size = Size(canvasSize.width, canvasSize.height - cropRect.bottom)
    )
    
    // Left
    drawRect(
        color = overlayColor,
        topLeft = Offset(0f, cropRect.top),
        size = Size(cropRect.left, cropRect.height)
    )
    
    // Right
    drawRect(
        color = overlayColor,
        topLeft = Offset(cropRect.right, cropRect.top),
        size = Size(canvasSize.width - cropRect.right, cropRect.height)
    )
    
    // Draw crop border
    drawRect(
        color = Color.White,
        topLeft = cropRect.topLeft,
        size = cropRect.size,
        style = Stroke(width = 2.dp.toPx())
    )
    
    // Draw grid lines
    val gridColor = Color.White.copy(alpha = 0.5f)
    val gridStroke = Stroke(width = 1.dp.toPx())
    
    // Vertical lines
    val verticalStep = cropRect.width / 3
    for (i in 1..2) {
        val x = cropRect.left + verticalStep * i
        drawLine(
            color = gridColor,
            start = Offset(x, cropRect.top),
            end = Offset(x, cropRect.bottom),
            strokeWidth = gridStroke.width
        )
    }
    
    // Horizontal lines
    val horizontalStep = cropRect.height / 3
    for (i in 1..2) {
        val y = cropRect.top + horizontalStep * i
        drawLine(
            color = gridColor,
            start = Offset(cropRect.left, y),
            end = Offset(cropRect.right, y),
            strokeWidth = gridStroke.width
        )
    }
}

private fun DrawScope.drawCropHandles(cropRect: Rect) {
    val handleSize = 20.dp.toPx()
    val handleColor = Color.White
    val handleStroke = Stroke(width = 3.dp.toPx())
    
    // Corner handles
    val corners = listOf(
        cropRect.topLeft,
        Offset(cropRect.right, cropRect.top),
        cropRect.bottomRight,
        Offset(cropRect.left, cropRect.bottom)
    )
    
    corners.forEach { corner ->
        drawCircle(
            color = handleColor,
            radius = handleSize / 2,
            center = corner,
            style = handleStroke
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.3f),
            radius = handleSize / 2 - handleStroke.width,
            center = corner
        )
    }
    
    // Edge handles
    val edges = listOf(
        Offset(cropRect.center.x, cropRect.top), // Top
        Offset(cropRect.right, cropRect.center.y), // Right
        Offset(cropRect.center.x, cropRect.bottom), // Bottom
        Offset(cropRect.left, cropRect.center.y) // Left
    )
    
    edges.forEach { edge ->
        drawCircle(
            color = handleColor,
            radius = handleSize / 3,
            center = edge,
            style = handleStroke
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.3f),
            radius = handleSize / 3 - handleStroke.width,
            center = edge
        )
    }
}

private fun calculateImageRect(imageBitmap: ImageBitmap, canvasSize: Size): Rect {
    val imageAspectRatio = imageBitmap.width.toFloat() / imageBitmap.height.toFloat()
    val canvasAspectRatio = canvasSize.width / canvasSize.height
    
    return if (imageAspectRatio > canvasAspectRatio) {
        // Image is wider, fit to width
        val scaledHeight = canvasSize.width / imageAspectRatio
        val offsetY = (canvasSize.height - scaledHeight) / 2
        Rect(
            offset = Offset(0f, offsetY),
            size = Size(canvasSize.width, scaledHeight)
        )
    } else {
        // Image is taller, fit to height
        val scaledWidth = canvasSize.height * imageAspectRatio
        val offsetX = (canvasSize.width - scaledWidth) / 2
        Rect(
            offset = Offset(offsetX, 0f),
            size = Size(scaledWidth, canvasSize.height)
        )
    }
}

private fun getDragHandle(offset: Offset, cropRect: Rect): DragHandle {
    val handleSize = 40f // Touch area
    
    // Check corners first
    if (offset.isNear(cropRect.topLeft, handleSize)) return DragHandle.TOP_LEFT
    if (offset.isNear(Offset(cropRect.right, cropRect.top), handleSize)) return DragHandle.TOP_RIGHT
    if (offset.isNear(cropRect.bottomRight, handleSize)) return DragHandle.BOTTOM_RIGHT
    if (offset.isNear(Offset(cropRect.left, cropRect.bottom), handleSize)) return DragHandle.BOTTOM_LEFT
    
    // Check edge handles
    if (offset.isNear(Offset(cropRect.center.x, cropRect.top), handleSize)) return DragHandle.TOP
    if (offset.isNear(Offset(cropRect.right, cropRect.center.y), handleSize)) return DragHandle.RIGHT
    if (offset.isNear(Offset(cropRect.center.x, cropRect.bottom), handleSize)) return DragHandle.BOTTOM
    if (offset.isNear(Offset(cropRect.left, cropRect.center.y), handleSize)) return DragHandle.LEFT
    
    // Check if inside crop area for moving
    if (cropRect.contains(offset)) return DragHandle.CENTER
    
    return DragHandle.NONE
}

private fun Offset.isNear(other: Offset, threshold: Float): Boolean {
    return (this - other).getDistance() <= threshold
}

private fun updateCropRect(
    currentRect: Rect,
    handle: DragHandle,
    dragAmount: Offset,
    canvasSize: IntSize
): Rect {
    val bounds = Rect(Offset.Zero, Size(canvasSize.width.toFloat(), canvasSize.height.toFloat()))
    val minSize = 50f
    
    return when (handle) {
        DragHandle.TOP_LEFT -> {
            val newLeft = (currentRect.left + dragAmount.x).coerceIn(0f, currentRect.right - minSize)
            val newTop = (currentRect.top + dragAmount.y).coerceIn(0f, currentRect.bottom - minSize)
            currentRect.copy(
                left = newLeft,
                top = newTop
            )
        }
        DragHandle.TOP_RIGHT -> {
            val newRight = (currentRect.right + dragAmount.x).coerceIn(currentRect.left + minSize, bounds.width)
            val newTop = (currentRect.top + dragAmount.y).coerceIn(0f, currentRect.bottom - minSize)
            currentRect.copy(
                right = newRight,
                top = newTop
            )
        }
        DragHandle.BOTTOM_RIGHT -> {
            val newRight = (currentRect.right + dragAmount.x).coerceIn(currentRect.left + minSize, bounds.width)
            val newBottom = (currentRect.bottom + dragAmount.y).coerceIn(currentRect.top + minSize, bounds.height)
            currentRect.copy(
                right = newRight,
                bottom = newBottom
            )
        }
        DragHandle.BOTTOM_LEFT -> {
            val newLeft = (currentRect.left + dragAmount.x).coerceIn(0f, currentRect.right - minSize)
            val newBottom = (currentRect.bottom + dragAmount.y).coerceIn(currentRect.top + minSize, bounds.height)
            currentRect.copy(
                left = newLeft,
                bottom = newBottom
            )
        }
        DragHandle.TOP -> {
            val newTop = (currentRect.top + dragAmount.y).coerceIn(0f, currentRect.bottom - minSize)
            currentRect.copy(top = newTop)
        }
        DragHandle.RIGHT -> {
            val newRight = (currentRect.right + dragAmount.x).coerceIn(currentRect.left + minSize, bounds.width)
            currentRect.copy(right = newRight)
        }
        DragHandle.BOTTOM -> {
            val newBottom = (currentRect.bottom + dragAmount.y).coerceIn(currentRect.top + minSize, bounds.height)
            currentRect.copy(bottom = newBottom)
        }
        DragHandle.LEFT -> {
            val newLeft = (currentRect.left + dragAmount.x).coerceIn(0f, currentRect.right - minSize)
            currentRect.copy(left = newLeft)
        }
        DragHandle.CENTER -> {
            val newLeft = (currentRect.left + dragAmount.x).coerceIn(0f, bounds.width - currentRect.width)
            val newTop = (currentRect.top + dragAmount.y).coerceIn(0f, bounds.height - currentRect.height)
            currentRect.copy(
                left = newLeft,
                top = newTop,
                right = newLeft + currentRect.width,
                bottom = newTop + currentRect.height
            )
        }
        else -> currentRect
    }
}

private fun updateCropRectWithAspectRatio(
    currentRect: Rect,
    handle: DragHandle,
    dragAmount: Offset,
    canvasSize: IntSize,
    aspectRatio: Float
): Rect {
    val bounds = Rect(Offset.Zero, Size(canvasSize.width.toFloat(), canvasSize.height.toFloat()))
    val minSize = 50f
    
    return when (handle) {
        DragHandle.TOP_LEFT, DragHandle.TOP_RIGHT, DragHandle.BOTTOM_RIGHT, DragHandle.BOTTOM_LEFT -> {
            // For corner handles, maintain aspect ratio
            val centerX = currentRect.center.x
            val centerY = currentRect.center.y
            
            val dragDistance = when (handle) {
                DragHandle.TOP_LEFT -> -dragAmount.x - dragAmount.y
                DragHandle.TOP_RIGHT -> dragAmount.x - dragAmount.y
                DragHandle.BOTTOM_RIGHT -> dragAmount.x + dragAmount.y
                DragHandle.BOTTOM_LEFT -> -dragAmount.x + dragAmount.y
                else -> 0f
            } / 2f
            
            val currentWidth = currentRect.width
            val newWidth = (currentWidth + dragDistance).coerceAtLeast(minSize)
            val newHeight = newWidth / aspectRatio
            
            // Check bounds
            val maxWidth = bounds.width
            val maxHeight = bounds.height
            val finalWidth = minOf(newWidth, maxWidth, maxHeight * aspectRatio)
            val finalHeight = finalWidth / aspectRatio
            
            Rect(
                left = centerX - finalWidth / 2,
                top = centerY - finalHeight / 2,
                right = centerX + finalWidth / 2,
                bottom = centerY + finalHeight / 2
            ).let { rect ->
                // Adjust if out of bounds
                val offsetX = when {
                    rect.left < 0 -> -rect.left
                    rect.right > bounds.width -> bounds.width - rect.right
                    else -> 0f
                }
                val offsetY = when {
                    rect.top < 0 -> -rect.top
                    rect.bottom > bounds.height -> bounds.height - rect.bottom
                    else -> 0f
                }
                rect.translate(offsetX, offsetY)
            }
        }
        DragHandle.CENTER -> {
            val newLeft = (currentRect.left + dragAmount.x).coerceIn(0f, bounds.width - currentRect.width)
            val newTop = (currentRect.top + dragAmount.y).coerceIn(0f, bounds.height - currentRect.height)
            currentRect.copy(
                left = newLeft,
                top = newTop,
                right = newLeft + currentRect.width,
                bottom = newTop + currentRect.height
            )
        }
        else -> currentRect // Don't allow edge dragging for fixed aspect ratio
    }
}

private fun applyCropAspectRatio(
    currentRect: Rect,
    aspectRatio: Float,
    canvasSize: IntSize
): Rect {
    val centerX = currentRect.center.x
    val centerY = currentRect.center.y
    
    val currentWidth = currentRect.width
    val currentHeight = currentRect.height
    
    // Calculate new dimensions based on aspect ratio
    val newWidth: Float
    val newHeight: Float
    
    if (currentWidth / currentHeight > aspectRatio) {
        // Current rect is wider than target ratio, adjust width
        newHeight = currentHeight
        newWidth = newHeight * aspectRatio
    } else {
        // Current rect is taller than target ratio, adjust height
        newWidth = currentWidth
        newHeight = newWidth / aspectRatio
    }
    
    // Calculate new bounds centered on current position
    val left = (centerX - newWidth / 2f).coerceAtLeast(0f)
    val top = (centerY - newHeight / 2f).coerceAtLeast(0f)
    val right = (left + newWidth).coerceAtMost(canvasSize.width.toFloat())
    val bottom = (top + newHeight).coerceAtMost(canvasSize.height.toFloat())
    
    // Adjust if bounds exceed canvas
    val finalLeft = if (right - left < newWidth) {
        (right - newWidth).coerceAtLeast(0f)
    } else left
    
    val finalTop = if (bottom - top < newHeight) {
        (bottom - newHeight).coerceAtLeast(0f)
    } else top
    
    return Rect(
        left = finalLeft,
        top = finalTop,
        right = finalLeft + newWidth,
        bottom = finalTop + newHeight
    )
}

private fun cropBitmap(originalBitmap: Bitmap, cropRect: Rect, canvasSize: IntSize): Bitmap {
    // Calculate the scale factor between canvas and bitmap
    val scaleX = originalBitmap.width.toFloat() / canvasSize.width
    val scaleY = originalBitmap.height.toFloat() / canvasSize.height
    
    // Convert crop rect to bitmap coordinates
    val bitmapCropRect = android.graphics.Rect(
        (cropRect.left * scaleX).toInt(),
        (cropRect.top * scaleY).toInt(),
        (cropRect.right * scaleX).toInt(),
        (cropRect.bottom * scaleY).toInt()
    )
    
    // Ensure crop rect is within bitmap bounds
    val clampedRect = android.graphics.Rect(
        bitmapCropRect.left.coerceIn(0, originalBitmap.width),
        bitmapCropRect.top.coerceIn(0, originalBitmap.height),
        bitmapCropRect.right.coerceIn(0, originalBitmap.width),
        bitmapCropRect.bottom.coerceIn(0, originalBitmap.height)
    )
    
    return Bitmap.createBitmap(
        originalBitmap,
        clampedRect.left,
        clampedRect.top,
        clampedRect.width(),
        clampedRect.height()
    )
}

// Data classes
data class CropAspectRatio(
    val name: String,
    val ratio: Float? // null for free form
)

enum class DragHandle {
    NONE,
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT,
    CENTER
}