package com.example.authenticatorapp.data.model

import com.example.authenticatorapp.R

enum class ServiceName(val displayName: String, val iconRes: Int) {
    FACEBOOK("Facebook", R.drawable.s_facebook),
    INSTAGRAM("Instagram", R.drawable.s_instagram),
    GOOGLE("Google", R.drawable.s_google),
    LINKEDIN("Linkedin", R.drawable.s_linkedin),
    AMAZON("Amazon", R.drawable.s_amazon_png),
    PAYPAL("Paypal", R.drawable.s_paypall_png),
    MICROSOFT("Microsoft", R.drawable.s_microsoft),
    DISCORD("Discord", R.drawable.s_discord),
    REDDIT("Reddit", R.drawable.s_reddit_png),
    NETFLIX("Netflix", R.drawable.s_netflix),

    BANKING_AND_FINANCE("Banking and finance", R.drawable.s_banking_and_finance),
    WEBSITE("Website", R.drawable.s_website),
    MAIL("Mail", R.drawable.s_mail),
    SOCIAL("Social", R.drawable.s_social),

    UNKNOWN("Unknown", R.drawable.s_mail);

    companion object {
        fun from(name: String): ServiceName {
            return entries.find { it.displayName.equals(name, ignoreCase = true) } ?: UNKNOWN
        }
    }
}