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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily

@Composable
fun AboutAppScreen(navController: NavController) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier.background(colors.background).fillMaxSize()
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
                text = "About Authenticator app",
                color = colors.onPrimary,
                style = AppTypography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            "What is an\nAuthenticator app?",
            Modifier.padding(horizontal = 16.dp),
            style = AppTypography.titleSmall,
        )

        Spacer(modifier = Modifier.height(16.dp))
        StyledParagraph(
            startText = "",
            boldText = "Authenticator",
            endText = " is an app that generates secure two-factor authentication (2FA) codes for your accounts. When you set up your account with two-factor authentication (2FA), you will receive a secret key to enter into the Authenticator, usually the key is in a QR code form.",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        StyledParagraph(
            startText = "This establishes a secure connection  etween the ",
            boldText = "Authenticator",
            endText = " and your account.",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        StyledParagraph(
            startText = "Once this secure connection is established, the Authenticator will generate a 6-8 digit code that is required to access your account.",
            boldText = "",
            endText = "",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        StyledParagraph(
            startText = "Even, if someone knows your password they still need the 2FA code to access your account.",
            boldText = "",
            endText = "",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun StyledParagraph(
    startText: String,
    boldText: String,
    endText: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val annotatedString = buildAnnotatedString {

        append(startText)

        withStyle( style = SpanStyle(
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            fontFamily = interFontFamily,
            color = Gray6
        )
        ) {
            append(boldText)
        }

        append(endText)
    }

    Text(
        text = annotatedString,
        style = AppTypography.labelMedium,
        color = Gray6,
        modifier = modifier
    )
}