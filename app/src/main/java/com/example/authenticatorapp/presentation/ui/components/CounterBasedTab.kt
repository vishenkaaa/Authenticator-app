package com.example.authenticatorapp.presentation.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel

@Composable
fun CounterBasedTab(
    accounts: List<AccountEntity>,
    searchQuery: String,
    context: Context,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val filtered = viewModel.filterAccounts(accounts, searchQuery, AccountType.HOTP)
    AccountList(
        accounts = filtered,
        context = context,
        navController = navController,
        viewModel = viewModel,
        isTimeBased = false
    )
}