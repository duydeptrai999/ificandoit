package com.xiaomi.base.ui.screens.camera

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.xiaomi.base.R
import com.xiaomi.base.ui.screens.camera.camera2.CameraTextureView
import com.xiaomi.base.ui.screens.camera.components.FilterPreviewItem
import com.xiaomi.base.ui.screens.camera.filter.FilterType

/**
 * Modern camera screen with real-time filters using Camera2 + OpenGL
 * Optimized for high performance and smooth user experience
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    // Camera state
    var isCameraReady by remember { mutableStateOf(false) }
    var cameraError by remember { mutableStateOf<String?>(null) }
    var currentFilter by remember { mutableStateOf(FilterType.ORIGINAL) }
    var showFilterPanel by remember { mutableStateOf(true) }
    
    // Camera texture view reference
    var cameraTextureView by remember { mutableStateOf<CameraTextureView?>(null) }
    
    // Available filters
    val availableFilters = remember {
        listOf(
            FilterType.ORIGINAL,
            FilterType.SEPIA,
            FilterType.BLACK_WHITE,
            FilterType.VINTAGE,
            FilterType.COOL,
            FilterType.WARM
        )
    }
    
    // Handle permission
    LaunchedEffect(cameraPermissionState.status) {
        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            cameraPermissionState.status.isGranted -> {
                // Camera preview with OpenGL rendering
                CameraPreviewContent(
                    isCameraReady = isCameraReady,
                    cameraError = cameraError,
                    currentFilter = currentFilter,
                    availableFilters = availableFilters,
                    showFilterPanel = showFilterPanel,
                    onCameraReady = { isCameraReady = true },
                    onCameraError = { error -> cameraError = error },
                    onFilterChanged = { filter -> 
                        currentFilter = filter
                        cameraTextureView?.applyFilter(filter)
                    },
                    onToggleFilterPanel = { showFilterPanel = !showFilterPanel },
                    onCameraTextureViewCreated = { view -> cameraTextureView = view },
                    lifecycleOwner = lifecycleOwner,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Top controls
                CameraTopControls(
                    onNavigateBack = onNavigateBack,
                    onToggleFilterPanel = { showFilterPanel = !showFilterPanel },
                    showFilterPanel = showFilterPanel,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .statusBarsPadding()
                )
                
                // Bottom controls
                CameraBottomControls(
                    isCameraReady = isCameraReady,
                    onCapturePhoto = {
                        cameraTextureView?.capturePhoto { photoData ->
                            // Handle captured photo
                        }
                    },
                    onSwitchCamera = {
                        cameraTextureView?.switchCamera()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                )
                
                // Filter panel
                if (showFilterPanel) {
                    FilterPanel(
                        availableFilters = availableFilters,
                        currentFilter = currentFilter,
                        onFilterSelected = { filter ->
                            currentFilter = filter
                            cameraTextureView?.applyFilter(filter)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(bottom = 120.dp)
                    )
                }
                
                // Error display
                cameraError?.let { error ->
                    ErrorDisplay(
                        error = error,
                        onDismiss = { cameraError = null },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            
            cameraPermissionState.status.shouldShowRationale -> {
                // Permission rationale
                PermissionRationale(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            else -> {
                // Permission denied
                PermissionDenied(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun CameraPreviewContent(
    isCameraReady: Boolean,
    cameraError: String?,
    currentFilter: FilterType,
    availableFilters: List<FilterType>,
    showFilterPanel: Boolean,
    onCameraReady: () -> Unit,
    onCameraError: (String) -> Unit,
    onFilterChanged: (FilterType) -> Unit,
    onToggleFilterPanel: () -> Unit,
    onCameraTextureViewCreated: (CameraTextureView) -> Unit,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            CameraTextureView(context).apply {
                // Set callbacks
                this.onCameraReady = { onCameraReady() }
                this.onCameraError = { error -> onCameraError(error) }
                this.onFilterChanged = { filter -> onFilterChanged(filter) }
                
                // Register lifecycle
                lifecycleOwner.lifecycle.addObserver(this)
                
                // Notify parent
                onCameraTextureViewCreated(this)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun CameraTopControls(
    onNavigateBack: () -> Unit,
    onToggleFilterPanel: () -> Unit,
    showFilterPanel: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Filter toggle button
        IconButton(
            onClick = onToggleFilterPanel,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = if (showFilterPanel) Icons.Default.FilterList else Icons.Default.FilterListOff,
                contentDescription = stringResource(R.string.toggle_filters),
                tint = Color.White
            )
        }
    }
}

@Composable
private fun CameraBottomControls(
    isCameraReady: Boolean,
    onCapturePhoto: () -> Unit,
    onSwitchCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gallery button (placeholder)
        IconButton(
            onClick = { /* TODO: Open gallery */ },
            modifier = Modifier
                .size(48.dp)
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = stringResource(R.string.gallery),
                tint = Color.White
            )
        }
        
        // Capture button
        Button(
            onClick = onCapturePhoto,
            enabled = isCameraReady,
            modifier = Modifier
                .size(72.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                disabledContainerColor = Color.Gray
            )
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = stringResource(R.string.capture_photo),
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }
        
        // Switch camera button
        IconButton(
            onClick = onSwitchCamera,
            enabled = isCameraReady,
            modifier = Modifier
                .size(48.dp)
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.FlipCameraAndroid,
                contentDescription = stringResource(R.string.switch_camera),
                tint = if (isCameraReady) Color.White else Color.Gray
            )
        }
    }
}

@Composable
private fun FilterPanel(
    availableFilters: List<FilterType>,
    currentFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(availableFilters) { filter ->
                    FilterPreviewItem(
                        filterType = filter,
                        isSelected = filter == currentFilter,
                        onClick = { onFilterSelected(filter) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorDisplay(
    error: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(R.string.camera_error),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    }
}

@Composable
private fun PermissionRationale(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.camera_permission_required),
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = stringResource(R.string.camera_permission_rationale),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onRequestPermission
            ) {
                Text(stringResource(R.string.grant_permission))
            }
        }
    }
}

@Composable
private fun PermissionDenied(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.camera_permission_denied),
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = stringResource(R.string.camera_permission_denied_message),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onRequestPermission
            ) {
                Text(stringResource(R.string.try_again))
            }
        }
    }
}