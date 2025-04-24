package com.example.authenticatorapp.presentation.ui.screens

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.CustomTopAppBar
import com.example.authenticatorapp.presentation.ui.navigation.CreatePasscode
import com.example.authenticatorapp.presentation.ui.navigation.VerifyPasscode
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.utils.BiometricAuthManager
import com.example.authenticatorapp.presentation.viewmodel.AppLockViewModel

@Composable
fun AppLockScreen(
    navController: NavController,
    viewModel: AppLockViewModel = hiltViewModel()) {
    //FIXME не використовуємо напряму MaterialTheme.colorScheme замість colors
    //Done

    val context = LocalContext.current
    val activity = context as FragmentActivity
    val biometricAuthManager = remember { BiometricAuthManager(activity) }
    val isBiometricAvailable = biometricAuthManager.isBiometricAvailable()

    val isPasscode by viewModel.isPasscode
    val isTouchID by viewModel.isTouchID

    val passcodeResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("passcode_enabled")
        ?.observeAsState()

    // Реагуємо на результат з інших екранів
    LaunchedEffect(passcodeResult?.value) {
        passcodeResult?.value?.let { enabled ->
            viewModel.updatePasscodeEnabled(enabled)
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("passcode_enabled")
        }
    }

    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        CustomTopAppBar(navController, stringResource(R.string.app_lock))

        Box(
            Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(24.dp)
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = MaterialTheme.colorScheme.inverseSurface,
                    spotColor = MaterialTheme.colorScheme.inverseSurface
                )
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                                navController.navigate(CreatePasscode)
                            } else if (isPasscode) {
                                navController.navigate(VerifyPasscode("disable"))
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Blue,
                            uncheckedThumbColor = Gray2,
                            uncheckedTrackColor = if (!isSystemInDarkTheme()) White else MaterialTheme.colorScheme.background,
                            checkedBorderColor = MainBlue,
                            uncheckedBorderColor = Gray5,
                        ),
                        thumbContent = null,
                    )
                }

                if (isPasscode) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = if(!isSystemInDarkTheme()) Black.copy(alpha = 0.1f) else Gray2.copy(alpha = 0.1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(VerifyPasscode("change")) },
                    ){
                        Text(
                            text = stringResource(R.string.change_passcode),
                            style = AppTypography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (isPasscode)
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = MaterialTheme.colorScheme.inverseSurface,
                        spotColor = MaterialTheme.colorScheme.inverseSurface
                    )
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                            viewModel.updateTouchIDEnabled(newValue)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Blue,
                            uncheckedThumbColor = Gray2,
                            uncheckedTrackColor = if (!isSystemInDarkTheme()) White else MaterialTheme.colorScheme.background,
                            checkedBorderColor = MainBlue,
                            uncheckedBorderColor = Gray5,
                        ),
                        thumbContent = null,
                    )
                }
            }
    }
}