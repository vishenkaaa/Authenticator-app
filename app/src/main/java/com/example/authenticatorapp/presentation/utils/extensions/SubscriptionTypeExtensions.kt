package com.example.authenticatorapp.presentation.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.model.SubscriptionType

@Composable
fun SubscriptionType.toName(): String {
    return when (this) {
        SubscriptionType.WEEKLY -> stringResource(R.string.weekly)
        SubscriptionType.YEARLY -> stringResource(R.string.yearly)
    }
}

@Composable
fun SubscriptionType.toTitle(): String {
    return when (this) {
        SubscriptionType.WEEKLY -> stringResource(R.string._3_days_free_trial)
        SubscriptionType.YEARLY -> stringResource(R.string._1_year)
    }
}

@Composable
fun SubscriptionType.toDescription(): String {
    return when (this) {
        SubscriptionType.WEEKLY -> stringResource(R.string.than_6_99_usd_per_week)
        SubscriptionType.YEARLY -> stringResource(R.string._39_99_usd_only_0_83_per_week)
    }
}
