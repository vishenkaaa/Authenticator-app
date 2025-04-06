package com.example.authenticatorapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.presentation.ui.screens.AddAccountScreen
import com.example.authenticatorapp.presentation.ui.screens.HomeScreen
import com.example.authenticatorapp.presentation.ui.screens.InfoScreen
import com.example.authenticatorapp.presentation.ui.screens.MainScreen
import com.example.authenticatorapp.presentation.ui.screens.OnboardingScreen
import com.example.authenticatorapp.presentation.ui.screens.PaywallScreen
import com.example.authenticatorapp.presentation.ui.screens.QRcodeScreen
import com.example.authenticatorapp.presentation.ui.screens.SigninScreen
import com.example.authenticatorapp.presentation.ui.screens.SplashScreen
import com.example.authenticatorapp.presentation.ui.theme.AuthenticatorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("isFinished", false)
//        editor.apply()

        val currentLocale = Locale.getDefault().language
        if (currentLocale == "uk") {
            setAppLocale(this, "uk")
        } else {
            setAppLocale(this, "en")
        }

        window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContent {
            AuthenticatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()

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
                            QRcodeScreen(navController) { scannedData ->
                                navController.popBackStack()
                            }
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
                        composable("InfoScreen") {
                            InfoScreen(navController, this@MainActivity)
                        }
                    }
                }
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
