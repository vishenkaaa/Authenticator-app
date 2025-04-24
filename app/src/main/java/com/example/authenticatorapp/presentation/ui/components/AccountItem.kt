package com.example.authenticatorapp.presentation.ui.components

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.RequiresPermission
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.example.authenticatorapp.data.model.ServiceName
import com.example.authenticatorapp.presentation.ui.navigation.EditAccount
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily
import com.example.authenticatorapp.presentation.utils.extensions.toLocalizedStringRes
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
    var showConfirmDialog by remember { mutableStateOf(false) }

    val editSheetState = rememberModalBottomSheetState()
    val updateSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    fun openEditSheet() {
        coroutineScope.launch { editSheetState.show() } }
    fun closeEditSheet() {
        coroutineScope.launch { editSheetState.hide() } }

    fun openUpdateSheet() {
        coroutineScope.launch { updateSheetState.show() } }
    fun closeUpdateSheet() {
        coroutineScope.launch { updateSheetState.hide() } }

    var formattedOtp = otp.chunked(3).joinToString(" ")

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
                        openEditSheet()
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
            Text(text = account.serviceName.toLocalizedStringRes(), style = AppTypography.bodyMedium)
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
                    modifier = Modifier.clickable { openUpdateSheet() },
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

        if(editSheetState.isVisible)
            ModalBottomSheet(
                onDismissRequest = { closeEditSheet() },
                sheetState = editSheetState,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 41.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    closeEditSheet()
                                    navController.navigate(EditAccount(account.id))
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

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Black.copy(alpha = 0.1f)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    closeEditSheet()
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
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Black.copy(alpha = 0.1f)
                        )
                    }
                }
            }

        if (updateSheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { closeUpdateSheet() },
                sheetState = updateSheetState,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 72.dp),
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
                                closeUpdateSheet()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
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
                            modifier = Modifier.padding(vertical = 8.dp),
                            tint = if (!isSystemInDarkTheme()) MainBlue else White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material.Text(
                            text = stringResource(R.string.update_code),
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
                {
                    accountViewModel.deleteAccount(account.id)
                    showConfirmDialog = false },
                {
                    showConfirmDialog = false
                }
            )
    }
}

@RequiresPermission(Manifest.permission.VIBRATE)
fun copyToClipboard(text: String, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("OTP", text)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(context, context.getString(R.string.code_copied_to_clipboard), Toast.LENGTH_LONG).show()

    val vibrator = context.getSystemService(Vibrator::class.java)
    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
}

@Composable
fun getServiceIcon(serviceName: ServiceName): Int {
    return ServiceName.from(serviceName.displayName).iconRes
}