package com.fabrika.pampaza.newpost.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fabrika.pampaza.R

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
    }
}