package com.example.authenticatorapp.presentation.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.data.model.OtpAlgorithm
import com.example.authenticatorapp.data.model.ServiceName
import com.example.authenticatorapp.presentation.ui.components.ServiceItem
import com.example.authenticatorapp.presentation.ui.components.SimpleServiceItem
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.utils.extensions.toLocalizedStringRes
import com.example.authenticatorapp.presentation.viewmodel.AddAccountViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    navController: NavController,
    context: Context,
    viewModel: AddAccountViewModel = hiltViewModel(),
    oldAccountId: Int? = null) {
    //FIXME не використовуємо напряму MaterialTheme.colorScheme замість colors
    //Done
    val serviceSheetState = rememberModalBottomSheetState()
    val keySheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    fun openSheetService() {
        coroutineScope.launch { serviceSheetState.show() } }
    fun closeSheetService() {
        coroutineScope.launch { serviceSheetState.hide() } }

    fun openSheetKey() {
        coroutineScope.launch { keySheetState.show() } }
    fun closeSheetKey() {
        coroutineScope.launch { keySheetState.hide() } }

    val base32Regex = Regex("^[A-Z2-7]+=*$")

    //FIXME не використовуємо напряму tringResource(R.string.time_based) замість змінної
    //Done
    val selectedService by viewModel.selectedService.collectAsState()
    val selectedTypeOfKey by viewModel.selectedTypeOfKey.collectAsState()

    //FIXME переносимо це в viewModel
    //Done
    val accountText by viewModel.accountName.collectAsState()
    val keyText by viewModel.secretKey.collectAsState()

    if(oldAccountId!=null){
        LaunchedEffect(key1 = oldAccountId) {
            oldAccountId.let { id ->
                viewModel.getAccountById(id)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                color = MaterialTheme.colorScheme.onPrimary,
                style = AppTypography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { openSheetService() }
        ) {
            OutlinedTextField(
                value = selectedService?.toLocalizedStringRes() ?: "",
                onValueChange = { viewModel.setSelectedService(ServiceName.from(it)) },
                readOnly = true,
                enabled = false,
                textStyle = AppTypography.labelMedium,
                placeholder = {
                    Text(
                        text = stringResource(R.string.service),
                        style = AppTypography.labelMedium
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.drop_down),
                        contentDescription = "Dropdown",
                        tint = Color.Unspecified
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                    disabledPlaceholderColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = accountText,
            onValueChange = { viewModel.setAccountName(it) },
            readOnly = false,
            placeholder = { Text(text = stringResource(R.string.account), style = AppTypography.labelMedium) },
            textStyle = AppTypography.labelMedium,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = "Account Icon",
                    tint = Color.Unspecified
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
            onValueChange = { viewModel.setSecretKey(it) },
            readOnly = false,
            placeholder = { Text(text = stringResource(R.string.key), style = AppTypography.labelMedium) },
            textStyle = AppTypography.labelMedium,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.key),
                    contentDescription = "Key Icon",
                    tint = Color.Unspecified
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Gray5,
                unfocusedLabelColor = Color.Gray,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { openSheetKey() }
        ) {
            OutlinedTextField(
                value = selectedTypeOfKey.toLocalizedStringRes(),
                onValueChange = { AccountType.from(it)?.let { type ->
                    viewModel.setSelectedKeyType(type)
                } },
                readOnly = true,
                enabled = false,
                textStyle = AppTypography.labelMedium,
                placeholder = {
                    Text(
                        text = stringResource(R.string.type_of_key),
                        style = AppTypography.bodyMedium
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.drop_down),
                        contentDescription = "Dropdown",
                        tint = Color.Unspecified
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                    disabledPlaceholderColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val sanitizedKey = keyText.trim().replace(" ", "").uppercase()

                if (!base32Regex.matches(sanitizedKey)) {
                    Toast.makeText(context,
                        context.getString(R.string.invalid_secret_format), Toast.LENGTH_LONG).show()
                    return@Button
                }
                if (selectedService == null) {
                    Toast.makeText(context,
                        context.getString(R.string.service_name_is_required), Toast.LENGTH_LONG).show()
                    return@Button
                }
                if (accountText.isBlank()) {
                    Toast.makeText(context,
                        context.getString(R.string.account_name_is_required), Toast.LENGTH_LONG).show()
                    return@Button
                }
                if (keyText.isBlank()) {
                    Toast.makeText(context,
                        context.getString(R.string.secret_key_is_required), Toast.LENGTH_LONG).show()
                    return@Button
                }

                val initialCounter = if (selectedTypeOfKey == AccountType.HOTP) 1L else 0L
                if(oldAccountId == null){
                    viewModel.addAccount(
                        service = selectedService!!.displayName,
                        email = accountText,
                        secret = keyText,
                        type = selectedTypeOfKey,
                        algorithm = OtpAlgorithm.SHA1,
                        digits = 6,
                        counter = initialCounter
                    )
                    Toast.makeText(context, context.getString(R.string.account_successfully_added), Toast.LENGTH_LONG).show()
                }
                else {
                    val account = viewModel.account.value
                    val newCounter = if (selectedTypeOfKey == AccountType.HOTP && account?.counter == 0L) 1L
                    else account?.counter ?: initialCounter

                    viewModel.updateAccount(
                        id = oldAccountId,
                        service = selectedService!!.displayName,
                        email = accountText,
                        secret = keyText,
                        type = selectedTypeOfKey,
                        algorithm = OtpAlgorithm.SHA1,
                        digits = 6,
                        counter = newCounter
                    )
                    Toast.makeText(context,
                        context.getString(R.string.account_successfully_updated), Toast.LENGTH_LONG).show()
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
                tint = White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if(oldAccountId==null) stringResource(R.string.add)
                else stringResource(R.string.save),
                style = AppTypography.labelMedium,
                color = White
            )
        }
    }

    if (serviceSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { closeSheetService() },
            sheetState = serviceSheetState,
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
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

                //FIXME не потрібно змінних, напряму викликаємо stringResource
                //Done
                LazyColumn {
                    item {
                        ServiceItem(
                            text = stringResource(R.string.banking_and_finance),
                            iconResId = R.drawable.s_banking_and_finance
                        ) {
                            viewModel.setSelectedService(ServiceName.BANKING_AND_FINANCE)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = stringResource(R.string.website),
                            iconResId = R.drawable.s_website
                        ) {
                            viewModel.setSelectedService(ServiceName.WEBSITE)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = stringResource(R.string.mail),
                            iconResId = R.drawable.s_mail
                        ) {
                            viewModel.setSelectedService(ServiceName.MAIL)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = stringResource(R.string.social),
                            iconResId = R.drawable.s_social
                        ) {
                            viewModel.setSelectedService(ServiceName.SOCIAL)
                            closeSheetService()
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
                            viewModel.setSelectedService(ServiceName.GOOGLE)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Instagram",
                            iconResId = R.drawable.s_instagram
                        ) {
                            viewModel.setSelectedService(ServiceName.INSTAGRAM)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Facebook",
                            iconResId = R.drawable.s_facebook
                        ) {
                            viewModel.setSelectedService(ServiceName.FACEBOOK)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "LinkedIn",
                            iconResId = R.drawable.s_linkedin
                        ) {
                            viewModel.setSelectedService(ServiceName.LINKEDIN)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Amazon",
                            iconResId = R.drawable.s_amazon_png
                        ) {
                            viewModel.setSelectedService(ServiceName.AMAZON)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "PayPal",
                            iconResId = R.drawable.s_paypall_png
                        ) {
                            viewModel.setSelectedService(ServiceName.PAYPAL)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Microsoft",
                            iconResId = R.drawable.s_microsoft
                        ) {
                            viewModel.setSelectedService(ServiceName.MICROSOFT)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Discord",
                            iconResId = R.drawable.s_discord
                        ) {
                            viewModel.setSelectedService(ServiceName.DISCORD)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Reddit",
                            iconResId = R.drawable.s_reddit_png
                        ) {
                            viewModel.setSelectedService(ServiceName.REDDIT)
                            closeSheetService()
                        }
                    }

                    item {
                        ServiceItem(
                            text = "Netflix",
                            iconResId = R.drawable.s_netflix
                        ) {
                            viewModel.setSelectedService(ServiceName.NETFLIX)
                            closeSheetService()
                        }
                    }
                }
            }
        }
    }

    if (keySheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { openSheetKey() },
            sheetState = keySheetState,
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 27.dp)
            ) {
                //FIXME не потрібно змінних, напряму викликаємо stringResource
                //Done

                SimpleServiceItem(text = stringResource(R.string.time_based)) {
                    viewModel.setSelectedKeyType(AccountType.TOTP)
                    closeSheetKey()
                }

                SimpleServiceItem(text = stringResource(R.string.counter_based)) {
                    viewModel.setSelectedKeyType(AccountType.HOTP)
                    closeSheetKey()
                }
            }
        }
    }
}