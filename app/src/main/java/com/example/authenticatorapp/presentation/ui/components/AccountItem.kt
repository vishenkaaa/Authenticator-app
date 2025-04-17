package com.example.authenticatorapp.presentation.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily
import com.example.authenticatorapp.presentation.viewmodel.AddAccountViewModel
import com.example.authenticatorapp.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(account: AccountEntity,
                otp: String,
                remainingTime: Int = 0,
                context: Context,
                isTimeBased: Boolean,
                accountViewModel: AddAccountViewModel = hiltViewModel(),
                homeViewModel: HomeViewModel = hiltViewModel(),
                navController: NavController,
                isLastItem: Boolean = false
) {
    var accountExpanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showUpdate by remember { mutableStateOf(false) }

    var formattedOtp = otp.chunked(3).joinToString(" ")
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .then(if (isLastItem) Modifier.padding(bottom = 106.dp) else Modifier)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = MaterialTheme.colorScheme.inverseSurface,
                spotColor = MaterialTheme.colorScheme.inverseSurface
            )
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .clip(RoundedCornerShape(24.dp))
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        accountExpanded = true
                    }
                )
            },
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(getServiceIcon(account.serviceName)),
            contentDescription = "Service Logo",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = account.serviceName, style = AppTypography.bodyMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = account.email, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.W400, fontFamily = interFontFamily)
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = formattedOtp, fontSize = 24.sp, fontWeight = FontWeight.W500, fontFamily = interFontFamily)
                Spacer(modifier = Modifier.width(16.dp))
                if (isTimeBased) CustomCircularProgressIndicator(remainingTime)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.height(64.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.copy),
                contentDescription = "Copy OTP",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        copyToClipboard(otp, context)
                    }
            )

            if (!isTimeBased) {
                Row(
                    modifier = Modifier.clickable { showUpdate = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_update),
                        contentDescription = "Update OTP",
                        tint = MainBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = stringResource(R.string.update), style = AppTypography.labelSmall, color = MainBlue)
                }
            }
        }

        if(accountExpanded)
            ModalBottomSheet(
                onDismissRequest = { accountExpanded = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 41.dp)
                ) {
                    LazyColumn {
                        item {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            accountExpanded = false
                                            navController.navigate("EditAccount/${account.id}")
                                        })
                                        .padding(vertical = 12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.edit),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.edit),
                                        style = AppTypography.labelMedium
                                    )
                                }
                                Divider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Black.copy(alpha = 0.1f)
                                )
                            }
                        }
                        item {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            accountExpanded = false
                                            showConfirmDialog = true
                                        })
                                        .padding(vertical = 12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.delete),
                                        style = AppTypography.labelMedium
                                    )
                                }
                                Divider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Black.copy(alpha = 0.1f)
                                )
                            }
                        }
                    }
                }
            }

        if (showUpdate) {
            ModalBottomSheet(
                onDismissRequest = { showUpdate = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 86.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.are_you_sure_you_want_to_update_the_code),
                        style = AppTypography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                accountViewModel.incrementCounter(account)
                                val updatedOtp =
                                    homeViewModel.generateOtp(account.copy(counter = account.counter + 1))
                                formattedOtp = updatedOtp.chunked(3).joinToString(" ")
                                showUpdate = false
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MainBlue
                        ),
                        border = if (!isSystemInDarkTheme()) BorderStroke(
                            2.dp,
                            MainBlue
                        ) else BorderStroke(2.dp, Gray6)
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(R.drawable.ic_update),
                            contentDescription = "QR",
                            tint = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material.Text(
                            text = "Update code",
                            style = AppTypography.bodyMedium,
                            color = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                    }
                }
            }
        }

        if (showConfirmDialog)
            ConfirmationAlertDialog(
                stringResource(R.string.confirm_deletion),
                stringResource(R.string.are_you_sure_you_want_to_delete_this_account),
                stringResource(R.string.delete),
                stringResource(R.string.cancel),
                {accountViewModel.deleteAccount(account.id)
                    showConfirmDialog = false },
                {showConfirmDialog = false}
            )
    }
}

fun copyToClipboard(text: String, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("OTP", text)
    clipboard.setPrimaryClip(clip)
}

@Composable
fun getServiceIcon(serviceName: String): Int {
    return when (serviceName) {
        stringResource(R.string.banking_and_finance) -> R.drawable.s_banking_and_finance
        stringResource(R.string.website) -> R.drawable.s_website
        stringResource(R.string.mail) -> R.drawable.s_mail
        stringResource(R.string.social) -> R.drawable.s_social
        "Facebook" -> R.drawable.s_facebook
        "Instagram" -> R.drawable.s_instagram
        "Google" -> R.drawable.s_google
        "Linkedin" -> R.drawable.s_linkedin
        "Amazon" -> R.drawable.s_amazon_png
        "Paypal" -> R.drawable.s_paypall_png
        "Microsoft" -> R.drawable.s_microsoft
        "Discord" -> R.drawable.s_discord
        "Reddit" -> R.drawable.s_reddit_png
        "Netflix" -> R.drawable.s_netflix
        else -> R.drawable.s_mail
    }
}

