package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.screens.Screen
import com.example.authenticatorapp.presentation.ui.theme.MainBlue

@Composable
fun CustomBottomNavigation(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 20.dp)
            .paint(
                painter = painterResource(R.drawable.nav_bar),
                contentScale = ContentScale.FillWidth
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                //FIXME спробувати позиціонувати іншим варіантом
                .padding(horizontal = 40.dp)
                .offset(y = (14).dp)
        ) {
            IconButton(onClick = { onScreenSelected(Screen.HOME) }) {
                Icon(
                    if (currentScreen == Screen.HOME) painterResource(R.drawable.key_checked) else painterResource(
                        R.drawable.key),
                    contentDescription = "Головна",
                    tint = MainBlue

                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { onScreenSelected(Screen.INFO) }) {
                Icon(
                    if (currentScreen == Screen.INFO) painterResource(R.drawable.info_checked) else painterResource(
                        R.drawable.info),
                    contentDescription = "Інформація",
                    tint = MainBlue
                )
            }
        }
    }
}