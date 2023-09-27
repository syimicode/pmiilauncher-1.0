package com.syimicode.pmiilauncher.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.syimicode.pmiilauncher.databinding.ActivityDetailWallpaperBinding
import com.syimicode.pmiilauncher.fragment.BottomSheetFragment
import com.syimicode.pmiilauncher.model.WallpaperModel
import dev.chrisbanes.insetter.applyInsetter

class DetailWallpaperActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailWallpaperBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  DEKLARASI UNTUK FIREBASE FIRESTORE DAN AUTH
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val userID = firebaseAuth.currentUser?.uid
        val dataWallpaperModel = intent.getSerializableExtra("wallpaper") as WallpaperModel

        //  MENERIMA DATA "LINK" GAMBAR DARI FRAGMENT HOME (melalui wallpaper adapter) KE ACTIVITY FINAL WALLPAPER
        Glide.with(this).load(dataWallpaperModel.link).into(binding.finalWallpaper)

        //  Deklarasi btnInfo agar membuka customDialog() yakni Activity Info
        binding.btnInfo.setOnClickListener {
            val i = Intent(this@DetailWallpaperActivity, InfoActivity::class.java)
            i.putExtra("wallpaper", dataWallpaperModel)
            startActivity(i)

            // Digunakan untuk menonaktifkan animasi slide activity ketika btnInfo diklik
            overridePendingTransition(0, 0)
        }

        //  Cek apakah data wallpaper yang ditampilkan sedang ada dalam daftar favorit milik pengguna atau tidak.
        //  Jika data wallpaper tersebut sudah terdapat dalam daftar favorit pengguna, maka btnFavorite akan di-set sebagai "checked"
        binding.btnFavorite.isChecked =
            dataWallpaperModel.checkFavorite == true && dataWallpaperModel.userId?.contains(userID) == true && dataWallpaperModel.userId != null

        binding.btnFavorite.setOnClickListener {
            // Cek apakah data wallpaper yang ditampilkan sedang ada dalam daftar favorit milik pengguna atau tidak.
            // Jika ya, maka wallpaper tersebut akan dihapus dari daftar favorit pengguna.
            if (dataWallpaperModel.checkFavorite == true && dataWallpaperModel.userId?.contains(
                    userID) == true
            ) {
                firestore.collection("wallpaper")
                    .document(dataWallpaperModel.id!!)
                    //  Update data wallpaper yang telah dihapus dari daftar favorit pengguna pada database Firestore.
                    .update("checkFavorite",
                        removeFavorite(userID!!, dataWallpaperModel.userId!!),
                        "userId",
                        FieldValue.arrayRemove(userID))
                    .addOnSuccessListener {
                        //  Setelah berhasil dihapus, data wallpaper tersebut akan diberi tanda "checkFavorite" = false.
                        dataWallpaperModel.checkFavorite = false
                        //  Tampilkan pesan "Dihapus dari Favorit!" jika proses penghapusan berhasil
                        Toast.makeText(this, " Dihapus dari Favorit! ", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        Toast.makeText(this, " Terjadi kesalahan! ", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                //  Kode program ini digunakan untuk menambahkan id pengguna ke dalam array temporary "tempArray".
                val tempArray: ArrayList<String> = arrayListOf()
                tempArray.add(userID!!)

                //  Kode program ini digunakan untuk menambahkan id pengguna yang sudah ada di daftar favorit ke dalam "tempArray".
                for (i in dataWallpaperModel.userId!!) {
                    tempArray.add(i)
                }

                firestore.collection("wallpaper")
                    .document(dataWallpaperModel.id!!)
                    //  update data wallpaper yang ditambahkan ke dalam daftar favorit pengguna pada database Firestore.
                    .update("checkFavorite", true, "userId", tempArray)
                    .addOnSuccessListener {
                        //  Setelah berhasil ditambahkan, data wallpaper tersebut akan diberi tanda "checkFavorite" = true.
                        dataWallpaperModel.checkFavorite = true
                        //  Tampilkan pesan "Disimpan ke Favorit!" jika proses penambahan berhasil
                        Toast.makeText(this, " Disimpan ke Favorit! ", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, " Terjadi kesalahan! ", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        //  Deklarasi Tombol SETEL WALLPAPER AGAR MENAMPILKAN BOTTOM SHEET
        val bottomSheet = BottomSheetFragment()
        binding.btnSetWallpaper.setOnClickListener {
            bottomSheet.show(supportFragmentManager, "bottomSheet")
        }

        //  Deklarasi btnBack agar kembali ke Fragment Home
        binding.btnBack.setOnClickListener {
            finish()
        }


        //  Memanggil function setFullScreen Status Bar untuk Activity Final Wallpaper
        setFullScreen(window)

        //  Memperbaiki tampilan setFullScreen Status Bar yang Overlaps
        binding.apply {
            toolbar4.applyInsetter {
                type(statusBars = true) {
                    margin()
                }
            }
            btnSetWallpaper.applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }
        }

    }

    private fun removeFavorite(myId: String, userId: List<String>): Boolean {
        //  Jika userId mengandung nilai "myId" dan jumlah elemen dalam userId sama dengan 1, maka fungsi akan mengembalikan nilai false.
        if (userId.contains(myId) && userId.size == 1) {
            return false

            //  Jika userId mengandung nilai "myId" dan jumlah elemen dalam userId lebih dari 1, maka fungsi akan mengembalikan nilai true.
        } else if (userId.contains(myId) && userId.size > 1) {
            return true
        }

        //  Jika kedua kondisi sebelumnya tidak terpenuhi. Fungsi akan mengembalikan nilai false.
        return false
    }

    //  Deklarasi Full Screen Status Bar untuk Activity Final Wallpaper
    fun setFullScreen(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    //  Deklarasi ketika tombol kembali (default) smartphone diklik agar kembali ke Fragment Home
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}