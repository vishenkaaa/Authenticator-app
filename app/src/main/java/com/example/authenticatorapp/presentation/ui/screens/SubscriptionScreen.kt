package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.ChoosePlanBox
import com.example.authenticatorapp.presentation.ui.components.ConfirmationAlertDialog
import com.example.authenticatorapp.presentation.ui.components.SignInBox
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.viewmodel.SubscriptionViewModel

@Composable
fun SubscriptionScreen(navController: NavController, viewModel: SubscriptionViewModel = hiltViewModel()) {
    val colors = MaterialTheme.colorScheme
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSubscription()
    }

    val plan by viewModel.plan.collectAsState()
    val billing by viewModel.nextBilling.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

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
                text = stringResource(R.string.subscription),
                color = colors.onPrimary,
                style = AppTypography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        if(!isAuthenticated){
            SignInBox(navController)
        }
        else if (plan != null) {
            Column {
                Box(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
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
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = stringResource(R.string.your_current_plan_is_a_subscription, plan ?: ""),
                            Modifier.padding(bottom = 8.dp),
                            style = AppTypography.bodyLarge
                        )
                        Text(
                            text = stringResource(R.string.next_billing_date, billing ?: ""),
                            style = AppTypography.labelMedium,
                            color = Gray5
                        )
                    }
                }

                Button(
                    onClick = { navController.navigate("Paywall") },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = colors.background,
                        contentColor = MainBlue
                    ),
                    border = if (!isSystemInDarkTheme()) BorderStroke(2.dp, MainBlue) else BorderStroke(
                        2.dp,
                        Gray6
                    )
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.change_plan),
                        style = AppTypography.bodyMedium,
                        color = if (!isSystemInDarkTheme()) MainBlue else White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.clearSubscription() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = colors.background,
                        contentColor = MainBlue
                    ),
                    border = if (!isSystemInDarkTheme()) BorderStroke(2.dp, Red.copy(0.6f)) else BorderStroke(
                        2.dp,
                        Gray6
                    )
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.cancel_plan),
                        style = AppTypography.bodyMedium,
                        color = if (!isSystemInDarkTheme()) Red.copy(0.6f) else White
                    )
                }
            }
        } else {
            Column {
                ChoosePlanBox(navController)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = colors.background,
                        contentColor = MainBlue
                    ),
                    border = if (!isSystemInDarkTheme()) BorderStroke(
                        2.dp,
                        Color(0xFFE33C3C)
                    ) else BorderStroke(
                        2.dp,
                        Gray6
                    )
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.cancel_plan),
                        style = AppTypography.bodyMedium,
                        color = if (!isSystemInDarkTheme()) Color(0xFFE33C3C) else White
                    )
                }
            }
        }

        if (showConfirmDialog)
            ConfirmationAlertDialog(
                stringResource(R.string.confirm_cancellation),
                stringResource(R.string.are_you_sure_you_want_to_cancel_your_premium_subscription),
                stringResource(R.string.yes),
                stringResource(R.string.no),
                { viewModel.clearSubscription()
                    showConfirmDialog = false },
                { showConfirmDialog = false}
            )
    }
}
