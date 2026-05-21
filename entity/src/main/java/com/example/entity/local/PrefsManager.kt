package com.example.entity.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("zoomrad_prefs", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("access_token", null)
        set(value) = prefs.edit { putString("access_token", value) }

    var appLanguage: String
        get() = prefs.getString("app_language","uz")?: "uz"
        set(value) = prefs.edit { putString("app_language",value) }


    var refreshToken: String?
        get() = prefs.getString("refresh_token", null)
        set(value) = prefs.edit { putString("refresh_token", value) }

    var isNewUser: Boolean
        get() = prefs.getBoolean("is_new_user", false)
        set(value) = prefs.edit { putBoolean("is_new_user", value) }

    var appPassword: String?
        get() = prefs.getString("app_password", null)
        set(value) = prefs.edit { putString("app_password", value) }

    fun clear() {
        prefs.edit { clear() }
    }
}
