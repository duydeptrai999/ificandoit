package com.xiaomi.base.utils

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Utility class for handling different window size classes and adaptive layouts
 * Provides responsive design support for different screen sizes and orientations
 */
object WindowSizeClassUtils {
    /**
     * Get responsive padding based on window size class
     */
    @Composable
    fun getResponsivePadding(windowSizeClass: WindowSizeClass): ResponsivePadding {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact ->
                ResponsivePadding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                    content = 12.dp,
                )
            WindowWidthSizeClass.Medium ->
                ResponsivePadding(
                    horizontal = 24.dp,
                    vertical = 16.dp,
                    content = 16.dp,
                )
            WindowWidthSizeClass.Expanded ->
                ResponsivePadding(
                    horizontal = 32.dp,
                    vertical = 24.dp,
                    content = 20.dp,
                )
            else ->
                ResponsivePadding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                    content = 12.dp,
                )
        }
    }

    /**
     * Get responsive grid columns based on window size class
     */
    @Composable
    fun getGridColumns(windowSizeClass: WindowSizeClass): Int {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 1
            WindowWidthSizeClass.Medium -> 2
            WindowWidthSizeClass.Expanded -> 3
            else -> 1
        }
    }

    /**
     * Get responsive card width based on window size class
     */
    @Composable
    fun getCardWidth(windowSizeClass: WindowSizeClass): Dp {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 280.dp
            WindowWidthSizeClass.Medium -> 320.dp
            WindowWidthSizeClass.Expanded -> 360.dp
            else -> 280.dp
        }
    }

    /**
     * Get responsive navigation type based on window size class
     */
    @Composable
    fun getNavigationType(windowSizeClass: WindowSizeClass): NavigationType {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION
            WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded -> NavigationType.PERMANENT_NAVIGATION_DRAWER
            else -> NavigationType.BOTTOM_NAVIGATION
        }
    }

    /**
     * Get responsive content type based on window size class
     */
    @Composable
    fun getContentType(windowSizeClass: WindowSizeClass): ContentType {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> ContentType.SINGLE_PANE
            WindowWidthSizeClass.Medium ->
                when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.Medium,
                    WindowHeightSizeClass.Expanded,
                    -> ContentType.DUAL_PANE
                    else -> ContentType.SINGLE_PANE
                }
            WindowWidthSizeClass.Expanded -> ContentType.DUAL_PANE
            else -> ContentType.SINGLE_PANE
        }
    }

    /**
     * Check if the current window size supports dual pane layout
     */
    @Composable
    fun supportsDualPane(windowSizeClass: WindowSizeClass): Boolean {
        return getContentType(windowSizeClass) == ContentType.DUAL_PANE
    }

    /**
     * Check if the current window size is compact
     */
    @Composable
    fun isCompact(windowSizeClass: WindowSizeClass): Boolean {
        return windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    }

    /**
     * Check if the current window size is expanded
     */
    @Composable
    fun isExpanded(windowSizeClass: WindowSizeClass): Boolean {
        return windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    }

    /**
     * Get responsive font scale based on window size class
     */
    @Composable
    fun getFontScale(windowSizeClass: WindowSizeClass): Float {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 1.0f
            WindowWidthSizeClass.Medium -> 1.1f
            WindowWidthSizeClass.Expanded -> 1.2f
            else -> 1.0f
        }
    }

    /**
     * Get responsive image size based on window size class
     */
    @Composable
    fun getImageSize(windowSizeClass: WindowSizeClass): ImageSize {
        return when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact ->
                ImageSize(
                    width = 120.dp,
                    height = 180.dp,
                )
            WindowWidthSizeClass.Medium ->
                ImageSize(
                    width = 140.dp,
                    height = 210.dp,
                )
            WindowWidthSizeClass.Expanded ->
                ImageSize(
                    width = 160.dp,
                    height = 240.dp,
                )
            else ->
                ImageSize(
                    width = 120.dp,
                    height = 180.dp,
                )
        }
    }
}

/**
 * Data class for responsive padding values
 */
data class ResponsivePadding(
    val horizontal: Dp,
    val vertical: Dp,
    val content: Dp,
)

/**
 * Enum for different navigation types based on screen size
 */
enum class NavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER,
}

/**
 * Enum for different content layout types
 */
enum class ContentType {
    SINGLE_PANE,
    DUAL_PANE,
}

/**
 * Data class for responsive image sizes
 */
data class ImageSize(
    val width: Dp,
    val height: Dp,
)

/**
 * Extension function to get device type based on window size class
 */
@Composable
fun WindowSizeClass.getDeviceType(): DeviceType {
    return when (this.widthSizeClass) {
        WindowWidthSizeClass.Compact -> DeviceType.PHONE
        WindowWidthSizeClass.Medium -> DeviceType.TABLET
        WindowWidthSizeClass.Expanded -> DeviceType.DESKTOP
        else -> DeviceType.PHONE
    }
}

/**
 * Enum for different device types
 */
enum class DeviceType {
    PHONE,
    TABLET,
    DESKTOP,
}

/**
 * Composable function to remember window size class from configuration
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current

    return remember(configuration) {
        val widthDp = configuration.screenWidthDp.dp
        val heightDp = configuration.screenHeightDp.dp

        val widthSizeClass =
            when {
                widthDp < 600.dp -> WindowWidthSizeClass.Compact
                widthDp < 840.dp -> WindowWidthSizeClass.Medium
                else -> WindowWidthSizeClass.Expanded
            }

        val heightSizeClass =
            when {
                heightDp < 480.dp -> WindowHeightSizeClass.Compact
                heightDp < 900.dp -> WindowHeightSizeClass.Medium
                else -> WindowHeightSizeClass.Expanded
            }

        WindowSizeClass.calculateFromSize(
            size =
                DpSize(
                    width = widthDp,
                    height = heightDp,
                ),
        )
    }
}

/**
 * Utility functions for responsive breakpoints
 */
object ResponsiveBreakpoints {
    val COMPACT_WIDTH = 600.dp
    val MEDIUM_WIDTH = 840.dp
    val COMPACT_HEIGHT = 480.dp
    val MEDIUM_HEIGHT = 900.dp

    @Composable
    fun isCompactWidth(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp.dp < COMPACT_WIDTH
    }

    @Composable
    fun isMediumWidth(): Boolean {
        val configuration = LocalConfiguration.current
        val widthDp = configuration.screenWidthDp.dp
        return widthDp >= COMPACT_WIDTH && widthDp < MEDIUM_WIDTH
    }

    @Composable
    fun isExpandedWidth(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp.dp >= MEDIUM_WIDTH
    }

    @Composable
    fun isCompactHeight(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp.dp < COMPACT_HEIGHT
    }

    @Composable
    fun isMediumHeight(): Boolean {
        val configuration = LocalConfiguration.current
        val heightDp = configuration.screenHeightDp.dp
        return heightDp >= COMPACT_HEIGHT && heightDp < MEDIUM_HEIGHT
    }

    @Composable
    fun isExpandedHeight(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp.dp >= MEDIUM_HEIGHT
    }
}
