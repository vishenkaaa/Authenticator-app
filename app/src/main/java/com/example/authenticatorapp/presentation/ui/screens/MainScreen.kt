package com.example.authenticatorapp.presentation.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.CustomBottomNavigation
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import com.example.authenticatorapp.presentation.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

enum class Screen { HOME, INFO }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    context: Context,
    viewModel: HomeViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val allAccounts by viewModel.accounts.collectAsState(initial = emptyList())

    val currentScreen by navigationViewModel.selectedTab.collectAsState()
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    fun openSheet() {
        coroutineScope.launch {
            sheetState.show()
        }
    }

    fun closeSheet() {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraPermissionGranted = true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.camera_permission_is_required_to_scan_the_qr_code),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(cameraPermissionGranted) {
        if (cameraPermissionGranted) {
            navController.navigate("QrScanner")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                Screen.HOME -> HomeScreen(navController, context, viewModel, allAccounts)
                Screen.INFO -> InfoScreen(navController, context)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            CustomBottomNavigation(currentScreen) { selectedScreen ->
                navigationViewModel.selectTab(selectedScreen)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-44).dp)
            ) {
                FloatingActionButton(
                    onClick = { openSheet() },
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

        if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { closeSheet() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 60.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Button(
                        onClick = {
                            closeSheet()
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                navController.navigate("QrScanner")
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.background,
                            contentColor = MainBlue
                        ),
                        border = if (!isSystemInDarkTheme()) BorderStroke(
                            2.dp,
                            MainBlue
                        ) else BorderStroke(2.dp, Gray6)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qr_code),
                            contentDescription = "QR",
                            tint = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.scan_qr_code),
                            style = AppTypography.bodyMedium,
                            color = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                    }
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            closeSheet()
                            navController.navigate("AddAccount")
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.background,
                            contentColor = MainBlue
                        ),
                        border = if (!isSystemInDarkTheme()) BorderStroke(
                            2.dp,
                            MainBlue
                        ) else BorderStroke(2.dp, Gray6)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.edit),
                            contentDescription = "Edit",
                            tint = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.enter_code_manually),
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