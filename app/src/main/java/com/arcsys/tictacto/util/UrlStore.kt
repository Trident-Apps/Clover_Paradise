package com.arcsys.tictacto.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "urlStorage")


suspend fun insertIntoDs(url: String, context: Context) {
    context.dataStore.edit {
        it[KEYS.dataStoreKey] = url
    }
}

suspend fun getFromDs(context: Context): String? {
    return context.dataStore.data.first()[KEYS.dataStoreKey]
}

object KEYS {
    val dataStoreKey = stringPreferencesKey("last_url")
}