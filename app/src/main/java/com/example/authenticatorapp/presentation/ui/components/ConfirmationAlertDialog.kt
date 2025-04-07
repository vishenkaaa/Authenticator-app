package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue

@Composable
fun ConfirmationAlertDialog(title: String, content: String, textConfirmButton: String, textDismissButton:String, onConfirmButton: () -> Unit, onDismissButton: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismissButton() },
        title = {
            Text(
                text = title,
                style = AppTypography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = {
            Text(
                text = content,
                style = AppTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        confirmButton = {
            Text(
                text = textConfirmButton,
                color = MaterialTheme.colorScheme.error,
                style = AppTypography.labelMedium,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                        onConfirmButton()
                    }
            )
        },
        dismissButton = {
            Text(
                text = textDismissButton,
                color = Blue,
                style = AppTypography.labelMedium,
                modifier = Modifier.clickable {
                    onDismissButton()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp)
    )
}