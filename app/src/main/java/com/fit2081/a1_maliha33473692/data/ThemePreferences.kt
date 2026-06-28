package com.fit2081.a1_maliha33473692.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// attaches a DataStore<Preferences> to every Context
private val Context.dataStore by preferencesDataStore(name = "settings_prefs")

object ThemePreferences {
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")

    // call this from a coroutine
    suspend fun setDarkMode(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    // a Flow which can collect in Compose
    fun darkModeFlow(context: Context): Flow<Boolean> =
        context.dataStore.data
            .map { it[DARK_MODE_KEY] ?: false }
}
