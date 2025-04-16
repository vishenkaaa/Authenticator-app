package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun BulletText(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("• ", color = MaterialTheme.colorScheme.onPrimary)
        Text(
            text = text,
            style = AppTypography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}