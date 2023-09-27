package com.syimicode.pmiilauncher.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.syimicode.pmiilauncher.R
import com.syimicode.pmiilauncher.activity.DetailWallpaperActivity
import com.syimicode.pmiilauncher.model.WallpaperModel

class WallpaperAdapter(
    val requireContext: Context,
    val listWallpaper: ArrayList<WallpaperModel>,
) : RecyclerView.Adapter<WallpaperAdapter.wallpaperViewHolder>() {

    inner class wallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.wallpaperImage)
    }

    //  onCreateViewHolder yang bertugas untuk memanggil layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wallpaperViewHolder {
        return wallpaperViewHolder(
            LayoutInflater.from(requireContext).inflate(R.layout.item_wallpaper, parent, false)
        )
    }

    //  onBindViewHolder yang bertugas untuk memberikan data pada masing2 item
    override fun onBindViewHolder(holder: wallpaperViewHolder, position: Int) {
        //   Aktifkan Cache Disk, agar gambar dimuat lebih cepat
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        
        //  Mengirim data link (gambar wallpaper) ke fragment home
        Glide.with(requireContext)
            .asBitmap()
            .load(listWallpaper[position].link)
            .apply(requestOptions)
            .centerCrop()
            .transition(BitmapTransitionOptions.withCrossFade(80))
            .into(holder.imageView)

        //  Mengirim semua data (wallpaper model) dari fragment home ke activity final wallpaper
        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailWallpaperActivity::class.java)
            intent.putExtra("wallpaper", listWallpaper[position])
            requireContext.startActivity(intent)
        }
    }

    //  getItemCount mengatur berapa jumlah data yang akan di tampilkan
    override fun getItemCount() = listWallpaper.size
}