package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.model.SubscriptionType
import com.example.authenticatorapp.presentation.model.OnboardingPage.Companion.BULLET_SYMBOL
import com.example.authenticatorapp.presentation.ui.components.SubscriptionOption
import com.example.authenticatorapp.presentation.ui.navigation.Main
import com.example.authenticatorapp.presentation.ui.navigation.Onboarding
import com.example.authenticatorapp.presentation.ui.navigation.PrivacyPolicy
import com.example.authenticatorapp.presentation.ui.navigation.TermsOfUse
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.DarkBlue
import com.example.authenticatorapp.presentation.ui.theme.Gray1
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.Green
import com.example.authenticatorapp.presentation.ui.theme.LightBlue
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.utils.extensions.toDescription
import com.example.authenticatorapp.presentation.utils.extensions.toTitle
import com.example.authenticatorapp.presentation.viewmodel.OnboardingViewModel
import com.example.authenticatorapp.presentation.viewmodel.SubscriptionViewModel

@Composable
fun PaywallScreen(
    navController: NavController,
    context: Context,
    subscriptionViewModel: SubscriptionViewModel = hiltViewModel(),
    onboardingViewModel: OnboardingViewModel = hiltViewModel()){
    //TODO робота з преференсами лише в репозиторії, view model викликає метод з репозиторію, апдейтить стейт і ти в залежності від стейту відмальовуєш свій ui певним чином
    //Done
    val isOnboardingShowed by onboardingViewModel.isOnboardingShowed.collectAsState()

    //TODO зберігай це в стейті viewModel
    //Done
    val selectedPlan by subscriptionViewModel.selectedSubscriptionType.collectAsState()
    val freeTrialIsSelected by subscriptionViewModel.hasFreeTrial.collectAsState()

    //FIXME це теж можна буде винести як функцію/змінну розширення над SubscriptionType (описала в SubscriptionScreen)
    //Done

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        //FIXME paywall_bg не повинен містити в собі іконку замка, це повинен бути окремий компонент зображення
        //Done
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.keyhole_shield),
            contentDescription = null,
            modifier = Modifier.padding(top = 106.dp).fillMaxWidth()
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
                        .clickable {
                            if(isOnboardingShowed) navController.popBackStack()
                            else {navController.navigate(Main)
                            onboardingViewModel.setOnboardingShowed()}
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                if(!isOnboardingShowed)
                    Text(
                        text = stringResource(R.string.restore),
                        color = White,
                        style = AppTypography.bodySmall,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                            .clickable {
                                navController.popBackStack()
                                navController.navigate(Onboarding)
                            }
                    )
            }

            Spacer(modifier = Modifier.weight(1f))

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
                    //FIXME винести колір до решти кольорів
                    //Done
                    color = DarkBlue,
                    fontWeight = FontWeight.W700,
                    fontSize = 14.sp
                )
                Switch(
                    checked = freeTrialIsSelected,
                    onCheckedChange = { subscriptionViewModel.toggleFreeTrial(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = White,
                        //FIXME винести колір до решти кольорів
                        //Done
                        checkedTrackColor = Green,
                        uncheckedThumbColor = White,
                        uncheckedTrackColor = Color.LightGray,
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent,
                    ),
                    thumbContent = null,
                )
            }

            SubscriptionOption(
                title = SubscriptionType.YEARLY.toTitle(),
                //FIXME це теж можна буде винести як функцію/змінну розширення над SubscriptionType (описала в SubscriptionScreen)
                //Done
                description = SubscriptionType.YEARLY.toDescription(),
                isSelected = selectedPlan == SubscriptionType.YEARLY
            ) {
                subscriptionViewModel.onSubscriptionSelected(SubscriptionType.YEARLY)
            }

            SubscriptionOption(
                title = SubscriptionType.WEEKLY.toTitle(),
                //FIXME це теж можна буде винести як функцію/змінну розширення над SubscriptionType (описала в SubscriptionScreen)
                //Done
                description = SubscriptionType.WEEKLY.toDescription(),
                isSelected = selectedPlan == SubscriptionType.WEEKLY
            ) {
                subscriptionViewModel.onSubscriptionSelected(SubscriptionType.WEEKLY)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    //FIXME коли винисеш значення selectedPlan і freeTrialIsSelected в viewModel, saveSubscription не буде приймати ніяких параметрів і все це захендлиться всередині viewModel
                    //Done
                    subscriptionViewModel.saveSubscription()
                    if(isOnboardingShowed) navController.popBackStack()
                    else {navController.navigate(Main)
                    onboardingViewModel.setOnboardingShowed()}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue
                ),
                enabled = selectedPlan!=null
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
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
                    modifier = Modifier.clickable { navController.navigate(TermsOfUse) }
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = BULLET_SYMBOL,
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
                    modifier = Modifier.clickable { navController.navigate(PrivacyPolicy) }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}
