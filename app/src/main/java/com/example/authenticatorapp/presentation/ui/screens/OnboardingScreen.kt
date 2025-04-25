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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
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
import com.example.authenticatorapp.presentation.model.OnboardingPage
import com.example.authenticatorapp.presentation.model.OnboardingPage.Companion.BULLET_SYMBOL
import com.example.authenticatorapp.presentation.ui.navigation.Main
import com.example.authenticatorapp.presentation.ui.navigation.Paywall
import com.example.authenticatorapp.presentation.ui.navigation.PrivacyPolicy
import com.example.authenticatorapp.presentation.ui.navigation.TermsOfUse
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.LightBlue
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.utils.extensions.getData
import com.example.authenticatorapp.presentation.viewmodel.OnboardingViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController,
                     onboardingViewModel: OnboardingViewModel = hiltViewModel()){

    //TODO створити sealed class OnboardingPage та нащадків PageOne, PageTwo...
    // В companion object створити функцію getPageById, яка приймає int id і повертає 0->PageOne, 1->PageTwo...
    // Створити клас OnboardingPageData з полями title, description, image
    // Створити extension функцію, OnboardingPage.getData():OnboardingPageData, і використати when(this) і вернути обʼєкт з необхідними даними
    //Done

    val pagerState = rememberPagerState()
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val coroutineScope = rememberCoroutineScope()

    val currentPageData = OnboardingPage.getPageById(currentPage).getData()
    val idLastPage = OnboardingPage.PageFour.id

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        //FIXME використати !pagerState.canScrollForward замість цієї умови
        //Неправильно працює так
        if (currentPage == idLastPage) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(5f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",

                    modifier = Modifier
                        .padding(end = 5.dp, top = 5.dp, bottom = 5.dp)
                        .clickable {
                            navController.navigate(Main)
                            onboardingViewModel.setOnboardingShowed()
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
            count = 4,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    //TODO після всіх змін що я описала зверху ти зможеш зробити щось накшталт 
                    // OnboardingPage.getPageById(currentPage).getData().image
                    //Done
                    painter = painterResource(id = R.drawable.onboarding_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if(currentPage == OnboardingPage.PageFour.id) {
                    Image(
                        painter = painterResource(id = R.drawable.onboarding_ic4_1),
                        contentDescription = null,
                        modifier = Modifier.padding(top = 110.dp).fillMaxWidth()
                    )
                }
                Image(
                    painter = painterResource(id = currentPageData.image),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 189.dp).fillMaxWidth().blueShadow()
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
                        //Done
                        text = currentPageData.title,
                        textAlign = TextAlign.Center,
                        color = White,
                        style = AppTypography.titleMedium
                    )
                    Text(
                        //TODO OnboardingPage.getPageById(currentPage).getData().description
                        //Done
                        text = currentPageData.description,
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
                    .background(White.copy(alpha = 0.2f), shape = CircleShape),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((pagerState.currentPage + 1) / (4).toFloat())
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
                            navController.navigate(Paywall)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue
                )
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.continue_),
                    color = MainBlue,
                    style = AppTypography.bodyMedium
                )
            }
        }
        //FIXME використати !pagerState.canScrollForward замість цієї умови
        //Не правильно працює так
        if (currentPage == idLastPage) {
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
                    modifier = Modifier.clickable { navController.navigate(TermsOfUse) }
                        .padding(vertical = 4.dp)
                )
                Text(
                    //FIXME винести це кудись, я не знаю, що таке u2022
                    //Done
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

fun Modifier.blueShadow() = this.then(
    Modifier.drawBehind {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MainBlue,
                    Color(0x0033B5E5)
                ),
                radius = size.minDimension,
                center = center
            ),
            radius = size.minDimension,
            center = center
        )
    }
)

