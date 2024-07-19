package com.syimicode.pmiilauncher.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.syimicode.pmiilauncher.R
import com.syimicode.pmiilauncher.databinding.ActivityInfoBinding
import com.syimicode.pmiilauncher.model.WallpaperModel

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

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