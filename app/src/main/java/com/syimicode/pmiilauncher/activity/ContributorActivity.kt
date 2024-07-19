package com.syimicode.pmiilauncher.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.syimicode.pmiilauncher.databinding.ActivityContributorBinding
import com.syimicode.pmiilauncher.model.UserModel
import com.syimicode.pmiilauncher.utils.Config
import com.syimicode.pmiilauncher.utils.Config.hideDialog
import java.io.ByteArrayOutputStream

class ContributorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContributorBinding

    private var imageUri: Uri? = null

    //  PROSES & MEMERIKSA APA GAMBAR YANG DIPILIH SAAT KLIK btnUnggahWallpaper
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it

        //  PROSES MENAMPILKAN GAMBAR YANG TELAH DIPILIH DARI GALERI (MENGGUNAKAN LIBRARY GLIDE)
        Glide.with(this).load(imageUri).into(binding.btnPreview)

        //  AGAR btnUnggahWallpaper BERADA DIBAWAH DAN btnPreview MENINDIH DIATASNYA
        binding.btnUnggahWallpaper.alpha = 0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContributorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  DEKLARASI AGAR btnUnggahWallpaper MENGAKSES GALERI (menggunakan Implicit Intent)
        binding.btnUnggahWallpaper.setOnClickListener {
            selectImage.launch("image/*")
        }

        //  DEKLARASI AGAR btnKirim MENGIRIM DATA KE FIREBASE
        binding.btnKirim.setOnClickListener {
            validateData()
        }
    }

    //  DEKLARASI AGAR SETIAP ITEM WAJIB DIISI SEBELUM KLIK btnKirim
    private fun validateData() {
        if (binding.userKontributor.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userSekretariat.text.toString().isEmpty()
            || imageUri == null
        ) {
            Toast.makeText(this, " Lengkapi data Anda! ", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        //  DEKLARASI UNTUK FIREBASE STORAGE
        val filename = binding.userKontributor.text.toString()
        val storageRef = FirebaseStorage.getInstance().getReference("Kontributor/$filename")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                // Membuat bitmap dari file yang dipilih
                val originalImageSize = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

                // Mengkompres originalImageSize menjadi JPEG dengan kualitas 75% dan menyimpannya dalam sebuah ByteArrayOutputStream
                val baos = ByteArrayOutputStream()
                originalImageSize.compress(Bitmap.CompressFormat.JPEG, 75, baos)
                val data = baos.toByteArray()

                // Mengupload gambar tersebut
                val uploadImageCompress = storageRef.putBytes(data)
                uploadImageCompress.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this, " Terjadi kesalahan, coba lagi! ", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                hideDialog()
                Toast.makeText(this, " Terjadi kesalahan, coba lagi! ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: UploadTask.TaskSnapshot) {

        val data = UserModel(
            image = imageUrl.toString(),
            kontributor = binding.userKontributor.text.toString(),
            email = binding.userEmail.text.toString(),
            sekretariat = binding.userSekretariat.text.toString(),
        )

        //  DEKLARASI UNTUK FIREBASE REALTIME DATABASE
        val uid = binding.userKontributor.text.toString()
        FirebaseDatabase.getInstance().getReference("Kontributor/$uid")
            .setValue(data).addOnCompleteListener {

                hideDialog()
                Toast.makeText(this, " Karya berhasil diupload! ", Toast.LENGTH_SHORT).show()
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, " Upload Gagal! ", Toast.LENGTH_SHORT).show()
                }
            }
    }
}