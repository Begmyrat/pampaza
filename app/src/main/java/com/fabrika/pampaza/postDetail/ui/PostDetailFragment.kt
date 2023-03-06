package com.fabrika.pampaza.postDetail.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.common.utils.extensions.toDateString
import com.fabrika.pampaza.databinding.FragmentPostDetailBinding
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.postDetail.ui.adapter.MyCommentAdapter
import com.fabrika.pampaza.postDetail.viewmodel.PostDetailViewModel

class PostDetailFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentPostDetailBinding
    private lateinit var viewmodel: PostDetailViewModel
    private lateinit var adapterComments: MyCommentAdapter
    private lateinit var commentList: MutableList<PostEntity>

    private val postId: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.POST_ID)
    }
    private val authorName: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.AUTHOR_NAME)
    }
    private val authorAvatarUrl: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.AUTHOR_AVATAR_URL)
    }
    private val postDate: Long? by lazy {
        (requireActivity()).intent.extras?.getLong(PostDetailActivity.POST_DATE)
    }
    private val postBody: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.POST_BODY)
    }
    private val postImageUrl: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.POST_IMAGE_URL)
    }
    private val repostCount: Long? by lazy {
        (requireActivity()).intent.extras?.getLong(PostDetailActivity.REPOST_COUNT)
    }
    private val likeCount: Long? by lazy {
        (requireActivity()).intent.extras?.getLong(PostDetailActivity.LIKE_COUNT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel = ViewModelProvider(this)[PostDetailViewModel::class.java]
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        commentList = mutableListOf()
        adapterComments = MyCommentAdapter(requireActivity() as PostDetailActivity)
        binding.recComments.adapter = adapterComments
        adapterComments.differ.submitList(commentList)
        addListeners()
        addObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postId?.let {
            viewmodel.getComments(it)
            Glide.with(requireContext()).load(authorAvatarUrl).circleCrop().into(binding.iAvatar)
            binding.tUsername.text = authorName.toString()
            binding.tDate.text = postDate.toDateString()
            binding.tBody.text = postBody.toString()
            binding.tLikeCount.text = likeCount.toString()
            binding.tRetweetCount.text = repostCount.toString()
        }
    }

    override fun addObservers() {
        viewmodel.allComments.observe(this) {
            Log.d("allComments for $postId:", it.toString())
            commentList = mutableListOf()
            repeat(20){x ->
                commentList.addAll(it)
            }
            adapterComments.differ.submitList(commentList)
        }

//        (requireActivity() as? PostDetailActivity)?.viewmodel?.userEntity?.observe(this) {
//            it.let {
//
//            }
//        }
    }

    override fun addListeners() {
        binding.iBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.i_back -> {
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
            }
        }
    }
}