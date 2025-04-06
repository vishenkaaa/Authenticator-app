package com.juraj.fluid

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.CustomBottomNavigation
import com.example.authenticatorapp.presentation.ui.screens.HomeScreen
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

enum class Screen { HOME, INFO }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, context: Context, viewModel: HomeViewModel = hiltViewModel()) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState()

    val allAccounts by viewModel.accounts.collectAsState(initial = emptyList())

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
                                painter = painterResource(R.drawable.ic_add),
                                contentDescription = "Додати",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
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
                    Screen.HOME -> HomeScreen(navController, context, viewModel, allAccounts)
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

        if(isMenuExpanded)
            ModalBottomSheet(
                onDismissRequest = { isMenuExpanded = false },
                sheetState = sheetState,
                containerColor = colors.background,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 60.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn {
                        item {
                            Button(
                                onClick = {isMenuExpanded = false
                                    navController.navigate("QrScanner")},
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
                        }

                        item {
                            Button(
                                onClick = {isMenuExpanded = false
                                    navController.navigate("AddAccount")},
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
                        }
                    }
                }
            }
    }
}

@Composable fun InfoScreen() { Text("Інформація", Modifier.fillMaxSize()) }