package com.fabrika.pampaza.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.ActivityLoginBinding
import com.fabrika.pampaza.utils.extension.toDP
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
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

    fun showLoading(status: Boolean){
        binding.frameLoading.isVisible = status
    }
}