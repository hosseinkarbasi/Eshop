package com.example.eshop.data.local.datastore.userinfo

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.eshop.data.local.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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
    val preferences: Flow<UserInfo> = dataStore.data.catch { cause ->
        Log.e("datastore_error", cause.message.toString())
    }.map { preferences ->
        val email = preferences[USER_EMAIL] ?: ""
        val id = preferences[USER_ID] ?: 0
        UserInfo(email, id)
    }

    suspend fun setEmail(email: String, userId: Int) {
        dataStore.edit {
            it[USER_EMAIL] = email
            it[USER_ID] = userId
        }
    }


    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_ID = intPreferencesKey("user_id")
}