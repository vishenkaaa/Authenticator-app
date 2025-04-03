package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.authenticatorapp.MainActivity
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(navController: NavHostController, context: MainActivity) {

    val alpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            1f,
            animationSpec = tween(2500)
        )
        delay(2000)

        withContext(Dispatchers.Main) {
            if (onBoardingIsFinished(context = context)) {
                navController.popBackStack()
                navController.navigate("Signin")
            } else {
                navController.popBackStack()
                navController.navigate("Onboarding")
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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

private fun onBoardingIsFinished(context: MainActivity): Boolean{
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isFinished", false)
}