package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.model.InfoItem
import com.example.authenticatorapp.presentation.ui.components.InfoBlock
import com.example.authenticatorapp.presentation.ui.navigation.AboutApp
import com.example.authenticatorapp.presentation.ui.navigation.AppLock
import com.example.authenticatorapp.presentation.ui.navigation.PremiumFeatures
import com.example.authenticatorapp.presentation.ui.navigation.PrivacyPolicy
import com.example.authenticatorapp.presentation.ui.navigation.Subscription
import com.example.authenticatorapp.presentation.ui.navigation.TermsOfUse
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.utils.sendFeedbackEmail

@Composable
fun InfoScreen(navController: NavController, context: Context){
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.authenticator),
            modifier = Modifier
                .padding(top = 52.dp)
                .fillMaxWidth(),
            style = AppTypography.bodyLarge,
            textAlign = TextAlign.Center
        )

        val supportItems = listOf(
            InfoItem(
                icon = R.drawable.ic_message,
                text = stringResource(R.string.contact_us),
                onClick = {
                    sendFeedbackEmail(context)
                }
            ),
            InfoItem(
                icon = R.drawable.ic_file,
                text = stringResource(R.string.privacy_policy),
                onClick = {
                    navController.navigate(PrivacyPolicy)
                }
            ),
            InfoItem(
                icon = R.drawable.ic_file,
                text = stringResource(R.string.terms_of_use),
                onClick = {
                    navController.navigate(TermsOfUse)
                }
            )
        )

        InfoBlock(
            headerText = stringResource(R.string.customer_support),
            items = supportItems
        )

        val usefulItems = listOf(
            InfoItem(
                icon = R.drawable.ic_shield,
                text = stringResource(R.string.about_authenticator_app),
                onClick = {
                    navController.navigate(AboutApp)
                }
            ),
            InfoItem(
                icon = R.drawable.lock,
                text = stringResource(R.string.app_lock),
                onClick = {
                    navController.navigate(AppLock)
                }
            ),
            InfoItem(
                icon = R.drawable.ic_calendar,
                text = stringResource(R.string.subscription),
                onClick = {
                    navController.navigate(Subscription)
                }
            ),
            InfoItem(
                icon = R.drawable.ic_star,
                text = stringResource(R.string.premium_features),
                onClick = {
                    navController.navigate(PremiumFeatures)
                }
            )
        )

        InfoBlock(
            headerText = stringResource(R.string.useful_information),
            items = usefulItems
        )
    }
}
//TODO давай створимо в presentation шарі пекедж models і винесимо туди моделі як ця
//Done
