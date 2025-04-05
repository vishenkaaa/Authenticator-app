package com.example.authenticatorapp.presentation.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.MainBlue
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily

@Composable
fun AccountItem(account: AccountEntity, otp: String, remainingTime: Int = 0, context: Context, isTimeBased: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0xFF000000).copy(alpha = 0.1f),
                ambientColor = Color(0xFF000000).copy(alpha = 0.1f)
            )
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .clip(RoundedCornerShape(24.dp))
            .padding(16.dp),
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

