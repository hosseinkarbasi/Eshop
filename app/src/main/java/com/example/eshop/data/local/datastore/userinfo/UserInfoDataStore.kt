package com.example.eshop.data.local.datastore.userinfo

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.eshop.data.local.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

@Singleton
class UserInfoDataStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.dataStore
    val preferences = dataStore.data.catch { cause ->
        Log.e("datastore_error", cause.message.toString())
    }.map { preferences ->
        preferences[KEY_USER] ?: ""
    }

    suspend fun getLogged(user: User) {
        dataStore.edit {
            it[KEY_USER] = user.email
        }
    }

    val KEY_USER = stringPreferencesKey("user_email")
}