package com.example.authenticatorapp.presentation.model

sealed class OnboardingPage(val id: Int) {
    object PageOne : OnboardingPage(0)
    object PageTwo : OnboardingPage(1)
    object PageThree : OnboardingPage(2)
    object PageFour : OnboardingPage(3)

    companion object {
        const val BULLET_SYMBOL = "\u2022"
        fun getPageById(id: Int): OnboardingPage = when (id) {
            0 -> PageOne
            1 -> PageTwo
            2 -> PageThree
            3 -> PageFour
            else -> throw IllegalArgumentException("Invalid onboarding page id")
        }
    }
}
