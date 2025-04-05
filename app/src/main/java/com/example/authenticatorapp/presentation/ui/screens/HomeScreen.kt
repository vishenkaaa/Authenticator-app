package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.AccountItem
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray3
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavHostController, context: Context, viewModel: HomeViewModel = hiltViewModel()) {
    val accounts by viewModel.accounts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.authenticator),
            modifier = Modifier.padding(top = 52.dp, bottom = 32.dp),
            style = AppTypography.bodyLarge
        )

        if (accounts.isNotEmpty())
            LazyColumn {
                items(accounts) { account ->
                    val otp = remember { mutableStateOf(viewModel.generateOtp(account)) }
                    val remainingTime = remember { mutableStateOf(calculateRemainingTime()) }

                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(1000)
                            remainingTime.value = calculateRemainingTime()
                            if (remainingTime.value <= 1) {
                                otp.value = viewModel.generateOtp(account)
                            }
                        }
                    }

                    AccountItem(account, otp.value, remainingTime.value, context)
                }
            }
        else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = false,
                        spotColor = Gray5,
                        ambientColor = Gray3
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
                            navController.navigate("QrScanner")
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

private fun calculateRemainingTime(): Int {
    val periodSeconds = 30
    val currentTimeSeconds = System.currentTimeMillis() / 1000
    return periodSeconds - (currentTimeSeconds % periodSeconds).toInt()
}
