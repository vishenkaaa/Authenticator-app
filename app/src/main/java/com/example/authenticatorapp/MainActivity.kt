package com.example.authenticatorapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.authenticatorapp.data.local.manager.PasscodeManager
import com.example.authenticatorapp.data.local.model.AccountEntity
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
import com.example.authenticatorapp.presentation.ui.theme.AuthenticatorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private var isAppLocked = false
    private var isInBackground = false
    private lateinit var navHostController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val currentLocale = Locale.getDefault().language
        if (currentLocale == "uk") {
            setAppLocale(this, "uk")
        } else {
            setAppLocale(this, "en")
        }

        window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                if (isInBackground) {
                    isInBackground = false
                    val passcodeManager = PasscodeManager(this@MainActivity)
                    if (passcodeManager.isPasscodeSet() && isAppLocked) {
                        checkLockAndNavigate()
                    }
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                isInBackground = true
                isAppLocked = true
            }
        })

        setContent {
            AuthenticatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    navHostController = navController

                    NavHost(navController = navController, startDestination = "Splash"){
                        composable("Splash"){
                            SplashScreen(navController, this@MainActivity)
                        }
                        composable("Onboarding") {
                            OnboardingScreen(navController)
                        }
                        composable("Main") {
                            MainScreen(navController, this@MainActivity)
                        }
                        composable("Paywall"){
                            PaywallScreen(navController, this@MainActivity)
                        }
                        composable("SignIn"){
                            SigninScreen(navController)
                        }
                        composable("Home"){
                            HomeScreen(navController, this@MainActivity, accounts = emptyList<AccountEntity>())
                        }
                        composable("QrScanner") {
                            QRcodeScreen(navController)
                        }
                        composable("AddAccount") {
                            AddAccountScreen(navController, this@MainActivity)
                        }
                        composable(
                            "EditAccount/{accountId}",
                            arguments = listOf(navArgument("accountId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val accountId = backStackEntry.arguments?.getInt("accountId")
                            accountId?.let {
                                AddAccountScreen(
                                    navController = navController,
                                    context = this@MainActivity,
                                    oldAccountId = accountId
                                )
                            }
                        }
                        composable("Info") {
                            InfoScreen(navController, this@MainActivity)
                        }
                        composable("AboutApp") {
                            AboutAppScreen(navController)
                        }
                        composable("Subscription") {
                            SubscriptionScreen(navController)
                        }
                        composable("PremiumFeatures") {
                            PremiumFeaturesScreen(navController, this@MainActivity)
                        }
                        composable("AppLock") {
                            AppLockScreen(navController, this@MainActivity)
                        }
                        composable("create_passcode") {
                            PasscodeScreen(
                                navController = navController,
                                isCreatingMode = true
                            ) { newPasscode -> }
                        }
                        composable("verify_passcode/{action}") { backStackEntry ->
                            val action = backStackEntry.arguments?.getString("action") ?: ""
                            PasscodeScreen(
                                navController = navController,
                                isCreatingMode = false,
                                action = action
                            ) { _ ->
                                val passcodeManager = PasscodeManager(this@MainActivity)

                                when (action) {
                                    "disable" -> {
                                        passcodeManager.savePasscode("")
                                        navController.previousBackStackEntry?.savedStateHandle?.set("passcode_enabled", false)
                                        navController.popBackStack()
                                    }
                                    "change" -> {
                                        navController.navigate("create_passcode") {
                                            popUpTo("verify_passcode/{action}") { inclusive = true }
                                        }
                                    }
                                    "unlock" -> {
                                        navController.navigate("Main") {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        }
                                    }
                                    else -> {
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                        composable("PrivacyPolicy") {
                            PrivacyPolicyScreen(navController)
                        }
                        composable("TermsOfUse") {
                            TermsOfUseScreen(navController)
                        }
                    }
                }
            }
        }
    }

    private fun checkLockAndNavigate() {
        if (isAppLocked) {
            isAppLocked = false
            navHostController.navigate("verify_passcode/unlock") {
               popUpTo(navHostController.graph.startDestinationId) { saveState = true }
            }
        }
    }
}

fun setAppLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration()
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
