package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.ChoosePlanBox
import com.example.authenticatorapp.presentation.ui.components.ConfirmationAlertDialog
import com.example.authenticatorapp.presentation.ui.components.CustomTopAppBar
import com.example.authenticatorapp.presentation.ui.components.SignInBox
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.Gray2
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.Red
import com.example.authenticatorapp.presentation.viewmodel.AuthViewModel
import com.example.authenticatorapp.presentation.viewmodel.SubscriptionViewModel
import com.example.authenticatorapp.presentation.viewmodel.SyncViewModel

@Composable
fun PremiumFeaturesScreen(
    navController: NavController,
    context: Context,
    syncViewModel: SyncViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    subscriptionViewModel: SubscriptionViewModel = hiltViewModel()
){
    val isSyncEnabled by syncViewModel.isSyncEnabled.collectAsState()

    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var showConfirmSignOutDialog by remember { mutableStateOf(false) }

    val plan by subscriptionViewModel.currentPlan.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        CustomTopAppBar(navController, stringResource(R.string.premium_features))

        if (!isAuthenticated) {
            SignInBox(navController)
        }
        else if (plan != null) {
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { showConfirmSignOutDialog = true })
                            .padding(vertical = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_login),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.sign_out),
                            modifier = Modifier.weight(1f),
                            style = AppTypography.bodyMedium
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_right),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Black.copy(alpha = 0.1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {})
                            .padding(vertical = 3.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_synchronize),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.synchronize),
                            modifier = Modifier.weight(1f),
                            style = AppTypography.bodyMedium
                        )

                        Switch(
                            checked = isSyncEnabled,
                            onCheckedChange = { syncViewModel.setSyncEnabled(it)},
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = Blue,
                                uncheckedThumbColor = Gray2,
                                uncheckedTrackColor = if (!isSystemInDarkTheme()) White
                                else MaterialTheme.colorScheme.background,
                                checkedBorderColor = MainBlue,
                                uncheckedBorderColor = Gray5,
                            ),
                            thumbContent = null,
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Black.copy(alpha = 0.1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { showConfirmDeleteDialog = true })
                            .padding(vertical = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null,
                            tint = Red,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.delete_account),
                            modifier = Modifier.weight(1f),
                            color = Red,
                            style = AppTypography.bodyMedium
                        )
                    }
                }
            }
        }
        else {
            ChoosePlanBox(navController)
        }

        if (showConfirmDeleteDialog)
            ConfirmationAlertDialog(
                stringResource(R.string.confirm_deletion),
                stringResource(R.string.are_you_sure_you_want_to_delete_your_account),
                stringResource(R.string.delete),
                stringResource(R.string.cancel),
                { authViewModel.deleteUserAccount(context)
                    showConfirmDeleteDialog = false },
                {showConfirmDeleteDialog = false})

        if (showConfirmSignOutDialog)
            ConfirmationAlertDialog(
                stringResource(R.string.confirm_sign_out),
                stringResource(R.string.are_you_sure_you_want_to_log_out),
                stringResource(R.string.sign_out),
                stringResource(R.string.cancel),
                { authViewModel.signOut(context)
                    showConfirmSignOutDialog = false },
                {showConfirmSignOutDialog = false})
    }
}