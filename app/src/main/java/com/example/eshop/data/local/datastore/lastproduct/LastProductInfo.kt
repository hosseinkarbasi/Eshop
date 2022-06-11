package com.example.eshop.data.local.datastore.lastproduct

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "product")

@Singleton
class LastProductInfo @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.dataStore
    val preferences: Flow<Int> = dataStore.data.catch { cause ->
        Log.e("datastore_error", cause.message.toString())
    }.map { preferences ->
        val id = preferences[PRODUCT_ID] ?: 0
        id
    }

    suspend fun saveUserInfo(userId: Int) {
        dataStore.edit {
            it[PRODUCT_ID] = userId
        }
    }

    private val PRODUCT_ID = intPreferencesKey("product_id")
}