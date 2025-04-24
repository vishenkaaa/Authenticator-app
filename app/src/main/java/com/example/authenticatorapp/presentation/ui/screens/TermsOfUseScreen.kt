package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.CustomTopAppBar
import com.example.authenticatorapp.presentation.ui.theme.AppTypography

@Composable
fun TermsOfUseScreen(navController: NavController) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        CustomTopAppBar(navController, stringResource(R.string.terms_of_use))

        Text(
            text = stringResource(R.string.this_app_is_provided_as_is_you_use_it_at_your_own_risk),
            style = AppTypography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsOfUseScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        TermsOfUseScreen(navController = navController)
    }
}
