package com.syimicode.pmiilauncher.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.model.Document
import com.google.firebase.firestore.model.ObjectValue
import com.syimicode.pmiilauncher.R
import com.syimicode.pmiilauncher.databinding.ActivityInfoBinding
import com.syimicode.pmiilauncher.model.WallpaperModel
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoBinding

    //  DEKLARASI UNTUK TEXTVIEW
    private lateinit var tvInputKontributor: TextView
    private lateinit var tvInputSekretariat: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("wallpaper") as WallpaperModel

        //  DEKLARASI UNTUK MEMANGGIL ID TEXTVIEW INPUT
        tvInputKontributor = findViewById(R.id.inputKontributor)
        tvInputSekretariat = findViewById(R.id.inputSekretariat)

        //  DEKLARASI UNTUK MENGAMBIL DATA WALLPAPER MODEL DAN DILETAKKAN KE DALAM ACTIVITY INFO
        tvInputKontributor.text = data.name
        tvInputSekretariat.text = data.sekretariat
    }

    override fun onPause() {
        super.onPause()
        // Digunakan untuk menonaktifkan animasi slide activity ketika tombol kembali (default) smartphone diklik
        overridePendingTransition(0, 0)
    }
}