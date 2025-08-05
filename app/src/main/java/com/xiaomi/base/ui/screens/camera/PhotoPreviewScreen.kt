package com.xiaomi.base.ui.screens.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xiaomi.base.R
import kotlinx.coroutines.launch

/**
 * Photo preview screen with save/discard options
 * Shows captured photo with applied filter
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
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Photo preview
        Image(
            bitmap = photoBitmap.asImageBitmap(),
            contentDescription = stringResource(R.string.captured_photo),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(0.dp)),
            contentScale = ContentScale.Fit
        )
        
        // Top controls
        PhotoPreviewTopControls(
            onDiscard = onDiscardPhoto,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
        )
        
        // Bottom controls
        PhotoPreviewBottomControls(
            isSaving = isSaving,
            onRetake = onRetakePhoto,
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
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        )
        
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
}

@Composable
private fun PhotoPreviewTopControls(
    onDiscard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Discard button
        IconButton(
            onClick = onDiscard,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.discard_photo),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Photo info
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.photo_preview),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun PhotoPreviewBottomControls(
    isSaving: Boolean,
    onRetake: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Retake button
        OutlinedButton(
            onClick = onRetake,
            enabled = !isSaving,
            modifier = Modifier
                .height(48.dp)
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color.White)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.retake),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Save button
        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier
                .height(48.dp)
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isSaving) stringResource(R.string.saving) else stringResource(R.string.save),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

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