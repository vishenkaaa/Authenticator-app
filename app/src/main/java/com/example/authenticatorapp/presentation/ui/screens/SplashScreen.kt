package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.preferences.OnBoardingPreferences
import com.example.authenticatorapp.presentation.ui.navigation.Main
import com.example.authenticatorapp.presentation.ui.navigation.Onboarding
import com.example.authenticatorapp.presentation.ui.navigation.VerifyPasscode
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import com.example.authenticatorapp.presentation.viewmodel.MainActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun SplashScreen(
    navController: NavController,
    context: Context,
    mainActivityViewModel: MainActivityViewModel = hiltViewModel()) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    val isLoading by homeViewModel.isLoadingAccounts.collectAsState()

    //FIXME винести цю логіку в viewModel
    //Done
    val isPasscodeEnabled by mainActivityViewModel.isPasscodeEnabled.collectAsState()

    val prefs = OnBoardingPreferences(context)

    val alpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            1f,
            animationSpec = tween(2500)
        )

        withTimeoutOrNull(3000) {
            while (isLoading) {
                delay(100)
            }
        }

        delay(500)

        if (prefs.isOnboardingShowed()) {
            navController.popBackStack()
            if (isPasscodeEnabled) {
                navController.navigate(VerifyPasscode("unlock")) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            } else {
                navController.navigate(Main) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        } else {
            navController.popBackStack()
            navController.navigate(Onboarding)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            //FIXME splash bg повинен бути лише фон, іконка повинна бути додана як окремe фото тут
            //Done
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.keyhole_shield),
            contentDescription = null,
            modifier = Modifier.padding(top = 207.dp).fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp, start = 32.dp, end = 32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
        ){
            Text(
                modifier = Modifier.alpha(alpha.value),
                text = stringResource(R.string.authenticator),
                style = AppTypography.titleLarge,
                color = White
            )
            Text(
                modifier = Modifier.alpha(alpha.value),
                text = stringResource(R.string.your_digital),
                style = AppTypography.labelLarge,
                color = White
            )
            Text(
                modifier = Modifier.alpha(alpha.value),
                text = stringResource(R.string.shield),
                style = AppTypography.titleLarge,
                color = White
            )
        }
    }
}