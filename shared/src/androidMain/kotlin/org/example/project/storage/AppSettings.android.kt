package org.example.project.storage

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.context.GlobalContext

actual class AppSettings actual constructor() {
    private val context: Context = GlobalContext.get().get()
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    actual fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    actual fun clear() {
        prefs.edit().clear().apply()
    }
}
