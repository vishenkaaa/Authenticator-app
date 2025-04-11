package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.components.ServiceItem
import com.example.authenticatorapp.presentation.ui.components.SimpleServiceItem
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.viewmodel.AddAccountViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(navController: NavController, context: Context, viewModel: AddAccountViewModel = hiltViewModel(), oldAccountId: Int? = null) {
    val colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var serviceExpanded by remember { mutableStateOf(false) }
    var typesOfKeyExpanded by remember { mutableStateOf(false) }

    val txt_time_based = stringResource(R.string.time_based)
    var selectedService by remember { mutableStateOf("") }
    var selectedTypeOfKey by remember { mutableStateOf(txt_time_based) }

    var accountText by remember { mutableStateOf("") }
    var keyText by remember { mutableStateOf("") }

    if(oldAccountId!=null){
        LaunchedEffect(key1 = oldAccountId) {
            oldAccountId?.let { id ->
                viewModel.getAccountById(id)
            }
        }

        val oldAccount = viewModel.account.collectAsState().value
        LaunchedEffect(key1 = oldAccount) {
            oldAccount?.let {
                accountText = it.email
                keyText = it.secret
                selectedService = it.serviceName
                selectedTypeOfKey = if(it.type == "TOTP") "Time-based" else "Counter-based"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 52.dp),
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
                text = stringResource(R.string.add_account),
                color = colors.onPrimary,
                style = AppTypography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = selectedService,
            onValueChange = { selectedService = it },
            readOnly = true,
            textStyle = AppTypography.bodyMedium,
            label = { Text(text = stringResource(R.string.service), style = AppTypography.labelMedium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.drop_down),
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable {
                        serviceExpanded = true
                    },
                    tint = Color.Unspecified
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colors.onPrimaryContainer,
                focusedContainerColor = colors.onPrimaryContainer,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Gray5,
                unfocusedLabelColor = Color.Gray,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { serviceExpanded = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = accountText,
            onValueChange = {newText ->
                accountText = newText  },
            readOnly = false,
            label = { Text(text = stringResource(R.string.account), style = AppTypography.labelMedium) },
            textStyle = AppTypography.bodyMedium,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = "Account Icon",
                    tint = Color.Unspecified
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colors.onPrimaryContainer,
                focusedContainerColor = colors.onPrimaryContainer,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Gray5,
                unfocusedLabelColor = Color.Gray,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = keyText,
            onValueChange = { newText ->
                keyText = newText
            },
            readOnly = false,
            label = { Text(text = stringResource(R.string.key), style = AppTypography.labelMedium) },
            textStyle = AppTypography.bodyMedium,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.key),
                    contentDescription = "Key Icon",
                    tint = Color.Unspecified
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colors.onPrimaryContainer,
                focusedContainerColor = colors.onPrimaryContainer,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Gray5,
                unfocusedLabelColor = Color.Gray,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = selectedTypeOfKey,
            onValueChange = { selectedTypeOfKey = it },
            readOnly = true,
            textStyle = AppTypography.labelMedium,
            label = { Text(text = stringResource(R.string.type_of_key), style = AppTypography.bodyMedium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.drop_down),
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable {
                        typesOfKeyExpanded = true
                    },
                    tint = Color.Unspecified
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = colors.onPrimaryContainer,
                focusedContainerColor = colors.onPrimaryContainer,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Gray5,
                unfocusedLabelColor = Color.Gray,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { typesOfKeyExpanded = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
//                val base32Regex = Regex("^[A-Z2-7]+=*$")
//                if (!keyText.uppercase().matches(base32Regex)) {
//                    Toast.makeText(context,
//                        "Invalid secret format", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
                if (selectedService.isBlank()) {
                    Toast.makeText(context,
                        context.getString(R.string.service_name_is_required), Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (accountText.isBlank()) {
                    Toast.makeText(context,
                        context.getString(R.string.account_name_is_required), Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (keyText.isBlank()) {
                    Toast.makeText(context,
                        context.getString(R.string.secret_key_is_required), Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val initialCounter = if (selectedTypeOfKey == "Counter-based") 1L else 0L
                if(oldAccountId == null){
                    viewModel.addAccount(
                        service = selectedService,
                        email = accountText,
                        secret = keyText,
                        type = selectedTypeOfKey,
                        algorithm = "HmacSHA1",
                        digits = 6,
                        counter = initialCounter
                    )
                    Toast.makeText(context, context.getString(R.string.account_successfully_added), Toast.LENGTH_SHORT).show()
                }
                else {
                    val account = viewModel.account.value
                    val newCounter = if (selectedTypeOfKey == "Counter-based" && account?.counter == 0L) 1L
                    else account?.counter ?: initialCounter

                    viewModel.updateAccount(
                        id = oldAccountId,
                        service = selectedService,
                        email = accountText,
                        secret = keyText,
                        type = selectedTypeOfKey,
                        algorithm = "HmacSHA1",
                        digits = 6,
                        counter = newCounter
                    )
                    Toast.makeText(context,
                        context.getString(R.string.account_successfully_updated), Toast.LENGTH_SHORT).show()
                }

                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
        ) {
            Icon(
                painter = if(oldAccountId==null) painterResource(R.drawable.ic_add)
                else painterResource(R.drawable.edit),
                contentDescription = "Add",
                Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if(oldAccountId==null) stringResource(R.string.add)
                else stringResource(R.string.save),
                style = AppTypography.bodyMedium,
                color = White
            )
        }
    }

    if (serviceExpanded) {
        ModalBottomSheet(
            onDismissRequest = { serviceExpanded = false },
            sheetState = sheetState,
            containerColor = colors.onPrimaryContainer,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 41.dp)
            ) {

                Text(
                    text = stringResource(R.string.general),
                    style = AppTypography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val txt_banking = stringResource(R.string.banking_and_finance)
                val txt_website = stringResource(R.string.website)
                val txt_mail = stringResource(R.string.mail)
                val txt_social = stringResource(R.string.social)
                LazyColumn {
                    item {
                        ServiceItem(
                            text = stringResource(R.string.banking_and_finance),
                            iconResId = R.drawable.s_banking_and_finance
                        ) {
                            selectedService = txt_banking
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = stringResource(R.string.website),
                            iconResId = R.drawable.s_website
                        ) {
                            selectedService = txt_website
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = stringResource(R.string.mail),
                            iconResId = R.drawable.s_mail
                        ) {
                            selectedService = txt_mail
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = stringResource(R.string.social),
                            iconResId = R.drawable.s_social
                        ) {
                            selectedService = txt_social
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.services),
                            style = AppTypography.bodyLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        ServiceItem(
                            text = "Google",
                            iconResId = R.drawable.s_google
                        ) {
                            selectedService = "Google"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Instagram",
                            iconResId = R.drawable.s_instagram
                        ) {
                            selectedService = "Instagram"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Facebook",
                            iconResId = R.drawable.s_facebook
                        ) {
                            selectedService = "Facebook"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "LinkedIn",
                            iconResId = R.drawable.s_linkedin
                        ) {
                            selectedService = "LinkedIn"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Amazon",
                            iconResId = R.drawable.s_amazon_png
                        ) {
                            selectedService = "Amazon"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "PayPal",
                            iconResId = R.drawable.s_paypall_png
                        ) {
                            selectedService = "PayPal"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Microsoft",
                            iconResId = R.drawable.s_microsoft
                        ) {
                            selectedService = "Microsoft"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Discord",
                            iconResId = R.drawable.s_discord
                        ) {
                            selectedService = "Discord"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Reddit",
                            iconResId = R.drawable.s_reddit_png
                        ) {
                            selectedService = "Reddit"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Netflix",
                            iconResId = R.drawable.s_netflix
                        ) {
                            selectedService = "Netflix"
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) serviceExpanded = false
                            }
                        }
                    }
                }
            }
        }
    }

    if (typesOfKeyExpanded) {
        ModalBottomSheet(
            onDismissRequest = { typesOfKeyExpanded = false },
            sheetState = sheetState,
            containerColor = colors.onPrimaryContainer,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 27.dp)
            ) {
                val txt_time_based = stringResource(R.string.time_based)
                val txt_counter_based = stringResource(R.string.counter_based)

                SimpleServiceItem(text = stringResource(R.string.time_based)) {
                    selectedTypeOfKey = txt_time_based
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) typesOfKeyExpanded = false
                    }
                }

                SimpleServiceItem(text = stringResource(R.string.counter_based)) {
                    selectedTypeOfKey = txt_counter_based
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) typesOfKeyExpanded = false
                    }
                }
            }
        }
    }
}