package com.fabrika.pampaza.postDetail.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.MainViewModel
import com.fabrika.pampaza.R
import com.fabrika.pampaza.postDetail.viewmodel.PostDetailActivityViewModel
import com.fabrika.pampaza.postDetail.viewmodel.PostDetailViewModel
import com.fabrika.pampaza.utils.extension.toDP
import com.google.android.material.snackbar.Snackbar

class PostDetailActivity : AppCompatActivity() {

    companion object{
        const val POST_ID = "postId"
        const val AUTHOR_ID = "authorId"
        const val AUTHOR_NAME = "authorName"
        const val AUTHOR_AVATAR_URL = "authorAvatarUrl"
        const val POST_DATE = "postDate"
        const val POST_BODY = "postBody"
        const val POST_IMAGE_URL = "postImageUrl"
        const val REPOST_COUNT = "repostCount"
        const val LIKE_COUNT = "likeCount"
        const val COMMENT_COUNT = "commentCount"
        const val IS_LIKED = "isLiked"
        const val IS_COMMENT_BUTTON_CLICKED = "isCommentButtonClicked"
        const val RESULT_CODE = 1324
    }

    lateinit var viewmodel: PostDetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        viewmodel = ViewModelProvider(this)[PostDetailActivityViewModel::class.java]
    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(LIKE_COUNT, viewmodel.likeCount.value)
        data.putExtra(COMMENT_COUNT, viewmodel.commentCount.value)
        super.setResult(RESULT_CODE, data)
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