
package com.example.levelupgamer.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("levelup_prefs")

object PrefKeys {
    val CURRENT_EMAIL = stringPreferencesKey("current_email")
    val POINTS = intPreferencesKey("points")
    val IS_DUOC = booleanPreferencesKey("is_duoc")
    val NAME = stringPreferencesKey("name")
    val PHOTO = stringPreferencesKey("photo")
}

class Prefs(private val context: Context) {

    val userFlow = context.dataStore.data.map { p ->
        val email = p[PrefKeys.CURRENT_EMAIL] ?: ""
        if (email.isBlank()) null else User(
            email = email,
            name = p[PrefKeys.NAME] ?: "",
            isDuoc = p[PrefKeys.IS_DUOC] ?: false,
            points = p[PrefKeys.POINTS] ?: 0,
            photoUri = p[PrefKeys.PHOTO]
        )
    }

    suspend fun saveUser(u: User) {
        context.dataStore.edit { e ->
            e[PrefKeys.CURRENT_EMAIL] = u.email
            e[PrefKeys.NAME] = u.name
            e[PrefKeys.IS_DUOC] = u.isDuoc
            e[PrefKeys.POINTS] = u.points
            u.photoUri?.let { e[PrefKeys.PHOTO] = it }
        }
    }

    suspend fun signOut() {
        context.dataStore.edit { e ->
            e.remove(PrefKeys.CURRENT_EMAIL)
            e.remove(PrefKeys.NAME)
            e.remove(PrefKeys.IS_DUOC)
            e.remove(PrefKeys.POINTS)
            e.remove(PrefKeys.PHOTO)
        }
    }
}
