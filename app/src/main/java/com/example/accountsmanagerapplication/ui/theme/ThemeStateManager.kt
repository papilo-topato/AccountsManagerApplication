package com.example.accountsmanagerapplication.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class ThemeStateManager {
    var isDarkTheme by mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}

val LocalThemeState = staticCompositionLocalOf<ThemeStateManager> {
    error("No ThemeStateManager provided")
}

