package com.parisara.cycle.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SessionManager(private val context: Context) {

    companion object {
        val LOGGED_IN_USER_ID = longPreferencesKey("logged_in_user_id")
    }

    val loggedInUserIdFlow: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[LOGGED_IN_USER_ID]
        }

    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_USER_ID] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(LOGGED_IN_USER_ID)
        }
    }
}
