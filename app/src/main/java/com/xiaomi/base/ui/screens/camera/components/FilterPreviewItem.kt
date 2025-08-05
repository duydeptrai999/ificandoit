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
        // New filters
        FilterType.PINK_DREAM -> Color(0xFFFF69B4) // Hot pink
        FilterType.RETRO_80S -> Color(0xFFFF00FF) // Magenta
        FilterType.OLD_FILM -> Color(0xFF8B7355) // Dark tan
        FilterType.SPRING -> Color(0xFF90EE90) // Light green
        FilterType.SUMMER -> Color(0xFFFFD700) // Gold
        FilterType.AUTUMN -> Color(0xFFFF8C00) // Dark orange
        FilterType.WINTER -> Color(0xFF87CEFA) // Light sky blue
        FilterType.NEON_NIGHTS -> Color(0xFF00FFFF) // Cyan
        FilterType.GOLDEN_HOUR -> Color(0xFFFFB347) // Peach
        FilterType.CYBERPUNK -> Color(0xFF9400D3) // Violet
        FilterType.CHERRY_BLOSSOM -> Color(0xFFFFB6C1) // Light pink
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
            // New filters
            FilterType.PINK_DREAM -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFF69B4),
                                    Color(0xFFFF1493)
                                )
                            )
                        )
                )
            }
            FilterType.RETRO_80S -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF00FF),
                                    Color(0xFF00FFFF)
                                )
                            )
                        )
                )
            }
            FilterType.OLD_FILM -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF8B7355),
                                    Color(0xFF654321)
                                )
                            )
                        )
                )
            }
            FilterType.SPRING -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF90EE90),
                                    Color(0xFF32CD32)
                                )
                            )
                        )
                )
            }
            FilterType.SUMMER -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFD700),
                                    Color(0xFFFF8C00)
                                )
                            )
                        )
                )
            }
            FilterType.AUTUMN -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF8C00),
                                    Color(0xFFDC143C)
                                )
                            )
                        )
                )
            }
            FilterType.WINTER -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF87CEFA),
                                    Color(0xFF4169E1)
                                )
                            )
                        )
                )
            }
            FilterType.NEON_NIGHTS -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00FFFF),
                                    Color(0xFFFF00FF)
                                )
                            )
                        )
                )
            }
            FilterType.GOLDEN_HOUR -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFD700),
                                    Color(0xFFFF6347)
                                )
                            )
                        )
                )
            }
            FilterType.CYBERPUNK -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF9400D3),
                                    Color(0xFF00FFFF)
                                )
                            )
                        )
                )
            }
            FilterType.CHERRY_BLOSSOM -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFB6C1),
                                    Color(0xFFFFC0CB),
                                    Color(0xFFFFE4E1)
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
        // New filters
        FilterType.PINK_DREAM -> stringResource(R.string.filter_pink_dream)
        FilterType.RETRO_80S -> stringResource(R.string.filter_retro_80s)
        FilterType.OLD_FILM -> stringResource(R.string.filter_old_film)
        FilterType.SPRING -> stringResource(R.string.filter_spring)
        FilterType.SUMMER -> stringResource(R.string.filter_summer)
        FilterType.AUTUMN -> stringResource(R.string.filter_autumn)
        FilterType.WINTER -> stringResource(R.string.filter_winter)
        FilterType.NEON_NIGHTS -> stringResource(R.string.filter_neon_nights)
        FilterType.GOLDEN_HOUR -> stringResource(R.string.filter_golden_hour)
        FilterType.CYBERPUNK -> stringResource(R.string.filter_cyberpunk)
        FilterType.CHERRY_BLOSSOM -> stringResource(R.string.filter_cherry_blossom)
    }
}