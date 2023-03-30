package com.fabrika.pampaza.newpost.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentNewPostBinding
import com.fabrika.pampaza.newpost.model.PublicityType
import com.fabrika.pampaza.newpost.viewmodel.NewPostViewModel

class NewPostFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentNewPostBinding
    private lateinit var viewmodel: NewPostViewModel
    var image = ""

    private val originalAuthorName: String? by lazy {
        (requireActivity()).intent.extras?.getString(NewPostActivity.ORIGINAL_AUTHOR_USER_NAME)
    }
    private val originalAuthorId: String? by lazy {
        (requireActivity()).intent.extras?.getString(NewPostActivity.ORIGINAL_AUTHOR_USER_ID)
    }
    private val originalPostBody: String? by lazy {
        (requireActivity()).intent.extras?.getString(NewPostActivity.ORIGINAL_POST_BODY)
    }
    private val originalPostImageUrl: String? by lazy {
        (requireActivity()).intent.extras?.getString(NewPostActivity.ORIGINAL_POST_IMAGE_URL)
    }
    private val originalPostId: String? by lazy {
        (requireActivity()).intent.extras?.getString(NewPostActivity.ORIGINAL_POST_ID)
    }
    private val originalPostDate: Long? by lazy {
        (requireActivity()).intent.extras?.getLong(NewPostActivity.ORIGINAL_POST_DATE)
    }
    private val originalAuthorAvatarUrl: String? by lazy {
        (requireActivity()).intent.extras?.getString(NewPostActivity.ORIGINAL_AUTHOR_AVATAR_URL)
    }
    private val originalPostRepostCount: Long? by lazy {
        (requireActivity()).intent.extras?.getLong(NewPostActivity.ORIGINAL_POST_REPOST_COUNT)
    }
    private val originalPostLikeCount: Long? by lazy {
        (requireActivity()).intent.extras?.getLong(NewPostActivity.ORIGINAL_POST_LIKE_COUNT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this)[NewPostViewModel::class.java]
        binding = FragmentNewPostBinding.inflate(layoutInflater)
        addListeners()
        addObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardRepost.visibility = View.GONE
        originalAuthorId?.let{
            binding.cardRepost.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(originalAuthorAvatarUrl.toString())
                .circleCrop()
                .into(binding.iRepostAvatar)
            binding.tRepostUsername.text = originalAuthorName.toString()
            binding.tRepostId.text = it
            binding.tRepostBody.text = originalPostBody.toString()
        }
    }

    override fun addObservers() {
        viewmodel.isError.observe(this) { success ->
            if (success){
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
            } else{
                (requireActivity() as? NewPostActivity)?.showSnackbar(binding.eBody, getString(R.string.new_post_validation_error), false)
            }
        }

        viewmodel.isValidationError.observe(this){
            (requireActivity() as? NewPostActivity)?.showSnackbar(binding.eBody, getString(R.string.new_post_validation_error), false)
        }

        viewmodel.publicity.observe(this){
            when(it){
                "PUBLIC" -> {
                    binding.bPublicity.text = getString(R.string.publicity_all)
                }
                "FRIENDS" -> {
                    binding.bPublicity.text = getString(R.string.publicity_friends)
                }
                else -> {
                    binding.bPublicity.text = getString(R.string.publicity_own)
                }
            }
        }
    }

    override fun addListeners() {
        binding.bCross.setOnClickListener(this)
        binding.bSend.setOnClickListener(this)
        binding.bPublicity.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.b_cross -> {
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
            }
            R.id.b_send -> {
                viewmodel.post(binding.eBody.text.toString(), image, originalPostId, originalAuthorName, originalPostBody, originalPostImageUrl, originalAuthorId, originalPostDate, originalPostRepostCount, originalPostLikeCount)
            }
            R.id.b_publicity -> {
                PublicityBottomSheetFragment(
                    onTypeSelectred = { type ->
                        viewmodel.setPublicity(type)
                    }
                ).show(childFragmentManager, "")
            }
        }
    }
}