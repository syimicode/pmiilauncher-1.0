package com.syimicode.pmiilauncher.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.syimicode.pmiilauncher.SharedPrefDarkMode
import com.syimicode.pmiilauncher.activity.AboutActivity
import com.syimicode.pmiilauncher.activity.ContributorActivity
import com.syimicode.pmiilauncher.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val pref by lazy { SharedPrefDarkMode(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//      Deklarasi untuk element Linear About agar pindah halaman menggunakan Explicit Intent
        binding.linearContributor.setOnClickListener {
            val i = Intent(this@SettingsFragment.requireContext(), ContributorActivity::class.java)
            startActivity(i)
        }

//      Mengganti warna Switch Button saat di klik mode gelap/terang
        binding.switchMode.isChecked = pref.getBoolean("pref_is_dark")

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
//            sharedPrefs digunakan untuk menyimpan data
            pref.put("change_ui_mode", true)

            when (isChecked) {
                true -> {
//                pref menyimpan pref_is_dark true maka terjadi mode gelap
                    pref.put("pref_is_dark", true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false -> {
//                pref menyimpan pref_is_dark false maka terjadi mode terang
                    pref.put("pref_is_dark", false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

//      Deklarasi untuk element Linear Rating agar pindah halaman menggunakan Implicit Intent
        val packageName =
            "com.syimicode.pmiilauncher" // Ganti dengan package name aplikasi Anda di Play Store
        binding.linearRating.setOnClickListener {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                startActivity(intent)
            }
        }

//      Deklarasi untuk element Linear Share agar pindah halaman menggunakan Implicit Intent
        binding.linearShare.setOnClickListener {
            val packageApp =
                "com.syimicode.pmiilauncher" // Ganti dengan package name aplikasi Anda di Play Store
            val playStoreLink =
                "Hai, sahabat/i udah cobain PMII Launcher belum? Kayaknya kamu bakal suka.. Yuk, install aplikasinya disini:\n\nhttps://play.google.com/store/apps/details?id=$packageApp"

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, playStoreLink)
                type = "text/plain"
            }
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Bagikan Aplikasi")
            sendIntent.putExtra(Intent.EXTRA_TITLE, "PMII Launcher")
            startActivity(Intent.createChooser(sendIntent, "Bagikan Aplikasi"))
        }

//      Deklarasi untuk element Linear About agar pindah halaman menggunakan Explicit Intent
        binding.linearAbout.setOnClickListener {
            val i = Intent(this@SettingsFragment.requireContext(), AboutActivity::class.java)
            startActivity(i)
        }
    }
}