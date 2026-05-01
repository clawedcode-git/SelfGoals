package com.example.selfgoals.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.selfgoals.ui.dashboard.SortOption
import com.example.selfgoals.ui.dashboard.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val SORT_OPTION = stringPreferencesKey("sort_option")
        val SHOW_ARCHIVED = booleanPreferencesKey("show_archived")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val name = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        try { ThemeMode.valueOf(name) } catch (e: Exception) { ThemeMode.SYSTEM }
    }

    val sortOption: Flow<SortOption> = context.dataStore.data.map { preferences ->
        val name = preferences[PreferencesKeys.SORT_OPTION] ?: SortOption.DATE_CREATED.name
        try { SortOption.valueOf(name) } catch (e: Exception) { SortOption.DATE_CREATED }
    }

    val showArchived: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SHOW_ARCHIVED] ?: false
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setSortOption(option: SortOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_OPTION] = option.name
        }
    }

    suspend fun setShowArchived(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_ARCHIVED] = show
        }
    }
}
