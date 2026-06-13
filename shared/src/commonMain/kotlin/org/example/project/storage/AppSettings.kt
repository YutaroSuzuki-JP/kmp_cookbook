package org.example.project.storage

expect class AppSettings() {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
    
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    
    fun remove(key: String)
    fun clear()
}
