package com.fabrika.pampaza.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.ActivityMainBinding
import com.fabrika.pampaza.databinding.ActivityProfileBinding
import com.fabrika.pampaza.utils.extension.toDP
import com.google.android.material.snackbar.Snackbar

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showSnackbar(view: View, title: String, status: Boolean){
        val snackbar = Snackbar.make(view, title, Snackbar.LENGTH_SHORT)

        val params = snackbar.view.layoutParams as FrameLayout.LayoutParams

        params.setMargins(
            20.toDP(this),
            20.toDP(this),
            20.toDP(this),
            3.toDP(this),
        )

        snackbar.view.layoutParams = params
        snackbar.setBackgroundTint(ContextCompat.getColor(this, if(status) R.color.green_success else R.color.red_violet))
        snackbar.show()
    }
}