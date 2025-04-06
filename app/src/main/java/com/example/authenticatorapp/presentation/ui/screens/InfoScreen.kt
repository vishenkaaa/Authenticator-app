package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray4
import com.example.authenticatorapp.presentation.ui.theme.Gray5

@Composable
fun InfoScreen(navController: NavController, context: Context){
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier
            .background(colors.background)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.authenticator),
            modifier = Modifier
                .padding(top = 52.dp)
                .fillMaxWidth(),
            style = AppTypography.bodyLarge,
            textAlign = TextAlign.Center
        )

        val supportItems = listOf(
            InfoItem(
                icon = R.drawable.ic_message,
                text = stringResource(R.string.contact_us),
                onClick = { }
            ),
            InfoItem(
                icon = R.drawable.ic_file,
                text = stringResource(R.string.privacy_policy),
                onClick = { }
            ),
            InfoItem(
                icon = R.drawable.ic_file,
                text = stringResource(R.string.terms_of_use),
                onClick = { }
            )
        )

        InfoBlock(
            headerText = stringResource(R.string.customer_support),
            items = supportItems
        )

        val usefulItems = listOf(
            InfoItem(
                icon = R.drawable.ic_shield,
                text = stringResource(R.string.about_authenticator_app),
                onClick = {
                    navController.navigate("AboutApp")
                }
            ),
            InfoItem(
                icon = R.drawable.ic_calendar,
                text = stringResource(R.string.subscription),
                onClick = { }
            ),
            InfoItem(
                icon = R.drawable.ic_star,
                text = stringResource(R.string.premium_features),
                onClick = { }
            )
        )

        InfoBlock(
            headerText = stringResource(R.string.useful_information),
            items = usefulItems
        )
    }
}
@Composable
fun InfoBlock(
    headerText: String,
    items: List<InfoItem>,
) {
    val colors = MaterialTheme.colorScheme
    Column {
        Text(
            text = headerText,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
            style = AppTypography.bodyLarge
        )

        Box(
            Modifier
                .background(
                    color = colors.onPrimaryContainer,
                    shape = RoundedCornerShape(24.dp)
                )
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Gray4,
                    spotColor = Gray4
                )
                .background(
                    color = colors.onPrimaryContainer,
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
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Black.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

data class InfoItem(
    val icon: Int,
    val text: String,
    val onClick: () -> Unit
)

@Composable
private fun InfoItemRow(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(24.dp)
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = AppTypography.bodyMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(24.dp)
        )
    }
}