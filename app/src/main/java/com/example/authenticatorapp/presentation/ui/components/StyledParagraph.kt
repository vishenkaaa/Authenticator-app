package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Gray3
import com.example.authenticatorapp.presentation.ui.theme.interFontFamily

@Composable
fun StyledParagraph(
    startText: String,
    boldText: String,
    endText: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val annotatedString = buildAnnotatedString {

        append(startText)

        withStyle( style = SpanStyle(
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            fontFamily = interFontFamily,
            color = if(!isSystemInDarkTheme()) Color(0xFF3E3E3E) else Gray3
        )
        ) {
            append(boldText)
        }

        append(endText)
    }

    Text(
        text = annotatedString,
        style = AppTypography.labelMedium,
        color = colors.surface,
        modifier = modifier
    )
}