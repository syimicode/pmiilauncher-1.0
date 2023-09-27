package com.syimicode.pmiilauncher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class BaseApp : Application() {

    private val pref by lazy { SharedPrefDarkMode(this) }

//    BaseApp digunakan untuk meng Create sebelum activity di Create
    override fun onCreate() {
        super.onCreate()
        when(pref.getBoolean("pref_is_dark")){
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}