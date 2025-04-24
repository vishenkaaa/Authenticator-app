package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun ServiceItem(
    text: String,
    iconResId: Int,
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
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 16.dp).size(32.dp)
            )
            Text(
                text = text,
                style = AppTypography.labelMedium
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Black.copy(alpha = 0.1f)
        )
    }
}
