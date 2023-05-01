package com.fabrika.pampaza.postDetail.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.utils.extension.toDateString
import com.fabrika.pampaza.databinding.FragmentPostDetailBinding
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.newpost.ui.NewPostActivity
import com.fabrika.pampaza.postDetail.ui.adapter.MyCommentAdapter
import com.fabrika.pampaza.postDetail.viewmodel.PostDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PostDetailFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentPostDetailBinding
    private lateinit var viewmodel: PostDetailViewModel
    private lateinit var adapterComments: MyCommentAdapter
    private lateinit var commentList: MutableList<PostEntity>
    private val delayTimeToOpenKeyboard = 750L

    private val postId: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.POST_ID)
    }
    private val authorId: String? by lazy {
        (requireActivity()).intent.extras?.getString(PostDetailActivity.AUTHOR_ID)
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
    private val isLiked: Boolean? by lazy {
        (requireActivity()).intent.extras?.getBoolean(PostDetailActivity.IS_LIKED)
    }
    private val isCommentButtonClicked: Boolean? by lazy {
        (requireActivity()).intent.extras?.getBoolean(PostDetailActivity.IS_COMMENT_BUTTON_CLICKED, false)
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
            Glide.with(requireContext()).load(authorAvatarUrl).centerCrop().into(binding.iAvatar)
            Glide.with(requireContext()).load(postImageUrl).into(binding.iBody)
            binding.tUsername.text = authorName.toString()
            binding.tDate.text = postDate.toDateString()
            binding.tBody.text = postBody.toString()
            binding.tLikeCount.text = likeCount.toString()
            binding.tCommentCount.text = "0"
            binding.tRetweetCount.text = repostCount.toString()
            viewmodel.isLiked.postValue(isLiked)
            viewmodel.likeCount.postValue(likeCount)
        }

        if(isCommentButtonClicked == true){
            lifecycleScope.launch{
                delay(delayTimeToOpenKeyboard)
                binding.tiComment.requestFocus()
                showKeyboard()
            }
        }
    }

    override fun addObservers() {
        viewmodel.allComments.observe(this) {
            Log.d("allComments for $postId:", it.toString())
            commentList = mutableListOf()
            commentList.addAll(it)
            adapterComments.differ.submitList(commentList)
            binding.tCommentCount.text = "${it.size}"
            (requireActivity() as? PostDetailActivity)?.viewmodel?.likeCount?.value = it.size.toLong()
        }

        viewmodel.likeCount.observe(this){
            binding.tLikeCount.text = "$it"
        }

        MainActivity.viewmodel.userEntity.observe(this) {
            val status = MainActivity.viewmodel.userEntity.value?.likedPosts?.contains(postId) == true
            binding.iLike.setImageDrawable(ContextCompat.getDrawable(requireContext(), if(status) R.drawable.ic_heard_filled else R.drawable.ic_heart))
            if(viewmodel.isLiked.value == false && status){
                viewmodel.likeCount.value = viewmodel.likeCount.value?.plus(1)
            } else if(viewmodel.isLiked.value == true && status){
                viewmodel.likeCount.value = viewmodel.likeCount.value?.minus(1)
            }
        }

        viewmodel.isPostCommentError.observe(this){
            if(it){
                binding.tiComment.setText("")
                binding.tiComment.clearFocus()
                hideKeyboard()
                postId?.let { it1 -> viewmodel.getComments(it1) }
            }
            (requireActivity() as? PostDetailActivity)?.showSnackbar(binding.iBack, if(it) getString(R.string.post_comment_success) else getString(R.string.post_comment_failure), it)
        }

        binding.tiComment.doOnTextChanged { text, _, _, _ ->
            binding.tlComment.endIconDrawable = if(text?.isEmpty() == false) ContextCompat.getDrawable(requireContext(), R.drawable.ic_send) else null
        }
    }

    override fun addListeners() {
        binding.iBack.setOnClickListener(this)
        binding.lComment.setOnClickListener(this)
        binding.lRetweet.setOnClickListener(this)
        binding.lLike.setOnClickListener(this)
        binding.tlComment.setEndIconOnClickListener {
            postId?.let { viewmodel.postComment(it, binding.tiComment.text.toString(), commentList.size.toLong()) }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.i_back -> {
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
            }
            R.id.l_comment -> {
                binding.tiComment.requestFocus()
                showKeyboard()
            }
            R.id.l_retweet -> {
                val intent = Intent(requireContext(), NewPostActivity::class.java)
                intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_USER_NAME, authorName)
                intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_USER_ID, authorId)
                intent.putExtra(NewPostActivity.ORIGINAL_POST_BODY, postBody)
                intent.putExtra(NewPostActivity.ORIGINAL_POST_ID, postId)
                intent.putExtra(NewPostActivity.ORIGINAL_POST_DATE, postDate)
                intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_AVATAR_URL, authorAvatarUrl)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
            }
            R.id.l_like -> {
                postId?.let { MainActivity.viewmodel.likePost(it) }
            }
        }
    }

    private fun showKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(binding.tiComment, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(){
        val view: View = binding.tiComment
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}