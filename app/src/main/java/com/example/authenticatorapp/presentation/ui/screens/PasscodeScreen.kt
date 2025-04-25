package com.example.authenticatorapp.presentation.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.utils.BiometricAuthManager
import com.example.authenticatorapp.presentation.viewmodel.PasscodeViewModel
import kotlinx.coroutines.delay

//TODO помилки ті ж самі, що на інших екранах, зроби ревʼю сама на базі тих правок, що я вже дала і відредагуй. Також екрани PremiumFeature, PrivacyPolicy, QRcodeScanner, SignIn, TermsOfUse я теж не переглядала 
@Composable
fun PasscodeScreen(
    navController: NavController,
    isCreatingMode: Boolean = false,
    isEditingMode: Boolean = false,
    action: String? = null,
    viewModel: PasscodeViewModel = hiltViewModel(),
    onPasscodeConfirmed: (String) -> Unit,
) {
    val context = LocalContext.current

    val activity = context as FragmentActivity
    val biometricAuthManager = remember { BiometricAuthManager(activity) }

    val isTouchIdEnabled by viewModel.isTouchIdEnabled.collectAsState()
    val isBiometricAvailable = biometricAuthManager.isBiometricAvailable()

    val showBiometricPrompt = action == "unlock" && isTouchIdEnabled && isBiometricAvailable

    val passcode by viewModel.passcode.collectAsState()
    val isConfirmStep by viewModel.isConfirmStep.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val headerTitle = when {
        isCreatingMode -> stringResource(R.string.create_passcode)
        isEditingMode -> stringResource(R.string.edit_passcode)
        else -> stringResource(R.string.confirm_current_passcode)
    }

    val instructionText = when {
        isCreatingMode && !isConfirmStep -> stringResource(R.string.enter_a_new_passcode)
        isCreatingMode && isConfirmStep -> stringResource(R.string.confirm_the_passcode)
        isEditingMode && !isConfirmStep -> stringResource(R.string.enter_the_old_passcode)
        isEditingMode && isConfirmStep -> stringResource(R.string.enter_a_new_passcode)
        else -> stringResource(R.string.enter_the_passcode)
    }

    LaunchedEffect(showBiometricPrompt) {
        if (showBiometricPrompt) {
            delay(300)
            biometricAuthManager.showBiometricPrompt(
                onSuccess = {
                    val savedCode = viewModel.getSavedPasscode()
                    onPasscodeConfirmed(savedCode)
                },
                onError = {},
                onCancel = {}
            )
        }
    }

    if (!isCreatingMode && !isEditingMode && action == "unlock") {
        BackHandler {
            (context as? Activity)?.finish()
        }
    }

    fun addDigit(d: String) {
        viewModel.addDigit(
            digit = d,
            isCreatingMode = isCreatingMode,
            isEditingMode = isEditingMode,
            onSuccess = { code, shouldClose ->
                onPasscodeConfirmed(code)
                if (shouldClose) {
                    navController.previousBackStackEntry?.savedStateHandle?.set("passcode_enabled", true)
                    navController.popBackStack()
                }
            },
            onError = { }
        )
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
                        if (showBiometricPrompt) {
                            IconButton(
                                onClick = {
                                    biometricAuthManager.showBiometricPrompt(
                                        onSuccess = {
                                            val savedCode = viewModel.getSavedPasscode()
                                            onPasscodeConfirmed(savedCode)
                                        },
                                        onError = {},
                                        onCancel = {}
                                    )
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
                                    painter = painterResource(id = R.drawable.finger_print),
                                    contentDescription = "Використати відбиток пальця",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.White
                                )
                            }
                        }
                        else {
                            Box(modifier = Modifier.size(72.dp)) { }
                        }

                        KeypadButton(number = "0") {
                            addDigit("0")
                        }

                        IconButton(
                            onClick = {
                                viewModel.removeLastDigit()
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
                                modifier = Modifier.size(32.dp),
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