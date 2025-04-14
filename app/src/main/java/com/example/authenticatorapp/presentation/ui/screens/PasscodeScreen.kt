package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.manager.BiometricAuthManager
import com.example.authenticatorapp.data.local.manager.PasscodeManager
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PasscodeScreen(
    navController: NavController,
    isCreatingMode: Boolean = false,
    isEditingMode: Boolean = false,
    action: String? = null,
    onPasscodeConfirmed: (String) -> Unit
) {
    val context = LocalContext.current
    val passcodeManager = remember { PasscodeManager(context) }

    val isTouchIdEnabled = remember { passcodeManager.isTouchIdEnabled() }
    val biometricManager = remember {
        if (context is FragmentActivity) BiometricAuthManager(context) else null
    }
    val isBiometricAvailable = remember { biometricManager?.isBiometricAvailable() ?: false }
    val showBiometricPrompt = !isCreatingMode && !isEditingMode && isTouchIdEnabled && isBiometricAvailable

    var passcode by remember { mutableStateOf("") }
    var confirmPasscode by remember { mutableStateOf("") }
    var isConfirmStep by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var isProcessing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val headerTitle = when {
        isCreatingMode -> stringResource(R.string.create_passcode)
        isEditingMode -> stringResource(R.string.edit_passcode)
        else -> stringResource(R.string.create_passcode)
    }

    val instructionText = when {
        isCreatingMode && !isConfirmStep -> stringResource(R.string.enter_a_new_passcode)
        isCreatingMode && isConfirmStep -> stringResource(R.string.confirm_the_passcode)
        isEditingMode && !isConfirmStep -> stringResource(R.string.enter_the_old_passcode)
        isEditingMode && isConfirmStep -> stringResource(R.string.enter_a_new_passcode)
        else -> stringResource(R.string.enter_the_passcode)
    }

    fun showBiometricAuth() {
        if (context is FragmentActivity) {
            biometricManager?.showBiometricPrompt(
                onSuccess = {
                    val savedPasscode = passcodeManager.getPasscode()
                    onPasscodeConfirmed(savedPasscode)
                },
                onError = { error -> },
                onCancel = {}
            )
        }
    }

    LaunchedEffect(showBiometricPrompt) {
        if (showBiometricPrompt) {
            delay(300)
            showBiometricAuth()
        }
    }

    // Перевірка паролю коли він досягає 4 символів
    fun checkPasscode(newPasscode: String) {
        if (newPasscode.length == 4) {
            isProcessing = true
            scope.launch {
                delay(500)
            if (isCreatingMode) {
                if (!isConfirmStep) {
                    isConfirmStep = true
                    confirmPasscode = newPasscode
                    passcode = ""
                } else {
                    if (newPasscode == confirmPasscode) {
                        passcodeManager.savePasscode(newPasscode)
                        onPasscodeConfirmed(newPasscode)
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "passcode_enabled",
                            true
                        )
                        navController.popBackStack()
                    } else {
                        errorMessage =
                            context.getString(R.string.the_codes_do_not_match_please_try_again)
                        passcode = ""
                        confirmPasscode = ""
                        isConfirmStep = false
                    }
                }
            } else if (isEditingMode) {
                if (!isConfirmStep) {
                    // Перевірка старого паролю
                    val savedCode = passcodeManager.getPasscode()
                    if (newPasscode == savedCode) {
                        isConfirmStep = true
                        passcode = ""
                    } else {
                        errorMessage = context.getString(R.string.invalid_passcode_please_try_again)
                        passcode = ""
                    }
                } else {
                    passcodeManager.savePasscode(newPasscode)
                    onPasscodeConfirmed(newPasscode)
                    navController.popBackStack()
                }
            } else {
                val savedCode = passcodeManager.getPasscode()
                if (newPasscode == savedCode) {
                    onPasscodeConfirmed(newPasscode)
                } else {
                    errorMessage = context.getString(R.string.invalid_passcode_please_try_again)
                    passcode = ""
                }
            }
        }
            isProcessing = false
        }
    }

    fun addDigit(digit: String) {
        if (passcode.length < 4) {
            val newPasscode = passcode + digit
            passcode = newPasscode
            errorMessage = ""
            checkPasscode(newPasscode)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MainBlue, Blue.copy(alpha = 0.7f))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (action != "unlock") {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 48.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = headerTitle,
                    color = Color.White,
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(180.dp))

        Text(
            text = instructionText,
            color = Color.White,
            style = AppTypography.titleSmall,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Індикатори паролю (крапки)
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            for (i in 0 until 4) {
                val isFilled = i < passcode.length
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = if (isFilled) Color.White else Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (isFilled) Color.White else Color.White.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                )
            }
        }

        // Повідомлення про помилку
        AnimatedVisibility(
            visible = errorMessage.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                style = AppTypography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Клавіатура
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 56.dp)
        ) {
            for (row in 0..3) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    if (row < 3) {
                        for (col in 0..2) {
                            val number = row * 3 + col + 1
                            KeypadButton(number = number.toString()) {
                                addDigit(number.toString())
                            }
                        }
                    } else {
                        // Останній рядок: порожньо (відбиток), 0, backspace
                        if (showBiometricPrompt && action == "unlock") {
                            IconButton(
                                onClick = { showBiometricAuth() },
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = Color.White.copy(alpha = 0.05f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.finger_print),
                                    contentDescription = "Використати відбиток пальця",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.White
                                )
                            }
                        }
                        else Box(modifier = Modifier.size(72.dp)) { }

                        KeypadButton(number = "0") {
                            addDigit("0")
                        }

                        IconButton(
                            onClick = {
                                if (passcode.isNotEmpty()) {
                                    passcode = passcode.dropLast(1)
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    color = Color.White.copy(alpha = 0.05f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.backspace),
                                contentDescription = "Backspace",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(number: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = MainBlue),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            style = AppTypography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}