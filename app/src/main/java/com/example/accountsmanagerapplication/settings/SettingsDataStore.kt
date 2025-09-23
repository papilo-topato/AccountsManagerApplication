package com.example.accountsmanagerapplication.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val SETTINGS_NAME = "app_settings"

val Context.dataStore by preferencesDataStore(name = SETTINGS_NAME)

object SettingsKeys {
    val darkThemeEnabled: Preferences.Key<Boolean> = booleanPreferencesKey("dark_theme_enabled")
}

class SettingsRepository(private val context: Context) {
    val isDarkThemeEnabled: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[SettingsKeys.darkThemeEnabled] ?: false }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.darkThemeEnabled] = enabled
        }
    }
}



