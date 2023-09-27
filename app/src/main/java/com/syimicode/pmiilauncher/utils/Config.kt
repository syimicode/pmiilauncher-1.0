package com.syimicode.pmiilauncher.utils

import android.app.AlertDialog
import android.content.Context
import com.syimicode.pmiilauncher.R

object Config {

    //  DEKLARASI UNTUK LOADING LAYOUT
    private var dialog: AlertDialog? = null

    fun showDialog(context: Context) {
        dialog = AlertDialog.Builder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        dialog!!.show()
    }

    fun hideDialog() {
        dialog!!.dismiss()
    }

}