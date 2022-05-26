package com.example.eshop.data.local.data_store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.concurrent.Flow
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

    }

}