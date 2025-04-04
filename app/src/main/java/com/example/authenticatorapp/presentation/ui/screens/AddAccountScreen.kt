package com.example.authenticatorapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(navController: NavController) {
    val colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var serviceExpanded by remember { mutableStateOf(false) }
    var typesOfKeyExpanded by remember { mutableStateOf(false) }

    var selectedService by remember { mutableStateOf("") }
    var selectedTypeOfKey by remember { mutableStateOf("Time-based") }

    var accountText by remember { mutableStateOf("") }
    var keyText by remember { mutableStateOf("") }

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
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add),
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
                            iconResId = R.drawable.banking_and__finance
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
                            iconResId = R.drawable.website
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
                            iconResId = R.drawable.mail
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
                            iconResId = R.drawable.social
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
                val txt_counter_based = stringResource(R.string.time_based)

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

@Composable
fun ServiceItem(
    text: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 16.dp).size(32.dp)
            )
            Text(
                text = text,
                style = AppTypography.bodyMedium
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Black.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun SimpleServiceItem(
    text: String,
    onClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = text,
                style = AppTypography.bodyMedium
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Black.copy(alpha = 0.1f)
        )
    }
}