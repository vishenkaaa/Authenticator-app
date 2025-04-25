package com.example.authenticatorapp.presentation.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.navigation.AddAccount
import com.example.authenticatorapp.presentation.ui.navigation.QrScanner
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.example.authenticatorapp.presentation.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddModalBottomSheet(
    navController: NavController,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    context: Context,
    closeSheet: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { closeSheet() },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                //FIXME в дизайні відступ 40
                //там 40, але не до полосочки, до полосочки 60
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Button(
                    onClick = {
                        closeSheet()
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            navController.navigate(QrScanner)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                        //FIXME не використовуй статичний розмір. Якщо в користувача буде збільшений щрифт в системі, він не поміститься у кнопку, бо ти обмежила її висоту. Краще додай відступи для контенту
                        //Done
                    shape = RoundedCornerShape(24.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.background,
                        contentColor = MainBlue
                    ),
                    border = if (!isSystemInDarkTheme()) BorderStroke(
                        2.dp,
                        MainBlue
                    ) else BorderStroke(2.dp, Gray6)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.qr_code),
                        contentDescription = "QR",
                        modifier = Modifier.padding(vertical = 5.dp),
                        tint = if (!isSystemInDarkTheme()) MainBlue else White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.scan_qr_code),
                        style = AppTypography.bodyMedium,
                        color = if (!isSystemInDarkTheme()) MainBlue else White
                    )
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        closeSheet()
                        navController.navigate(AddAccount)
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                        // FIXME статичний розмір
                        //Done,
                    shape = RoundedCornerShape(24.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.background,
                        contentColor = MainBlue
                    ),
                    // FIXME давай замість того, щоб кожен раз перевіряти яка в нас зараз тема ми використаємо кольори з ColorScheme
                    //Done
                    border = BorderStroke( 2.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = "Edit",
                        modifier = Modifier.padding(vertical = 4.dp),
                        tint = if (!isSystemInDarkTheme()) MainBlue else White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.enter_code_manually),
                        style = AppTypography.bodyMedium,
                        color = if (!isSystemInDarkTheme()) MainBlue else White
                    )
                }
            }
        }
    }
}