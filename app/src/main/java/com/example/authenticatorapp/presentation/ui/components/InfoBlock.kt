package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.presentation.model.InfoItem
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray2

@Composable
fun InfoBlock(
    headerText: String,
    items: List<InfoItem>,
) {
    //FIXME не потрібно присвоювати змінну
    //Done
    Column {
        Text(
            text = headerText,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
            style = AppTypography.bodyLarge
        )

        Box(
            Modifier
                //FIXME двіччі використовуєш background
                //Done
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = MaterialTheme.colorScheme.inverseSurface,
                    spotColor = MaterialTheme.colorScheme.inverseSurface
                )
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    InfoItemRow(
                        icon = item.icon,
                        text = item.text,
                        onClick = item.onClick
                    )

                    if (index < items.size - 1) {
                        //FIXME deprecated
                        //Done
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = if(!isSystemInDarkTheme()) Black.copy(alpha = 0.1f) else Gray2.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}