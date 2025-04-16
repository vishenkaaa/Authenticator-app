package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.BulletText
import com.example.authenticatorapp.presentation.ui.components.StyledParagraph
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier
            .background(colors.background)
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 50.dp, bottom = 32.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.privacy_policy),
                color = colors.onPrimary,
                style = AppTypography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.last_updated_april_2025),
                style = AppTypography.labelMedium,
                color = colors.onPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.your_privacy_is_very_important_to_us_the_otp_authenticator_app_does_not_collect_store_or_share_any_personal_user_data),
                style = AppTypography.labelMedium,
                color = colors.onPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.what_we_do_not_do),
                style = AppTypography.bodyLarge,
                color = colors.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            BulletText(stringResource(R.string.we_do_not_collect_personal_information_name_email_etc))
            BulletText(stringResource(R.string.we_do_not_share_your_data_with_third_parties))
            BulletText(stringResource(R.string.all_accounts_and_otp_codes_are_stored_locally_on_your_device))

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.security),
                style = AppTypography.bodyLarge,
                color = colors.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.we_use_encrypted_storage_to_protect_your_accounts_you_are_responsible_for_the_security_of_your_device),
                style = AppTypography.labelMedium,
                color = colors.onPrimary
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}