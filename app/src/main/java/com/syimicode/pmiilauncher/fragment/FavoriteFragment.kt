package com.syimicode.pmiilauncher.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.syimicode.pmiilauncher.adapter.WallpaperAdapter
import com.syimicode.pmiilauncher.databinding.FragmentFavoriteBinding
import com.syimicode.pmiilauncher.model.WallpaperModel

class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    //  DEKLARASI UNTUK LAYOUT TERSIMPAN DAN BELUM TERSIMPAN
    private lateinit var layoutWallpaperTersimpan: RelativeLayout
    private lateinit var layoutWallpaperBelumTersimpan: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        //  DEKLARASI UNTUK FIREBASE FIRESTORE DAN AUTH
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        //  DEKLARASI UNTUK LAYOUT TERSIMPAN DAN BELUM TERSIMPAN
        layoutWallpaperTersimpan = binding.layoutTersimpan
        layoutWallpaperBelumTersimpan = binding.layoutBelumTersimpan

        //  Dapatkan userID dari currentUser pada objek FirebaseAuth dan menyimpannya pada variabel "userID".
        val userID = firebaseAuth.currentUser?.uid

        //  Memanggil fungsi "listFavoriteWallpaper" jika userID tidak null.
        //  Fungsi ini akan mengambil semua wallpaper yang disimpan di Firestore dengan flag "checkFavorite" bernilai true dan userId yang sama dengan nilai "id" yang diberikan.
        userID?.let { listFavoriteWallpaper(it) }

        return binding.root
    }

    private fun listFavoriteWallpaper(id: String){
        //  Memanggil metode "collection" pada objek Firestore untuk mengambil semua dokumen dari koleksi "wallpaper"
        //  yang memenuhi kondisi "checkFavorite" bernilai true dan "userId" yang sama dengan nilai "id" yang diberikan.
        firestore.collection("wallpaper")
            .whereEqualTo("checkFavorite", true)
            .whereArrayContains("userId", id)
            .addSnapshotListener { value, _ ->
                //  Buat sebuah ArrayList kosong dengan nama "listWallpaper" yang akan digunakan untuk menampung wallpaper yang diterima dari Firestore.
                val listWallpaper = arrayListOf<WallpaperModel>()

                //  Mengkonversi data yang diterima dari Firestore menjadi list of object dengan menggunakan kelas "WallpaperModel".
                val data = value?.toObjects(WallpaperModel::class.java)

                if (data != null) {
                    //  DEKLARASI JIKA ADA WALLPAPER YANG DISIMPAN MAKA TAMPILKAN LAYOUT
                    if (data.size > 0) {
                        layoutWallpaperTersimpan.visibility = View.VISIBLE
                        layoutWallpaperBelumTersimpan.visibility = View.GONE
                        listWallpaper.addAll(data)

                        binding.rcvFavorite.layoutManager =
                            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

                        binding.rcvFavorite.adapter =
                            context?.let { WallpaperAdapter(it, listWallpaper) }

                    } else {
                        //  DEKLARASI JIKA TIDAK ADA WALLPAPER YANG TERSIMPAN MAKA TAMPILKAN LAYOUT LOTTIE
                        layoutWallpaperBelumTersimpan.visibility = View.VISIBLE
                        layoutWallpaperTersimpan.visibility = View.GONE
                    }
                }
            }
    }
}