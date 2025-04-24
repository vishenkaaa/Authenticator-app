package com.example.authenticatorapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.authenticatorapp.presentation.ui.screens.AboutAppScreen
import com.example.authenticatorapp.presentation.ui.screens.AddAccountScreen
import com.example.authenticatorapp.presentation.ui.screens.AppLockScreen
import com.example.authenticatorapp.presentation.ui.screens.HomeScreen
import com.example.authenticatorapp.presentation.ui.screens.InfoScreen
import com.example.authenticatorapp.presentation.ui.screens.MainScreen
import com.example.authenticatorapp.presentation.ui.screens.OnboardingScreen
import com.example.authenticatorapp.presentation.ui.screens.PasscodeScreen
import com.example.authenticatorapp.presentation.ui.screens.PaywallScreen
import com.example.authenticatorapp.presentation.ui.screens.PremiumFeaturesScreen
import com.example.authenticatorapp.presentation.ui.screens.PrivacyPolicyScreen
import com.example.authenticatorapp.presentation.ui.screens.QRcodeScreen
import com.example.authenticatorapp.presentation.ui.screens.SigninScreen
import com.example.authenticatorapp.presentation.ui.screens.SplashScreen
import com.example.authenticatorapp.presentation.ui.screens.SubscriptionScreen
import com.example.authenticatorapp.presentation.ui.screens.TermsOfUseScreen
import com.example.authenticatorapp.presentation.viewmodel.MainActivityViewModel
import com.example.authenticatorapp.presentation.viewmodel.SyncViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Any,
    mainActivity: FragmentActivity,
    mainActivityViewModel: MainActivityViewModel,
    syncViewModel: SyncViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<Splash> {
            SplashScreen(navController, mainActivity)
        }
        composable<Onboarding> {
            OnboardingScreen(navController)
        }
        composable<Main> {
            MainScreen(navController, mainActivity)
        }
        composable<Paywall> {
            PaywallScreen(navController, mainActivity)
        }
        composable<SignIn> {
            SigninScreen(navController, mainViewModel = mainActivityViewModel)
        }
        composable<Home> {
            HomeScreen(navController, mainActivity, accounts = emptyList())
        }
        composable<QrScanner> {
            QRcodeScreen(navController = navController, mainViewModel = mainActivityViewModel)
        }
        composable<AddAccount> {
            AddAccountScreen(navController, mainActivity)
        }
        composable<EditAccount> { backStackEntry ->
            val editAccount = backStackEntry.toRoute<EditAccount>()
            AddAccountScreen(
                navController = navController,
                context = mainActivity,
                oldAccountId = editAccount.accountId
            )
        }
        composable<Info> {
            InfoScreen(navController, mainActivity)
        }
        composable<AboutApp> {
            AboutAppScreen(navController)
        }
        composable<Subscription> {
            SubscriptionScreen(navController)
        }
        composable<PremiumFeatures> {
            PremiumFeaturesScreen(navController, mainActivity, syncViewModel)
        }
        composable<AppLock> {
            AppLockScreen(navController)
        }
        composable<CreatePasscode> {
            PasscodeScreen(navController = navController, isCreatingMode = true) {}
        }
        composable<VerifyPasscode> { backStackEntry ->
            val data = backStackEntry.toRoute<VerifyPasscode>()
            PasscodeScreen(
                navController = navController,
                isCreatingMode = false,
                action = data.action
            ) { _ ->
                when (data.action) {
                    "disable" -> {
                        mainActivityViewModel.clearPasscode()
                        navController.previousBackStackEntry?.savedStateHandle?.set("passcode_enabled", false)
                        navController.popBackStack()
                    }

                    "change" -> {
                        navController.navigate(CreatePasscode) {
                            popUpTo(VerifyPasscode(data.action)) { inclusive = true }
                        }
                    }

                    "unlock" -> {
                        navController.navigate(Main) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }

                    else -> navController.popBackStack()
                }
            }
        }
        composable<PrivacyPolicy> {
            PrivacyPolicyScreen(navController)
        }
        composable<TermsOfUse> {
            TermsOfUseScreen(navController)
        }
    }
}