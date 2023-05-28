package com.fabrika.pampaza.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}