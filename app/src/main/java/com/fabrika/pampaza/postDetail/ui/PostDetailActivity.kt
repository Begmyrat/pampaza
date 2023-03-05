package com.fabrika.pampaza.postDetail.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.MainViewModel
import com.fabrika.pampaza.R
import com.fabrika.pampaza.postDetail.viewmodel.PostDetailActivityViewModel
import com.fabrika.pampaza.postDetail.viewmodel.PostDetailViewModel

class PostDetailActivity : AppCompatActivity() {

    companion object{
        const val POST_ID = "postId"
        const val AUTHOR_NAME = "authorName"
        const val AUTHOR_AVATAR_URL = "authorAvatarUrl"
        const val POST_DATE = "postDate"
        const val POST_BODY = "postBody"
        const val POST_IMAGE_URL = "postImageUrl"
        const val REPOST_COUNT = "repostCount"
        const val LIKE_COUNT = "likeCount"
    }

    lateinit var viewmodel: PostDetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        viewmodel = ViewModelProvider(this)[PostDetailActivityViewModel::class.java]
        viewmodel.getUser("GmBegmyrat", "123123")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
    }
}