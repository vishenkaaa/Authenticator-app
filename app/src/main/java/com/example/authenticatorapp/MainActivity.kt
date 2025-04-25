package com.example.authenticatorapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.authenticatorapp.presentation.ui.navigation.AppNavGraph
import com.example.authenticatorapp.presentation.ui.navigation.Main
import com.example.authenticatorapp.presentation.ui.navigation.Splash
import com.example.authenticatorapp.presentation.ui.navigation.VerifyPasscode
import com.example.authenticatorapp.presentation.ui.theme.AuthenticatorAppTheme
import com.example.authenticatorapp.presentation.viewmodel.MainActivityViewModel
import com.example.authenticatorapp.presentation.viewmodel.SyncViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private var navHostController: NavController? = null
    val mainActivityViewModel: MainActivityViewModel by viewModels()
    private val syncViewModel: SyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupBackPressHandler()

        //FIXME цей код непотрібний взагалі. Система сама визначить, який файл з локалізацією обрати. Якщо в тебе системна мова українська і в тебе є файл values-uk/stings.xml, воно візьме текст звідти, якщо мова китайська, а в тебе немає файлу для цієї мови - візьме дефолтні значення. Файл, який знаходиться в папці values буде дефолтної мовою(в твоєму випадку це англійська). Цей підхід можна буде використати, якщо в тебе в застосунку є пікер мови в налаштуваннях і мова застосунку повиннна відрізнятись від мови системи
        //Done

        //TODO давай ми це винисемо в окрему функцію і назвемо щось типу disableScreenCapture. Це дасть більше контексту, що саме робить цей код
        //Done
        disableScreenCapture()

        //TODO було б непогано винести цю логіку в viewModel. А з активності вже використати isAppLocked і тоді вже в викликати
        //                      if (isAppLocked) {
        //                        checkLockAndNavigate()
        //                    }
        setupLifecycleObserver()

        //TODO Тут в навігації дуже багато літералів. Давай ми використаємо typesafe підхід, і сторимо класи. В документації це описано https://developer.android.com/guide/navigation/design/type-safety 
        //Done

        //FIXME Краще винести wasSplashShown в view model. Зараз, якщо я переверну телефон, активність перествориться і твоє значення wasSplashShown буде знову false, і сплеш покажеться, хоча так бути не повнно. Якщо ця змінна буде всередині viewModel, цього не станеться
        //Done
        setContent {
            AuthenticatorAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    navHostController = navController

                    val startDestination = when {
                        !mainActivityViewModel.wasSplashShown -> Splash
                        mainActivityViewModel.isPasscodeSet() && mainActivityViewModel.isAppLocked -> VerifyPasscode("unlock")
                        else -> Main
                    }

                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        mainActivity = this@MainActivity,
                        mainActivityViewModel = mainActivityViewModel,
                        syncViewModel = syncViewModel
                    )
                }
            }
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navHostController?.let { navController ->
                    val currentRoute = navController.currentBackStackEntry?.destination?.route

                    if (currentRoute == VerifyPasscode::class.qualifiedName && mainActivityViewModel.isAppLocked) {
                        return@let
                    }

                    if (currentRoute == Splash::class.qualifiedName ||
                        currentRoute == Main::class.qualifiedName
                    ) {
                        moveTaskToBack(true)
                    } else {
                        navController.popBackStack()
                    }
                }
            }
        })
    }

    private fun setupLifecycleObserver() {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                mainActivityViewModel.onAppBackgrounded()
            }

            override fun onResume(owner: LifecycleOwner) {
                mainActivityViewModel.onAppResumed()

                navHostController?.let { navController ->
                    if (navController.currentBackStackEntry?.destination?.route != null) {
                        mainActivityViewModel.markSplashShown()
                    }

                    checkLockAndNavigate()
                }
            }
        })
    }

    private fun checkLockAndNavigate() {
        if (mainActivityViewModel.isAppLocked) {
            navHostController?.let { navController ->
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute != VerifyPasscode::class.qualifiedName) {
                    navController.navigate(VerifyPasscode("unlock")) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    private fun disableScreenCapture() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}
