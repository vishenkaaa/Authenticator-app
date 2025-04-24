package com.example.authenticatorapp.presentation.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel

@Composable
fun AccountList(
    accounts: List<AccountEntity>,
    context: Context,
    navController: NavController,
    viewModel: HomeViewModel,
    isTimeBased: Boolean
) {
    val remainingTime by viewModel.timeTicker.collectAsState()
    if (accounts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.nothing_found),
                color = Color.Gray,
                style = AppTypography.labelMedium
            )
        }
    } else {
        LazyColumn {
            itemsIndexed(accounts) { index, account ->
                //FIXME не використовуємо while (true). Кращее пошукай якісь рішення з таймером, який спрацьовуватиме кожні 30 секунд і винести цю логіку в viewModel
                //Done
                val otp = remember(remainingTime) { viewModel.generateOtp(account) }

                AccountItem(
                    account = account,
                    otp = otp,
                    remainingTime = remainingTime,
                    context = context,
                    isTimeBased = isTimeBased,
                    navController = navController,
                    isLastItem = index == accounts.size - 1
                )
            }
        }
    }
}