package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.MainActivity
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.preferences.OnBoardingPreferences
import com.example.authenticatorapp.presentation.ui.components.SubscriptionOption
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray1
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.LightBlue
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.viewmodel.SubscriptionViewModel

@Composable
fun PaywallScreen(navController: NavController, context: MainActivity, viewModel: SubscriptionViewModel = hiltViewModel()){
    val prefs = OnBoardingPreferences(context)
    var selectedPlan by remember { mutableStateOf("") }
    var freeTrialIsSelected by remember { mutableStateOf(false) }

    val oneYearText = stringResource(id = R.string._1_year)
    val freeTrialText = stringResource(id = R.string._3_days_free_trial)

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.paywall_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp)
                    .zIndex(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",

                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if(prefs.isFinished()) navController.popBackStack()
                            else navController.navigate("Main")
                            prefs.setFinished()
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                if(!prefs.isFinished())
                    Text(
                        text = stringResource(R.string.restore),
                        color = White,
                        style = AppTypography.bodySmall,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                            .clickable {
                                navController.popBackStack()
                                navController.navigate("Onboarding")
                            }
                    )
            }

            Spacer(modifier = Modifier.weight(1f))

            LaunchedEffect(Locale.current) {
                selectedPlan = oneYearText
            }

            Text(
                text = stringResource(R.string.pick_your_plan_and_get_benefits),
                style = AppTypography.titleSmall,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.subtitle_text_about_benefits),
                style = AppTypography.labelMedium,
                color = Gray2,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(White, shape = RoundedCornerShape(30.dp))
                    .border(
                        3.dp,
                        color = if (freeTrialIsSelected) MainBlue else Gray2,
                        RoundedCornerShape(30.dp)
                    )
                    .padding(start = 24.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.free_trial_enabled),
                    color = Color(0xFF2A313E),
                    fontWeight = FontWeight.W700,
                    fontSize = 14.sp
                )
                Switch(
                    checked = freeTrialIsSelected,
                    onCheckedChange = {
                        freeTrialIsSelected = it
                        if(freeTrialIsSelected) selectedPlan = freeTrialText
                        else selectedPlan = oneYearText },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF00CF00),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray,
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent,
                    ),
                    thumbContent = null,
                )
            }

            SubscriptionOption(
                title = oneYearText,
                description = stringResource(R.string._39_99_usd_only_0_83_per_week),
                isSelected = selectedPlan == oneYearText
            ) {
                selectedPlan = oneYearText
                freeTrialIsSelected = false
            }

            SubscriptionOption(
                title = freeTrialText,
                description = stringResource(R.string.than_6_99_usd_per_week),
                isSelected = selectedPlan == freeTrialText
            ) {
                selectedPlan = freeTrialText
                freeTrialIsSelected = true
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if(selectedPlan==oneYearText)
                        viewModel.saveSubscription(
                            plan = "Yearly",
                            hasFreeTrial = freeTrialIsSelected
                        )
                    else
                        viewModel.saveSubscription(
                            plan = "Weekly",
                            hasFreeTrial = freeTrialIsSelected
                        )

                    if(prefs.isFinished()) navController.popBackStack()
                    else navController.navigate("Main")
                    prefs.setFinished()
                },
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue
                )
            ) {
                Text(
                    text = stringResource(R.string.continue_),
                    color = MainBlue,
                    style = AppTypography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shield_tick),
                    contentDescription = "Shield tick",

                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp, top = 5.dp, bottom = 5.dp)
                )
                Text(
                    text = stringResource(R.string.just_0_83_per_week),
                    color = Gray1,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
                    .zIndex(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.terms_of_use),
                    color = White,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("TermsOfUse") }
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = "\u2022",
                    color = White,
                    style = AppTypography.labelMedium,
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                )
                Text(
                    text = stringResource(R.string.privacy_policy),
                    color = White,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("PrivacyPolicy") }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}
