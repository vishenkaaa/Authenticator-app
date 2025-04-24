package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun ConfirmationAlertDialog(
    title: String,
    content: String,
    textConfirmButton: String,
    textDismissButton: String,
    onConfirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissButton() },
        title = {
            Text(
                text = title,
                style = AppTypography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = content,
                style = AppTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmButton,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Red
                ),
            ) {
                Text(
                    text = textConfirmButton,
                    color = MaterialTheme.colorScheme.error,
                    style = AppTypography.labelMedium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissButton,
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = textDismissButton,
                    color = MaterialTheme.colorScheme.primary,
                    style = AppTypography.labelMedium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 6.dp
    )
}
