package com.xiaomi.base.ui.screens.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaomi.base.R
import com.xiaomi.base.ui.screens.camera.filter.FilterType

/**
 * Filter preview item component for camera filter selection
 * Shows a preview thumbnail and filter name
 */
@Composable
fun FilterPreviewItem(
    filterType: FilterType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        Color.Black.copy(alpha = 0.3f)
    }
    
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Filter preview thumbnail
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Preview content based on filter type
            FilterPreviewContent(
                filterType = filterType,
                isSelected = isSelected
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Filter name
        Text(
            text = getFilterDisplayName(filterType),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White
            },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun FilterPreviewContent(
    filterType: FilterType,
    isSelected: Boolean
) {
    // Simple colored circle to represent filter effect
    val filterColor = when (filterType) {
        FilterType.ORIGINAL -> Color.White
        FilterType.SEPIA -> Color(0xFFD2B48C) // Tan color
        FilterType.BLACK_WHITE -> Color.Gray
        FilterType.VINTAGE -> Color(0xFFDEB887) // Burlywood
        FilterType.COOL -> Color(0xFF87CEEB) // Sky blue
        FilterType.WARM -> Color(0xFFFFB347) // Peach
    }
    
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(filterColor),
        contentAlignment = Alignment.Center
    ) {
        // Add a subtle pattern or icon to make it more recognizable
        when (filterType) {
            FilterType.ORIGINAL -> {
                Text(
                    text = "O",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            FilterType.SEPIA -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFDEB887),
                                    Color(0xFF8B7355)
                                )
                            )
                        )
                )
            }
            FilterType.BLACK_WHITE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.Black
                                )
                            )
                        )
                )
            }
            FilterType.VINTAGE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFDEB887),
                                    Color(0xFF8B4513)
                                )
                            )
                        )
                )
            }
            FilterType.COOL -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF87CEEB),
                                    Color(0xFF4682B4)
                                )
                            )
                        )
                )
            }
            FilterType.WARM -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFB347),
                                    Color(0xFFFF6347)
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
private fun getFilterDisplayName(filterType: FilterType): String {
    return when (filterType) {
        FilterType.ORIGINAL -> stringResource(R.string.filter_original)
        FilterType.SEPIA -> stringResource(R.string.filter_sepia)
        FilterType.BLACK_WHITE -> stringResource(R.string.filter_black_white)
        FilterType.VINTAGE -> stringResource(R.string.filter_vintage)
        FilterType.COOL -> stringResource(R.string.filter_cool)
        FilterType.WARM -> stringResource(R.string.filter_warm)
    }
}