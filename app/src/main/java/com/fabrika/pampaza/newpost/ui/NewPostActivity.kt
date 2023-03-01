package com.fabrika.pampaza.newpost.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fabrika.pampaza.R

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
}