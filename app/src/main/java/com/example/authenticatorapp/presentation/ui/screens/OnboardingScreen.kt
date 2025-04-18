package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.LightBlue
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController){

    //TODO створити sealed class OnboardingPage та нащадків PageOne, PageTwo...
    // В companion object створити функцію getPageById, яка приймає int id і повертає 0->PageOne, 1->PageTwo...
    // Створити клас OnboardingPageData з полями title, description, image
    // Створити extension функцію, OnboardingPage.getData():OnboardingPageData, і використати when(this) і вернути обʼєкт з необхідними даними
    val titles = listOf(
        stringResource(R.string.safer_account),
        stringResource(R.string.simple_camera_code_setting),
        stringResource(R.string.enhanced_privacy),
        stringResource(R.string.unlock_full_access_to_all_the_features)
    )

    val descriptions = listOf(
        stringResource(R.string.securely_protect_your_data_from_intrusion_and_data_loss),
        stringResource(R.string.just_scan_the_qr_code_or_add_it_manually),
        stringResource(R.string.securely_conduct_crypto_transactions_and_keep_your_wallet_safe),
        stringResource(R.string._3_day_free_trial_than_6_99_usd_per_week)
    )

    val images = listOf(
        R.drawable.onboarding_bg1,
        R.drawable.onboarding_bg2,
        R.drawable.onboarding_bg3,
        R.drawable.onboarding_bg4
    )

    val pagerState = rememberPagerState()
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        //FIXME використати !pagerState.canScrollForward замість цієї умови
        if (currentPage == titles.size - 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",

                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp, top = 5.dp, bottom = 5.dp)
                        .clickable {
                            navController.popBackStack()
                            navController.navigate("SignIn")
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.restore),
                    color = White,
                    style = AppTypography.bodySmall,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            count = titles.size,
            modifier = Modifier.fillMaxSize()
        ) { currentPage ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    //TODO після всіх змін що я описала зверху ти зможеш зробити щось накшталт 
                    // OnboardingPage.getPageById(currentPage).getData().image 
                    painter = painterResource(id = images[currentPage]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, bottom = 162.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        //TODO OnboardingPage.getPageById(currentPage).getData().title
                        text = titles[currentPage],
                        textAlign = TextAlign.Center,
                        color = White,
                        style = AppTypography.titleMedium
                    )
                    Text(
                        //TODO OnboardingPage.getPageById(currentPage).getData().description
                        text = descriptions[currentPage],
                        Modifier.padding(16.dp),
                        color = Gray2,
                        textAlign = TextAlign.Center,
                        style = AppTypography.labelMedium,
                    )
                }
            }
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 91.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((pagerState.currentPage + 1) / titles.size.toFloat())
                        .height(4.dp)
                        .background(LightBlue, shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage != 3) {
                            val nextPage = pagerState.currentPage + 1
                            pagerState.animateScrollToPage(nextPage)
                        } else {
                            navController.popBackStack()
                            navController.navigate("Paywall")
                        }
                    }
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
        }
        //FIXME використати !pagerState.canScrollForward замість цієї умови
        if (currentPage == titles.size - 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
                    .align(Alignment.BottomCenter)
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
                    //FIXME винести це кудись, я не знаю, що таке u2022
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

