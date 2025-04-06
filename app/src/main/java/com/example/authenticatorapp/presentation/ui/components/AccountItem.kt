package com.example.authenticatorapp.presentation.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.Gray4
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily
import com.example.authenticatorapp.presentation.viewmodel.AddAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(account: AccountEntity,
                otp: String,
                remainingTime: Int = 0,
                context: Context,
                isTimeBased: Boolean,
                viewModel: AddAccountViewModel = hiltViewModel(),
                navController: NavController
) {
    var accountExpanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
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
                Text(text = otp, fontSize = 24.sp, fontWeight = FontWeight.W500, fontFamily = interFontFamily)
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
                    modifier = Modifier.clickable {},
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

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = {
                    Text(
                        stringResource(R.string.confirm_deletion),
                        style = AppTypography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                text = {
                    Text(
                        stringResource(R.string.are_you_sure_you_want_to_delete_this_account),
                        style = AppTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                confirmButton = {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error,
                        style = AppTypography.labelMedium,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                viewModel.deleteAccount(account.id)
                                showConfirmDialog = false
                            }
                    )
                },
                dismissButton = {
                    Text(
                        text = "Cancel",
                        color = Blue,
                        style = AppTypography.labelMedium,
                        modifier = Modifier.clickable {
                            showConfirmDialog = false
                        }
                    )
                },
                containerColor = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
        }

    }
}

fun copyToClipboard(text: String, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("OTP", text)
    clipboard.setPrimaryClip(clip)
}

fun getServiceIcon(serviceName: String): Int {
    return when (serviceName.lowercase()) {
        "banking and finance" -> R.drawable.s_banking_and_finance
        "website" -> R.drawable.s_website
        "main" -> R.drawable.s_mail
        "social" -> R.drawable.s_social
        "facebook" -> R.drawable.s_facebook
        "instagram" -> R.drawable.s_instagram
        "google" -> R.drawable.s_google
        "linkedin" -> R.drawable.s_linkedin
        "amazon" -> R.drawable.s_amazon_png
        "paypal" -> R.drawable.s_paypall_png
        "microsoft" -> R.drawable.s_microsoft
        "discord" -> R.drawable.s_discord
        "reddit" -> R.drawable.s_reddit_png
        "netflix" -> R.drawable.s_netflix
        else -> R.drawable.s_mail
    }
}

