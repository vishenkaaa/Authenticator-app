package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun SimpleServiceItem(
    text: String,
    onClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = text,
                style = AppTypography.bodyMedium
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Black.copy(alpha = 0.1f)
        )
    }
}