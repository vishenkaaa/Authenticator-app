package com.example.authenticatorapp.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

val interFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = GoogleFont.Provider(
            providerAuthority = "com.google.android.gms.fonts",
            providerPackage = "com.google.android.gms",
            certificates = emptyList()
        )
    )
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 40.sp
    ),
    titleMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),

    labelLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 40.sp
    ),
    labelMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
)