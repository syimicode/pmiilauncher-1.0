package com.syimicode.pmiilauncher.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.syimicode.pmiilauncher.R
import com.syimicode.pmiilauncher.databinding.ActivityAboutBinding
import com.syimicode.pmiilauncher.databinding.ActivityContributorBinding

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}