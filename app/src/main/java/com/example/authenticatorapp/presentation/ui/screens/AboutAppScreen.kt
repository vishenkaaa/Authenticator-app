package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.CustomTopAppBar
import com.example.authenticatorapp.presentation.ui.components.StyledParagraph
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun AboutAppScreen(navController: NavController) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        //TODO давай ми винесемо цю логіку тулбару в окремий компонент щоб ми могли його перевикористати пізніше або заюзаємо вже готовий Toolbar чи AppBar composable
        //Done
        CustomTopAppBar(navController, stringResource(R.string.about_authenticator_app))

        Text(
            stringResource(R.string.what_is_an_authenticator_app),
            Modifier.padding(horizontal = 16.dp),
            style = AppTypography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
        //TODO(optional) почитай про buildAnnotatedString і спробуй замінити
        StyledParagraph(
            startText = "",
            boldText = stringResource(R.string.authenticator),
            endText = stringResource(R.string.is_an_app_that_generates_secure_two_factor_authentication_2fa_codes_for_your_accounts_when_you_set_up_your_account_with_two_factor_authentication_2fa_you_will_receive_a_secret_key_to_enter_into_the_authenticator_usually_the_key_is_in_a_qr_code_form),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        StyledParagraph(
            startText = stringResource(R.string.this_establishes_a_secure_connection_etween_the),
            boldText = stringResource(R.string.authenticator),
            endText = stringResource(R.string.and_your_account),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        StyledParagraph(
            startText = stringResource(R.string.once_this_secure_connection_is_established_the_authenticator_will_generate_a_6_digit),
            boldText = "",
            endText = "",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        StyledParagraph(
            startText = stringResource(R.string.even_if_someone_knows_your_password_they_still_need_the_2fa_code_to_access_your_account),
            boldText = "",
            endText = "",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
