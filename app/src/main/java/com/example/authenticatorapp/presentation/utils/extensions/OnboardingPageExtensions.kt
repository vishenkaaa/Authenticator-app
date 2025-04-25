package com.example.authenticatorapp.presentation.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.model.OnboardingPage
import com.example.authenticatorapp.presentation.model.OnboardingPageData

@Composable
fun OnboardingPage.getData(): OnboardingPageData {
    return when (this) {
        OnboardingPage.PageOne -> OnboardingPageData(
            title = stringResource(R.string.safer_account),
            description = stringResource(R.string.securely_protect_your_data_from_intrusion_and_data_loss),
            image = R.drawable.onboarding_ic1
        )
        OnboardingPage.PageTwo -> OnboardingPageData(
            title = stringResource(R.string.simple_camera_code_setting),
            description = stringResource(R.string.just_scan_the_qr_code_or_add_it_manually),
            image = R.drawable.onboarding_ic2
        )
        OnboardingPage.PageThree -> OnboardingPageData(
            title = stringResource(R.string.enhanced_privacy),
            description = stringResource(R.string.securely_conduct_crypto_transactions_and_keep_your_wallet_safe),
            image = R.drawable.onboarding_ic3
        )
        OnboardingPage.PageFour -> OnboardingPageData(
            title = stringResource(R.string.unlock_full_access_to_all_the_features),
            description = stringResource(R.string._3_day_free_trial_than_6_99_usd_per_week),
            image = R.drawable.onboarding_ic4
        )
    }
}
