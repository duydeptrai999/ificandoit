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
import com.xiaomi.base.R
import com.xiaomi.base.ui.screens.camera.components.PhotoCropView
import kotlinx.coroutines.launch

/**
 * Photo preview screen with edit options
 * Shows captured photo with editing tools
 */
@Composable
fun PhotoPreviewScreen(
    photoBitmap: Bitmap,
    onSavePhoto: () -> Unit,
    onDiscardPhoto: () -> Unit,
    onRetakePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var isSaving by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var selectedEditOption by remember { mutableStateOf("") }
    var currentBitmap by remember { mutableStateOf(photoBitmap) }
    var showCropView by remember { mutableStateOf(false) }
    
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
    
    // Sử dụng Column để layout theo chiều dọc
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top controls - Header với tiêu đề và action buttons
        PhotoEditTopBar(
            onBack = onDiscardPhoto,
            onUndo = { /* TODO: Implement undo */ },
            onRedo = { /* TODO: Implement redo */ },
            onSave = {
                scope.launch {
                    isSaving = true
                    try {
                        onSavePhoto()
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
        
        // Photo preview - chiếm toàn bộ không gian còn lại
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
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
        
        // Bottom edit options
        PhotoEditBottomBar(
            selectedOption = selectedEditOption,
            onOptionSelected = { option -> 
                selectedEditOption = option
                if (option == "Crop") {
                    showCropView = true
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
        EditOption("Cutout", Icons.Default.ContentCut)
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
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = stringResource(R.string.photo_saved),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.photo_saved_message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    )
}