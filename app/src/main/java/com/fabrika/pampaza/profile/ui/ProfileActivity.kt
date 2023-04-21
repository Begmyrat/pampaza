package com.fabrika.pampaza.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.ActivityMainBinding
import com.fabrika.pampaza.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}