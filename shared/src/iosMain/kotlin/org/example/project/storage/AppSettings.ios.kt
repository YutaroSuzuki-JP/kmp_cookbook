package org.example.project.storage

import platform.Foundation.NSUserDefaults

actual class AppSettings actual constructor() {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun putString(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }

    actual fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, forKey = key)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        // Since standardUserDefaults.objectForKey returns nil if not present,
        // we can check if the key exists before returning, or fall back to default
        if (userDefaults.objectForKey(key) == null) {
            return defaultValue
        }
        return userDefaults.boolForKey(key)
    }

    actual fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    actual fun clear() {
        val dictionary = userDefaults.dictionaryRepresentation()
        dictionary.keys.forEach { key ->
            if (key is String) {
                userDefaults.removeObjectForKey(key)
            }
        }
    }
}
