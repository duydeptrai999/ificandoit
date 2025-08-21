package com.xiaomi.base.ui.screens.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.util.Log
import com.xiaomi.base.R
import com.xiaomi.base.ui.screens.camera.components.PhotoCropView
import com.xiaomi.base.ui.screens.camera.components.PhotoAdjustView
import com.xiaomi.base.ui.screens.camera.components.AdjustmentValues
import com.xiaomi.base.ui.screens.camera.components.PhotoColorAdjustView
import com.xiaomi.base.ui.screens.camera.components.ColorAdjustmentValues
import com.xiaomi.base.ui.screens.camera.components.FilterPreviewItem
import com.xiaomi.base.ui.screens.camera.utils.ColorAdjustmentUtils
import com.xiaomi.base.ui.screens.camera.filter.FilterType
import com.xiaomi.base.ui.screens.camera.filter.FilterManager
import com.xiaomi.base.ui.screens.camera.utils.PhotoUtils
import com.xiaomi.base.ui.screens.camera.effect.EffectSelectionPanel
import com.xiaomi.base.ui.screens.camera.effect.EffectType
import com.xiaomi.base.ui.screens.camera.effect.EffectManager
import kotlinx.coroutines.launch

/**
 * Photo preview screen with edit options
 * Shows captured photo with editing tools and filter selection
 */
@Composable
fun PhotoPreviewScreen(
    rawPhotoBitmap: Bitmap, // Raw bitmap without any filter applied
    initialFilter: FilterType = FilterType.ORIGINAL,
    onSavePhoto: (Bitmap) -> Unit, // Pass the final processed bitmap
    onDiscardPhoto: () -> Unit,
    onRetakePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isSaving by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var selectedEditOption by remember { mutableStateOf("") }

    // Photo editing state
    data class PhotoEditState(
        val filter: FilterType = FilterType.ORIGINAL,
        val adjustmentValues: AdjustmentValues = AdjustmentValues(),
        val hasAdjustments: Boolean = false,
        val effect: EffectType = EffectType.ORIGINAL,
        val colorAdjustmentValues: ColorAdjustmentValues = ColorAdjustmentValues(),
        val hasColorAdjustments: Boolean = false
    )

    var editState by remember { mutableStateOf(PhotoEditState(initialFilter)) }
    var currentBitmap by remember { mutableStateOf(rawPhotoBitmap) }
    var showFilterPanel by remember { mutableStateOf(false) }
    var showAdjustView by remember { mutableStateOf(false) }
    var showEffectPanel by remember { mutableStateOf(false) }
    var showColorAdjustView by remember { mutableStateOf(false) }

    // Filter manager
    val filterManager = remember { FilterManager() }
    val availableFilters = remember {
        listOf(
            FilterType.ORIGINAL,
            FilterType.SEPIA,
            FilterType.BLACK_WHITE,
            FilterType.VINTAGE,
            FilterType.COOL,
            FilterType.WARM,
            FilterType.PINK_DREAM,
            FilterType.RETRO_80S,
            FilterType.OLD_FILM,
            FilterType.SPRING,
            FilterType.SUMMER,
            FilterType.AUTUMN,
            FilterType.WINTER,
            FilterType.NEON_NIGHTS,
            FilterType.GOLDEN_HOUR,
            FilterType.CYBERPUNK,
            FilterType.CHERRY_BLOSSOM
        )
    }

    // Handle back gesture - đóng Color Adjust View trước, nếu không có thì show discard dialog
    BackHandler {
        when {
            showColorAdjustView -> {
                showColorAdjustView = false
                selectedEditOption = ""
            }
            else -> showDiscardDialog = true
        }
    }

    var showCropView by remember { mutableStateOf(false) }

    // Apply filter, adjustments, color adjustments and effects when edit state changes
    LaunchedEffect(editState) {
        scope.launch {
            try {
                var processedBitmap = rawPhotoBitmap

                // Step 1: Apply filter if not original
                if (editState.filter != FilterType.ORIGINAL) {
                    processedBitmap = PhotoUtils.applyFilterToBitmap(processedBitmap, editState.filter) ?: processedBitmap
                }

                // Step 2: Apply adjustments if any
                if (editState.hasAdjustments) {
                    processedBitmap = PhotoUtils.applyAdjustments(processedBitmap, editState.adjustmentValues) ?: processedBitmap
                }

                // Step 3: Apply color adjustments if any
                if (editState.hasColorAdjustments) {
                    processedBitmap = ColorAdjustmentUtils.applyColorAdjustments(processedBitmap, editState.colorAdjustmentValues) ?: processedBitmap
                }

                // Step 4: Apply effect if not original
                if (editState.effect != EffectType.ORIGINAL) {
                    val effectManager = EffectManager()
                    processedBitmap = effectManager.applyEffect(processedBitmap, editState.effect, 1.0f) ?: processedBitmap
                }

                currentBitmap = processedBitmap
            } catch (e: Exception) {
                Log.e("PhotoPreview", "Error applying edits", e)
                currentBitmap = rawPhotoBitmap
            }
        }
    }

    // Show crop view when crop option is selected
    if (showCropView) {
        PhotoCropView(
            bitmap = currentBitmap,
            onCropConfirmed = { croppedBitmap ->
                currentBitmap = croppedBitmap
                showCropView = false
                selectedEditOption = ""
            },
            onCropCancelled = {
                showCropView = false
                selectedEditOption = ""
            },
            modifier = modifier.fillMaxSize()
        )
        return
    }

    // Remove the separate adjust view block - we'll integrate it into the main layout

    // Sử dụng Column để layout theo chiều dọc
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top controls - Header với tiêu đề và action buttons
        PhotoEditTopBar(
            onBack = { showDiscardDialog = true },
            onUndo = { /* TODO: Implement undo */ },
            onRedo = { /* TODO: Implement redo */ },
            onSave = {
                scope.launch {
                    isSaving = true
                    try {
                        onSavePhoto(currentBitmap) // Pass the processed bitmap
                        showSaveDialog = true
                    } finally {
                        isSaving = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        // Photo preview - chiếm 3/4 màn hình khi adjust view hiển thị, toàn bộ khi không
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (showAdjustView) 3f else 1f)
                .background(Color.Black)
        ) {
            // Main photo
            Image(
                bitmap = currentBitmap.asImageBitmap(),
                contentDescription = "Edit photo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            // Loading indicator when saving
            if (isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        // Photo Adjust View - chiếm 1/4 màn hình khi hiển thị
        if (showAdjustView) {

            PhotoAdjustView(
            originalBitmap = rawPhotoBitmap, // Sử dụng rawPhotoBitmap để đảm bảo tính nhất quán
            initialAdjustmentValues = editState.adjustmentValues,
            onAdjustmentApplied = { adjustmentValues ->
                    editState = editState.copy(
                        adjustmentValues = adjustmentValues,
                        hasAdjustments = true
                    )
                    showAdjustView = false
                    selectedEditOption = ""
                },
                onCancel = {
                    showAdjustView = false
                    selectedEditOption = ""
                },
                onPreviewUpdate = { adjustmentValues ->
                    // Cập nhật state để trigger LaunchedEffect
                    editState = editState.copy(
                        adjustmentValues = adjustmentValues,
                        hasAdjustments = adjustmentValues != AdjustmentValues()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
            )
        }

        // Filter panel - hiển thị phía trên bottom bar với chiều cao cố định
        if (showFilterPanel) {
            FilterSelectionPanel(
                    availableFilters = availableFilters,
                    currentFilter = editState.filter,
                    onFilterSelected = { filter ->
                        editState = editState.copy(filter = filter)
                    },
                    onDismiss = { showFilterPanel = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Black)
            )
        }

        // Effect panel - hiển thị phía trên bottom bar với chiều cao cố định
        if (showEffectPanel) {
            EffectSelectionPanel(
                selectedEffect = editState.effect,
                onEffectSelected = { effect ->
                    editState = editState.copy(effect = effect)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Black)
            )
        }

        // Color Adjust View - chiếm 1/4 màn hình khi hiển thị
        if (showColorAdjustView) {
            PhotoColorAdjustView(
                originalBitmap = rawPhotoBitmap,
                initialColorValues = editState.colorAdjustmentValues,
                onColorAdjustmentApplied = { colorAdjustmentValues ->
                    editState = editState.copy(
                        colorAdjustmentValues = colorAdjustmentValues,
                        hasColorAdjustments = colorAdjustmentValues.hasAnyAdjustments()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
            )
        }

        // Bottom edit options
        PhotoEditBottomBar(
            selectedOption = selectedEditOption,
            onOptionSelected = { option ->
                // Nếu nhấn vào option đã được chọn, thì tắt UI đó đi
                if (selectedEditOption == option) {
                    selectedEditOption = ""
                    showFilterPanel = false
                    showAdjustView = false
                    showCropView = false
                    showEffectPanel = false
                    showColorAdjustView = false
                } else {
                    selectedEditOption = option
                    when (option) {
                        "Crop" -> {
                            showCropView = true
                            showFilterPanel = false
                            showAdjustView = false
                            showEffectPanel = false
                            showColorAdjustView = false
                        }
                        "Filter" -> {
                            showFilterPanel = true
                            showAdjustView = false
                            showCropView = false
                            showEffectPanel = false
                            showColorAdjustView = false
                        }
                        "Adjust" -> {
                            showAdjustView = true
                            showFilterPanel = false
                            showCropView = false
                            showEffectPanel = false
                            showColorAdjustView = false
                        }
                        "Effect" -> {
                            showEffectPanel = true
                            showFilterPanel = false
                            showAdjustView = false
                            showCropView = false
                            showColorAdjustView = false
                        }
                        "Color" -> {
                            showColorAdjustView = true
                            showFilterPanel = false
                            showAdjustView = false
                            showCropView = false
                            showEffectPanel = false
                        }
                        else -> {
                            // Handle other edit options
                            showFilterPanel = false
                            showAdjustView = false
                            showCropView = false
                            showEffectPanel = false
                            showColorAdjustView = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        )
    }

    // Save success dialog
    if (showSaveDialog) {
        SaveSuccessDialog(
            onDismiss = {
                showSaveDialog = false
                onDiscardPhoto()
            }
        )
    }

    // Discard confirmation dialog
    if (showDiscardDialog) {
        DiscardChangesDialog(
            onConfirm = {
                showDiscardDialog = false
                onDiscardPhoto()
            },
            onDismiss = {
                showDiscardDialog = false
            }
        )
    }
}

@Composable
private fun PhotoEditTopBar(
    onBack: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.9f),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title
            Text(
                text = "Edit Photo",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onUndo,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(
                    onClick = onRedo,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Redo,
                        contentDescription = "Redo",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(
                    onClick = onSave,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoEditBottomBar(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val editOptions = listOf(
        EditOption("Crop", Icons.Default.Crop),
        EditOption("Filter", Icons.Default.PhotoFilter),
        EditOption("Adjust", Icons.Default.Tune),
        EditOption("Effect", Icons.Default.AutoAwesome),
        EditOption("Color", Icons.Default.Palette)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.9f),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            editOptions.forEach { option ->
                EditOptionItem(
                    option = option,
                    isSelected = selectedOption == option.name,
                    onClick = { onOptionSelected(option.name) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EditOptionItem(
    option: EditOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    Color.Transparent
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = option.name,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = option.name,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.9f),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

data class EditOption(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun SaveSuccessDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.photo_saved),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.photo_saved_gallery),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun FilterSelectionPanel(
    availableFilters: List<FilterType>,
    currentFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        color = Color.Black.copy(alpha = 0.9f),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close filters",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter list
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(availableFilters) { filter ->
                    FilterPreviewItem(
                        filterType = filter,
                        isSelected = filter == currentFilter,
                        onClick = {
                            onFilterSelected(filter)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscardChangesDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.discard_changes),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.discard_changes_message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.discard),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
