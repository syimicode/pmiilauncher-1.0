package com.syimicode.pmiilauncher.fragment

import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.syimicode.pmiilauncher.R
import com.syimicode.pmiilauncher.databinding.FragmentBottomSheetBinding
import com.syimicode.pmiilauncher.utils.Constants
import java.io.IOException

//  BottomSheetDialogFragment() untuk menampilkan popup bottom setel wallpaper
class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        // MEMANGGIL FUNCTION initButton()
        initButton()

        return binding.root
    }

    // DEKLARASI FUNGSI MASING2 BUTTON KETIKA DI KLIK
    private fun initButton() {
        binding.linearHomescreen.setOnClickListener { setAsBackground(Constants.Background.HomeScreen) }
        binding.linearLockscreen.setOnClickListener { setAsBackground(Constants.Background.LockScreen) }
        binding.linearClose.setOnClickListener { dismiss() }
    }

    // DEKLARASI SET WALLPAPER
    private fun setAsBackground(LockOrHome: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context)
                val image = activity?.findViewById<ImageView>(R.id.finalWallpaper)

                if (image?.drawable == null) {
                    Toast.makeText(context, " Harap tunggu! ", Toast.LENGTH_SHORT).show()
                } else {
                    val bitmap = (image.drawable as BitmapDrawable).bitmap
                    // dismiss() Berfungsi agar setelah diklik BottomSheetDialogFragment ditutup
                    dismiss()
                    wallpaperManager.setBitmap(bitmap, null, true, LockOrHome)
                    Toast.makeText(context, " Berhasil! ", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                Toast.makeText(context, " Gagal! ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}