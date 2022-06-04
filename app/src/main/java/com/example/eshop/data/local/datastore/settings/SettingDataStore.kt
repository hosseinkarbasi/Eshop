package com.example.eshop.data.local.datastore.settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.eshop.data.local.datastore.settings.SettingPreferencesKey.KEY_THEME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore_settings")

@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.dataStore
    val preferences = dataStore.data.catch { cause ->
        Log.e("datastore_error", cause.message.toString())
    }.map { preferences ->
        val theme: Theme = Theme.valueOf(preferences[KEY_THEME] ?: Theme.AUTO.name)
        PreferencesInfo(theme = theme)
    }

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit {
            it[KEY_THEME] = theme.name
        }
    }

}