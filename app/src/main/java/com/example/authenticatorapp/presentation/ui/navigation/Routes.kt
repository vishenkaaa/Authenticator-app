package com.example.authenticatorapp.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Onboarding

@Serializable
object Main

@Serializable
object Paywall

@Serializable
object SignIn

@Serializable
object Home

@Serializable
object QrScanner

@Serializable
object AddAccount

@Serializable
data class EditAccount(val accountId: Int)

@Serializable
object Info

@Serializable
object AboutApp

@Serializable
object Subscription

@Serializable
object PremiumFeatures

@Serializable
object AppLock

@Serializable
object CreatePasscode

@Serializable
data class VerifyPasscode(val action: String)

@Serializable
object PrivacyPolicy

@Serializable
object TermsOfUse
