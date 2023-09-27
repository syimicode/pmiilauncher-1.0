package com.syimicode.pmiilauncher.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.syimicode.pmiilauncher.adapter.WallpaperAdapter
import com.syimicode.pmiilauncher.databinding.FragmentHomeBinding
import com.syimicode.pmiilauncher.model.WallpaperModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var database: FirebaseFirestore

    //  DEKLARASI UNTUK LAYOUT NO INTERNET ACCESS
    private lateinit var internetLayout: RelativeLayout
    private lateinit var noInternetLayout: LinearLayout
    private lateinit var btnCobaLagi: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        //  DEKLARASI UNTUK FIREBASE FIRESTORE
        database = FirebaseFirestore.getInstance()

        database.collection("wallpaper").addSnapshotListener { value, _ ->
            val listWallpaper = arrayListOf<WallpaperModel>()
            val data = value?.toObjects(WallpaperModel::class.java)
            if (data != null) {
                listWallpaper.addAll(data)
            }

            binding.rcvWallpaper.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

            binding.rcvWallpaper.adapter =
                context?.let { WallpaperAdapter(it, listWallpaper) }
        }

        //  DEKLARASI UNTUK LAYOUT NO INTERNET ACCESS
        internetLayout = binding.layoutConnected
        noInternetLayout = binding.layoutDisconnected
        btnCobaLagi = binding.btnCobaLagi

        //  MEMANGGIL FUNCTION DRAW LAYOUT
        drawLayout()

        //  DEKLARASI UNTUK btnCobaLagi
        btnCobaLagi.setOnClickListener {
            drawLayout()
        }
        return binding.root
    }

    //  DEKLARASI UNTUK MEMERIKSA KONEKSI INTERNET
    private fun isNetworkAvailable(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }

    //  DEKLARASI JIKA JARINGAN INTERNET TERSEDIA
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            internetLayout.visibility = View.VISIBLE
            noInternetLayout.visibility = View.GONE

            //  DEKLARASI JIKA JARINGAN INTERNET TIDAK TERSEDIA
        } else {
            noInternetLayout.visibility = View.VISIBLE
            internetLayout.visibility = View.GONE
        }
    }
}