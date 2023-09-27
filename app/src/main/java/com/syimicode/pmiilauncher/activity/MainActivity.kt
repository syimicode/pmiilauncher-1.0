package com.syimicode.pmiilauncher.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.syimicode.pmiilauncher.R
import com.syimicode.pmiilauncher.SharedPrefDarkMode
import com.syimicode.pmiilauncher.databinding.ActivityMainBinding
import com.syimicode.pmiilauncher.fragment.FavoriteFragment
import com.syimicode.pmiilauncher.fragment.HomeFragment
import com.syimicode.pmiilauncher.fragment.SettingsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAnalytics instance
        analytics = FirebaseAnalytics.getInstance(this)

        // Inisialisasi FirebaseAuthentication anonymous
        auth = FirebaseAuth.getInstance()

//        ATUR FRAGMENT SATU SEBAGAI FRAGMENT YANG MUNCUL PERTAMA KALI SEBELUM USER MEMILIH MENU PADA NAVIGATION
        changeFragment(HomeFragment())

//        MEMBUAT SELECTED LISTENER UNTUK MENGKONDISIKAN USER KETIKA SALAH SATU ITEM DI KLIK
        binding.bottomMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
//                    Tampilkan Fragment Satu
                    changeFragment(HomeFragment())
                }
                R.id.favorite -> {
//                    Tampilkan Fragment Dua
                    changeFragment(FavoriteFragment())
                }
                R.id.settings -> {
//                    Tampilkan Fragment Tiga
                    changeFragment(SettingsFragment())
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }


    //    PROSES PERGANTIAN ANTAR FRAGMENT PADA ACTIVITY MAIN
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


    //    FITUR DARK MODE
    private val pref by lazy { SharedPrefDarkMode(this) }

    //    onResume Lifecycle Activity
    override fun onResume() {
        super.onResume()
        if (pref.getBoolean("change_ui_mode")) {
            binding.bottomMenu.selectedItemId = R.id.settings
            pref.put("change_ui_mode", false)
        }
    }

    //  Autentikasi pengguna pada aplikasi dengan menggunakan Firebase Authentication
    override fun onStart() {
        super.onStart()
        //  Periksa apakah pengguna masuk (non-null) dan perbarui UI yang sesuai
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    //  signInAnonymously digunakan untuk melakukan login anonim (tanpa login) ke Firebase Authentication
    private fun updateUI(currentUser: FirebaseUser?) {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //  Berhasil masuk, perbarui UI dengan informasi pengguna yang masuk
                    Log.d(TAG, "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    //  Jika proses masuk gagal, tampilkan pesan kepada pengguna.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
}