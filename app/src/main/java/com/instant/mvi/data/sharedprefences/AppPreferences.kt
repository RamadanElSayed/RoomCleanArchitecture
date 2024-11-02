package com.instant.mvi.data.sharedprefences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {

    // Save Boolean preference
    fun saveBooleanPreference(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    // Save String preference
    fun saveStringPreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Save Int preference
    fun saveIntPreference(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    // Save Float preference
    fun saveFloatPreference(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    // Retrieve Boolean preference
    fun getBooleanPreference(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Retrieve String preference
    fun getStringPreference(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // Retrieve Int preference
    fun getIntPreference(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Retrieve Float preference
    fun getFloatPreference(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    // Method to clear all preferences (optional)
    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}

