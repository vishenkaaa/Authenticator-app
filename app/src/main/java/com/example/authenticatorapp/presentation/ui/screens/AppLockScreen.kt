package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.preferences.PasscodeManager
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.FragmentActivity
import com.example.authenticatorapp.presentation.utils.BiometricAuthManager

@Composable
fun AppLockScreen(navController: NavController, context: Context) {
    //FIXME не використовуємо напряму MaterialTheme.colorScheme замість colors
    val colors = MaterialTheme.colorScheme
    val passcodeManager = remember { PasscodeManager(context) }
    var isPasscode by remember { mutableStateOf(passcodeManager.isPasscodeSet()) }
    var isTouchID by remember { mutableStateOf(passcodeManager.isTouchIdEnabled()) }

    val fragmentActivity = context as? FragmentActivity
    val biometricManager = remember {
        if (fragmentActivity != null) BiometricAuthManager(fragmentActivity) else null
    }

    val isBiometricAvailable = biometricManager?.isBiometricAvailable() ?: false

    val passcodeResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("passcode_enabled")
        ?.observeAsState()

    // Реагуємо на результат з інших екранів
    LaunchedEffect(passcodeResult?.value) {
        passcodeResult?.value?.let { enabled ->
            isPasscode = enabled
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("passcode_enabled")
        }
    }

    Column(
        Modifier
            .background(colors.background)
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 52.dp, bottom = 24.dp),
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
                text = stringResource(R.string.app_lock),
                color = colors.onPrimary,
                style = AppTypography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = colors.onPrimaryContainer,
                    shape = RoundedCornerShape(24.dp)
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = colors.inverseSurface,
                    spotColor = colors.inverseSurface
                )
                .background(
                    color = colors.onPrimaryContainer,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {})
                        .padding(vertical = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.key),
                        contentDescription = null,
                        tint = Blue,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = stringResource(R.string.passcode),
                        modifier = Modifier.weight(1f),
                        style = AppTypography.bodyMedium
                    )
                    Switch(
                        checked = isPasscode,
                        onCheckedChange = { newValue ->
                            if (newValue) {
                                navController.navigate("create_passcode")
                            } else if (isPasscode) {
                                navController.navigate("verify_passcode/disable")
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Blue,
                            uncheckedThumbColor = Gray2,
                            uncheckedTrackColor = if (!isSystemInDarkTheme()) White else colors.background,
                            checkedBorderColor = MainBlue,
                            uncheckedBorderColor = Gray5,
                        ),
                        thumbContent = null,
                    )
                }

                if (isPasscode) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = if(!isSystemInDarkTheme()) Black.copy(alpha = 0.1f) else Gray2.copy(alpha = 0.1f)
                    )
                    Text(
                        text = stringResource(R.string.change_passcode),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp)
                            .clickable { navController.navigate("verify_passcode/change") },
                        style = AppTypography.bodyMedium,
                        color = if(isSystemInDarkTheme()) Blue else MainBlue,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (isPasscode)
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = colors.onPrimaryContainer,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = colors.inverseSurface,
                        spotColor = colors.inverseSurface
                    )
                    .background(
                        color = colors.onPrimaryContainer,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {})
                        .padding(vertical = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.finger_print),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                    )
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.touch_id),
                            style = AppTypography.bodyMedium
                        )
                        if (!isBiometricAvailable) {
                            Text(
                                text = stringResource(R.string.biometrics_are_unavailable_on_this_device),
                                style = AppTypography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                    Switch(
                        checked = isTouchID,
                        onCheckedChange = { newValue ->
                            isTouchID = newValue
                            passcodeManager.setTouchIdEnabled(newValue)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Blue,
                            uncheckedThumbColor = Gray2,
                            uncheckedTrackColor = if (!isSystemInDarkTheme()) White else colors.background,
                            checkedBorderColor = MainBlue,
                            uncheckedBorderColor = Gray5,
                        ),
                        thumbContent = null,
                    )
                }
            }
    }
}