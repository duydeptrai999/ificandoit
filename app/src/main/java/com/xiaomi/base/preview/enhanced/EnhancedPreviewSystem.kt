package com.xiaomi.base.preview.enhanced

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.xiaomi.base.ui.theme.BaseAppTheme
import com.xiaomi.base.utils.WindowSizeClassUtils

/**
 * Enhanced Preview System for better development experience
 * Provides comprehensive preview capabilities with different configurations
 */

/**
 * Multi-configuration preview annotation
 */
@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Large Font", showBackground = true, fontScale = 1.5f)
@Preview(name = "Small Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Preview(name = "Tablet", showBackground = true, widthDp = 840, heightDp = 1200)
annotation class MultiConfigPreview

/**
 * Device-specific preview annotation
 */
@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673dp,height=841dp")
@Preview(name = "Tablet", device = "spec:width=1280dp,height=800dp")
@Preview(name = "Desktop", device = "spec:width=1920dp,height=1080dp")
annotation class DevicePreview

/**
 * Theme preview annotation
 */
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreview

/**
 * Font scale preview annotation
 */
@Preview(name = "Normal", showBackground = true, fontScale = 1.0f)
@Preview(name = "Large", showBackground = true, fontScale = 1.3f)
@Preview(name = "Largest", showBackground = true, fontScale = 1.5f)
annotation class FontScalePreview

/**
 * Enhanced preview container with comprehensive controls
 */
@Composable
fun EnhancedPreviewContainer(
    title: String,
    description: String? = null,
    showControls: Boolean = true,
    content: @Composable () -> Unit
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var fontScale by remember { mutableStateOf(1.0f) }
    var showBounds by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf(PreviewDevice.PHONE) }

    BaseAppTheme(darkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            PreviewHeader(
                title = title,
                description = description
            )

            // Controls
            if (showControls) {
                PreviewControls(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme },
                    fontScale = fontScale,
                    onFontScaleChange = { fontScale = it },
                    showBounds = showBounds,
                    onBoundsToggle = { showBounds = !showBounds },
                    selectedDevice = selectedDevice,
                    onDeviceChange = { selectedDevice = it }
                )
            }

            // Content Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .then(
                        if (showBounds) {
                            Modifier.border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                        } else Modifier
                    )
                    .padding(if (showBounds) 8.dp else 0.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * Preview header component
 */
@Composable
private fun PreviewHeader(
    title: String,
    description: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Preview controls component
 */
@Composable
private fun PreviewControls(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    showBounds: Boolean,
    onBoundsToggle: () -> Unit,
    selectedDevice: PreviewDevice,
    onDeviceChange: (PreviewDevice) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Theme Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Theme")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeToggle() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Font Scale
            Text("Font Scale: ${String.format("%.1f", fontScale)}")
            Slider(
                value = fontScale,
                onValueChange = onFontScaleChange,
                valueRange = 0.8f..2.0f,
                steps = 11
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show Bounds Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show Bounds")
                Switch(
                    checked = showBounds,
                    onCheckedChange = { onBoundsToggle() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Device Selection
            Text("Device Type")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                items(PreviewDevice.values()) { device ->
                    FilterChip(
                        onClick = { onDeviceChange(device) },
                        label = { Text(device.displayName) },
                        selected = selectedDevice == device
                    )
                }
            }
        }
    }
}

/**
 * Preview device types
 */
enum class PreviewDevice(val displayName: String, val width: Int, val height: Int) {
    PHONE("Phone", 411, 891),
    TABLET("Tablet", 840, 1200),
    FOLDABLE("Foldable", 673, 841),
    DESKTOP("Desktop", 1280, 800)
}

/**
 * Component showcase for systematic preview
 */
@Composable
fun ComponentShowcase(
    title: String,
    components: List<ShowcaseItem>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(components) { item ->
            ShowcaseCard(
                title = item.title,
                description = item.description,
                content = item.content
            )
        }
    }
}

/**
 * Individual showcase card
 */
@Composable
private fun ShowcaseCard(
    title: String,
    description: String?,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * Data class for showcase items
 */
data class ShowcaseItem(
    val title: String,
    val description: String? = null,
    val content: @Composable () -> Unit
)

/**
 * Preview parameter provider for sample data
 */
class SampleDataProvider : PreviewParameterProvider<SampleData> {
    override val values = sequenceOf(
        SampleData("Short Title", "Brief description"),
        SampleData("Very Long Title That Might Wrap", "This is a much longer description that demonstrates how the component handles extended content"),
        SampleData("Title", null),
        SampleData("", "Description only")
    )
}

data class SampleData(
    val title: String,
    val description: String?
)

/**
 * Responsive preview wrapper
 */
@Composable
fun ResponsivePreview(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current

    Column {
        // Size class info
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Window Size Class Info",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Screen: ${configuration.screenWidthDp}x${configuration.screenHeightDp}dp",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            content()
        }
    }
}

/**
 * Preview examples
 */
@MultiConfigPreview
@Composable
fun EnhancedPreviewExample() {
    EnhancedPreviewContainer(
        title = "Enhanced Preview Example",
        description = "Demonstrates the enhanced preview system capabilities"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Enhanced Preview System",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Interactive preview with controls",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@DevicePreview
@Composable
fun ResponsivePreviewExample() {
    ResponsivePreview {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Responsive Layout",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Preview Example",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
