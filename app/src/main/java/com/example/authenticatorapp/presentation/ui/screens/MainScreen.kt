package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
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
//FIXME давай ми також максимально пофіксимо варнінги, щоб таких жовтих підкреслень не було як закінчиш редагувати після код рев'ю
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.model.AccountEntity
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
    val currentScreen by navigationViewModel.selectedTab.collectAsState()

    var isMenuExpanded by remember { mutableStateOf(false) }
    var colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState()

    val allAccounts by viewModel.accounts.collectAsState(initial = emptyList())

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
                //FIXME це не потрібно виносити у viewModel, це можна зберегти тут
                navigationViewModel.selectTab(selectedScreen)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    //FIXME offset
                    .offset(y = (-44).dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        //FIXME показуй ботомшит використовуючи sheetState.show()  і тоді в invokeOnCompletion {
                        //                                if (!sheetState.isVisible) {
                        //                                    isMenuExpanded = true
                        //                                }
                        //                            }
                        // І так само коли потрібно заховати його
                        isMenuExpanded = !isMenuExpanded 
                              },
                    backgroundColor = MainBlue,
                    //FIXME давай використаємо тут size краще
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

        //TODO винести ботомшит як окремий компонент
        if (isMenuExpanded)
            ModalBottomSheet(
                onDismissRequest = { isMenuExpanded = false },
                sheetState = sheetState,
                containerColor = colors.background,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        //FIXME в дизайні відступ 40
                        .padding(bottom = 60.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //TODO ask why lazy column
                    LazyColumn {
                        item {
                            Button(
                                onClick = {
                                    isMenuExpanded = false
                                    navController.navigate("QrScanner")
                                },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    //FIXME не використовуй статичний розмір. Якщо в користувача буде збільшений щрифт в системі, він не поміститься у кнопку, бо ти обмежила її висоту. Краще додай відступи для контенту
                                    .height(50.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = if (isSystemInDarkTheme()) colors.onPrimaryContainer
                                    else colors.background,
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
                        }

                        item {
                            Button(
                                onClick = {
                                    isMenuExpanded = false
                                    navController.navigate("AddAccount")
                                },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    // FIXME статичний розмір
                                    .height(50.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = colors.onPrimaryContainer,
                                    contentColor = MainBlue
                                ),
                                // FIXME давай замість того, щоб кожен раз перевіряти яка в нас зараз тема ми використаємо кольори з ColorScheme
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

//TODO переробити таким чином, щоб можна було показати превʼю. Решту скрінів теж