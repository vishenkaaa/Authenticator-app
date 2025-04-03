package com.juraj.fluid

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.screens.HomeScreen
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.AuthenticatorAppTheme
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue

enum class Screen { HOME, INFO }

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var colors = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter) {

        Scaffold(
            bottomBar = {
                Box{
                    CustomBottomNavigation(currentScreen) { selectedScreen ->
                        currentScreen = selectedScreen
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-44).dp)
                    ) {
                        FloatingActionButton(
                            onClick = { isMenuExpanded = !isMenuExpanded },
                            backgroundColor = MainBlue,
                            modifier = Modifier.scale(1.25f)
                        ) {
                            Icon(
                                imageVector =  Icons.Default.Add,
                                contentDescription = "Додати",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.HOME -> HomeScreen()
                    Screen.INFO -> InfoScreen()
                }
            }
        }

        if (isMenuExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black.copy(alpha = 0.3f))
                    .clickable { isMenuExpanded = false }
            )
        }

        AnimatedVisibility(
            visible = isMenuExpanded,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).wrapContentHeight(),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = colors.background,
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 29.dp)
                            .width(40.dp)
                            .height(5.dp)
                            .background(Gray5, RoundedCornerShape(2.dp))
                    )

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = colors.onPrimaryContainer,
                            contentColor = MainBlue
                        ),
                        border = if (!isSystemInDarkTheme()) BorderStroke(2.dp, MainBlue) else BorderStroke(2.dp, Gray6)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qr_code),
                            contentDescription = "QR",
                            tint = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scan QR-code",
                            style = AppTypography.bodyMedium,
                            color = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = colors.onPrimaryContainer,
                            contentColor = MainBlue
                        ),
                        border = if (!isSystemInDarkTheme()) BorderStroke(2.dp, MainBlue) else BorderStroke(2.dp, Gray6)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.edit),
                            contentDescription = "Edit",
                            tint = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enter code manually",
                            style = AppTypography.bodyMedium,
                            color = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                    }

                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun CustomBottomNavigation(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
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
                .padding(horizontal = 40.dp)
                .offset(y = (12).dp)
        ) {
            IconButton(onClick = { onScreenSelected(Screen.HOME) }) {
                Icon(
                    if (currentScreen == Screen.HOME) painterResource(R.drawable.key_checked) else painterResource(R.drawable.key),
                    contentDescription = "Головна",
                    tint = MainBlue
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { onScreenSelected(Screen.INFO) }) {
                Icon(
                    if (currentScreen == Screen.INFO) painterResource(R.drawable.info_checked) else painterResource(R.drawable.info),
                    contentDescription = "Інформація",
                    tint = MainBlue
                )
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Preview
@Composable
private fun preview() {
    AuthenticatorAppTheme {
        MainScreen()
    }
}

@Composable fun InfoScreen() { Text("Інформація", Modifier.fillMaxSize()) }