package com.syimicode.pmiilauncher

import android.content.Context
import android.content.SharedPreferences

class SharedPrefDarkMode(context: Context) {

    private val Prefs_Name = "DarkMode.pref"
    private var sharedPref: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(Prefs_Name, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }
}