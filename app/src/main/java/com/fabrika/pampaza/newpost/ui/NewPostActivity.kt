package com.fabrika.pampaza.newpost.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.fabrika.pampaza.R
import com.fabrika.pampaza.utils.extension.toDP
import com.google.android.material.snackbar.Snackbar

class NewPostActivity : AppCompatActivity() {
    companion object {
        const val ORIGINAL_POST_ID = "OriginalPostId"
        const val ORIGINAL_AUTHOR_USER_ID = "OriginalAuthorId"
        const val ORIGINAL_AUTHOR_USER_NAME = "OriginalAuthorName"
        const val ORIGINAL_AUTHOR_AVATAR_URL = "OriginalAuthorAvatarUrl"
        const val ORIGINAL_POST_BODY = "OriginalPostBody"
        const val ORIGINAL_POST_DATE = "OriginalPostDate"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
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
}