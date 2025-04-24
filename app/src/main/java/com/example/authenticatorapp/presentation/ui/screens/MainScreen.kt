package com.example.authenticatorapp.presentation.ui.screens

//FIXME давай ми також максимально пофіксимо варнінги, щоб таких жовтих підкреслень не було як закінчиш редагувати після код рев'ю
//Done
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.model.TabScreen
import com.example.authenticatorapp.presentation.ui.components.AddModalBottomSheet
import com.example.authenticatorapp.presentation.ui.components.CustomBottomNavigation
import com.example.authenticatorapp.presentation.ui.navigation.QrScanner
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import com.example.authenticatorapp.presentation.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

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
            navController.navigate(QrScanner)
        }
    }

    fun openSheet() {
        coroutineScope.launch { sheetState.show() }
    }

    fun closeSheet() {
        coroutineScope.launch { sheetState.hide() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                TabScreen.HOME -> HomeScreen(navController, context, allAccounts, viewModel)
                TabScreen.INFO -> InfoScreen(navController, context)
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
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                FloatingActionButton(
                    onClick = { openSheet()
                        //FIXME показуй ботомшит використовуючи sheetState.show()  і тоді в invokeOnCompletion {
//                    //                                if (!sheetState.isVisible) {
//                    //                                    isMenuExpanded = true
//                    //                                }
//                    //                            }
//                    // І так само коли потрібно заховати його
                    },
                    containerColor = MainBlue,
                    shape = RoundedCornerShape(40.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    //FIXME offset
                    //не вийшло
                    //FIXME давай використаємо тут size краще
                    //Done
                    modifier = Modifier.size(66.dp).offset(y = (-6).dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "Додати",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (sheetState.isVisible) {
            AddModalBottomSheet(
                navController = navController,
                permissionLauncher = permissionLauncher,
                context = context,
                closeSheet = { closeSheet() }
            )
        }
    }
}

//TODO переробити таким чином, щоб можна було показати превʼю. Решту скрінів теж