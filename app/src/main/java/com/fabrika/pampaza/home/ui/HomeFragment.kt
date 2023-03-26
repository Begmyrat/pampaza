package com.fabrika.pampaza.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentHomeBinding
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.home.ui.adapter.MyPostAdapter
import com.fabrika.pampaza.home.viewmodel.HomeViewModel
import com.fabrika.pampaza.newpost.ui.NewPostActivity
import com.fabrika.pampaza.postDetail.ui.PostDetailActivity
import androidx.core.app.ActivityOptionsCompat
import com.fabrika.pampaza.common.ui.MyCustomDialog
import com.fabrika.pampaza.common.ui.MyCustomDialogType


class HomeFragment : Fragment(), BaseFragment {

    companion object{
        const val TAG = "HomeFragment"
    }

    lateinit var binding: FragmentHomeBinding
    lateinit var viewmodel: HomeViewModel
    private lateinit var adapterPost: MyPostAdapter
    lateinit var postList: MutableList<PostEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[HomeViewModel::class.java]
        MainActivity.viewmodel.isSplash = false
        postList = mutableListOf()
        adapterPost = MyPostAdapter(requireActivity() as MainActivity)
        binding.recPosts.adapter = adapterPost
        adapterPost.differ.submitList(postList)
        addListeners()
        addObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getAllPosts()
    }

    override fun addObservers() {
        viewmodel.allPosts.observe(this, Observer {
            Log.d("allPosts:", it.toString())
            postList = mutableListOf()
            postList.addAll(it)
            adapterPost.differ.submitList(postList)
            binding.swipeRefresh.isRefreshing = false
        })

        viewmodel.isLikeError.observe(this) {
            Log.d(TAG, "likeStatus: $it")
        }

        MainActivity.viewmodel.userEntity.observe(this) {
            it.let {
                adapterPost.notifyDataSetChanged()
            }
        }
    }

    override fun addListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            Log.d(TAG, "refresh")
            viewmodel.getAllPosts()
        }

        adapterPost.onCommentButtonClick = {
            Log.d(TAG, "commentClicked")

        }

        adapterPost.onRePostButtonClick = {
            var intent = Intent(requireContext(), NewPostActivity::class.java)
            intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_USER_NAME, it.authorName)
            intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_USER_ID, it.authorId)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_BODY, it.body)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_ID, it.id)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_DATE, it.date)
            intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_AVATAR_URL, it.authorAvatarUrl)
            startActivity(intent)
        }

        adapterPost.onLikeButtonClick = {
            Log.d(TAG, "likeClicked")
            viewmodel.likePost((requireActivity() as MainActivity), it)
        }

        adapterPost.onShareButtonClick = {
            Log.d(TAG, "shareClicked")
            MyCustomDialog(
                requireContext(),
                MyCustomDialogType.WARNING,
                "heheh fdsfds",
                "asjdkf sdflskd fsdkfs df sd",
                "ok",
                "cancel"
            ) {
                Log.d(TAG, "hehehehe")
            }.show()
        }

        adapterPost.onPostItemClicked = {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra(PostDetailActivity.POST_ID, it.id)
            intent.putExtra(PostDetailActivity.AUTHOR_ID, it.authorId)
            intent.putExtra(PostDetailActivity.AUTHOR_NAME, it.authorName)
            intent.putExtra(PostDetailActivity.POST_DATE, it.date)
            intent.putExtra(PostDetailActivity.POST_BODY, it.body)
            intent.putExtra(PostDetailActivity.POST_IMAGE_URL, it.imageUrl)
            intent.putExtra(PostDetailActivity.REPOST_COUNT, it.rePostCount)
            intent.putExtra(PostDetailActivity.LIKE_COUNT, it.likeCount)
            intent.putExtra(PostDetailActivity.AUTHOR_AVATAR_URL, it.authorAvatarUrl)
            intent.putExtra(PostDetailActivity.IS_LIKED, MainActivity.viewmodel.userEntity.value?.likedPosts?.contains(it.id) == true)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
        }

        adapterPost.onOriginalPostItemClicked = {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra(PostDetailActivity.POST_ID, it.originalPostId)
            intent.putExtra(PostDetailActivity.AUTHOR_NAME, it.originalPostAuthorName)
            intent.putExtra(PostDetailActivity.POST_DATE, it.originalPostDate)
            intent.putExtra(PostDetailActivity.POST_BODY, it.originalPostBody)
            intent.putExtra(PostDetailActivity.POST_IMAGE_URL, it.originalPostImageUrl)
            intent.putExtra(PostDetailActivity.REPOST_COUNT, it.originalPostRepostCount)
            intent.putExtra(PostDetailActivity.LIKE_COUNT, it.originalPostLikeCount)
            intent.putExtra(PostDetailActivity.AUTHOR_AVATAR_URL, it.originalPostAuthorAvatarUrl)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
        }
    }
}