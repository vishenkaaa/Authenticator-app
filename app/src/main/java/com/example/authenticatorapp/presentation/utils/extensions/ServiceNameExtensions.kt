package com.example.authenticatorapp.presentation.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.model.ServiceName
import com.example.authenticatorapp.data.model.ServiceName.AMAZON
import com.example.authenticatorapp.data.model.ServiceName.BANKING_AND_FINANCE
import com.example.authenticatorapp.data.model.ServiceName.DISCORD
import com.example.authenticatorapp.data.model.ServiceName.FACEBOOK
import com.example.authenticatorapp.data.model.ServiceName.GOOGLE
import com.example.authenticatorapp.data.model.ServiceName.INSTAGRAM
import com.example.authenticatorapp.data.model.ServiceName.LINKEDIN
import com.example.authenticatorapp.data.model.ServiceName.MAIL
import com.example.authenticatorapp.data.model.ServiceName.MICROSOFT
import com.example.authenticatorapp.data.model.ServiceName.NETFLIX
import com.example.authenticatorapp.data.model.ServiceName.PAYPAL
import com.example.authenticatorapp.data.model.ServiceName.REDDIT
import com.example.authenticatorapp.data.model.ServiceName.SOCIAL
import com.example.authenticatorapp.data.model.ServiceName.UNKNOWN
import com.example.authenticatorapp.data.model.ServiceName.WEBSITE

@Composable
fun ServiceName.toLocalizedStringRes(): String {
    return when (this) {
        FACEBOOK -> "Facebook"
        INSTAGRAM -> "Instagram"
        GOOGLE -> "Google"
        LINKEDIN -> "Linkedin"
        AMAZON -> "Amazon"
        PAYPAL -> "Paypal"
        MICROSOFT -> "Microsoft"
        DISCORD -> "Discord"
        REDDIT -> "Reddit"
        NETFLIX -> "Netflix"
        BANKING_AND_FINANCE -> stringResource(R.string.banking_and_finance)
        WEBSITE -> stringResource(R.string.website)
        MAIL -> stringResource(R.string.mail)
        SOCIAL -> stringResource(R.string.social)
        UNKNOWN -> stringResource(R.string.unknown)
    }
}
