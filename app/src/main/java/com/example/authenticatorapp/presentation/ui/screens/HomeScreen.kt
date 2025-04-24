package com.example.authenticatorapp.presentation.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.presentation.model.AccountTab
import com.example.authenticatorapp.presentation.ui.components.CounterBasedTab
import com.example.authenticatorapp.presentation.ui.components.TabLayout
import com.example.authenticatorapp.presentation.ui.components.TimeBasedTab
import com.example.authenticatorapp.presentation.ui.navigation.QrScanner
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    context: Context,
    accounts: List<AccountEntity>,
    viewModel: HomeViewModel = hiltViewModel()
) {
    //FIXME не потрібно так присвоювати. Це не має сенсу.
    //Done

    val isLoading by viewModel.isLoadingAccounts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val selectedTab = AccountTab.fromPage(pagerState.currentPage)

    var cameraPermissionGranted by remember { mutableStateOf(false) }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.authenticator),
            modifier = Modifier.padding(top = 52.dp),
            style = AppTypography.bodyLarge
        )

        if (!isLoading && accounts.isNotEmpty())
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                textStyle = AppTypography.bodyMedium,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        style = AppTypography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.Unspecified
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = Gray5,
                    unfocusedLabelColor = Color.Gray,
                )
            )

        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CircularProgressIndicator()
            }
        } else
            if (accounts.isNotEmpty()) {
                //TODO неправильне будуєш лейаут
                // Розділи кожну вкладку на різні composable і подивись в документації або в статтях на медіумі, як правильно використовувати TabRow
                //Done
                TabLayout(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(AccountTab.toPage(tab))
                        }
                    }
                )

                HorizontalPager(
                    count = AccountTab.values.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (AccountTab.fromPage(page)) {
                        AccountTab.TimeBased -> TimeBasedTab(accounts, searchQuery, context, navController, viewModel)
                        AccountTab.CounterBased -> CounterBasedTab(accounts, searchQuery, context, navController, viewModel)
                    }
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .padding(horizontal = 16.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(24.dp),
                            clip = false,
                            spotColor = MaterialTheme.colorScheme.inverseSurface,
                            ambientColor = MaterialTheme.colorScheme.inverseSurface
                        )
                        .background(Color.Transparent, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = if (isSystemInDarkTheme()) painterResource(R.drawable.main_bg_dark)
                        else painterResource(R.drawable.main_bg_light),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.92f)
                    )

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(bottom = 13.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.add_2fa_codes),
                            modifier = Modifier.padding(bottom = 8.dp),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.W700,
                            fontSize = 24.sp
                        )

                        Text(
                            text = stringResource(R.string.keep_your_accounts_secure_by_adding_two_factor_authentication),
                            modifier = Modifier.padding(bottom = 32.dp),
                            textAlign = TextAlign.Center,
                            style = AppTypography.labelMedium
                        )

                        Button(
                            onClick = {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    navController.navigate(QrScanner)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .shadow(4.dp, shape = RoundedCornerShape(27.dp)),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainBlue
                            )
                        ) {
                            Image(
                                painter = painterResource(R.drawable.qr_code),
                                contentDescription = "Google",
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = stringResource(R.string.scan_qr_code),
                                color = White,
                                style = AppTypography.bodyLarge
                            )
                        }
                    }
                }
            }
    }
}

//TODO виносимо логіку в viewModel, додаємо в репозиторій метод для пошуку
//Done