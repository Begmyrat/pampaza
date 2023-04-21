package com.fabrika.pampaza.profile.ui

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentHomeBinding
import com.fabrika.pampaza.databinding.FragmentProfileBinding
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.home.ui.HomeFragment
import com.fabrika.pampaza.home.ui.adapter.MyPostAdapter
import com.fabrika.pampaza.home.viewmodel.HomeViewModel
import com.fabrika.pampaza.postDetail.ui.PostDetailActivity
import com.fabrika.pampaza.profile.model.ProfileObj
import com.fabrika.pampaza.profile.ui.adapter.MyProfileAdapter
import com.fabrika.pampaza.profile.viewmodel.ProfileViewModel

class ProfileFragment : Fragment(), BaseFragment {

    companion object {
        const val TAG = "ProfileFragment"
        const val LIMIT = 20L
        const val OFFSET = Long.MAX_VALUE
    }


    lateinit var binding: FragmentProfileBinding
    lateinit var viewmodel: ProfileViewModel
    var list = mutableListOf<ProfileObj>()
    private val adapterProfile: MyProfileAdapter by lazy {
        MyProfileAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[ProfileViewModel::class.java]
        addListeners()
        addObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel.getOwnPostsWithPagination(OFFSET, LIMIT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.add(ProfileObj.ProfileUserEntity())
        adapterProfile.differ.submitList(list)
        binding.recProfile.adapter = adapterProfile
    }

    override fun addObservers() {
        viewmodel.allPosts.observe(this){
            val listClone = mutableListOf<ProfileObj>()
            listClone.addAll(adapterProfile.differ.currentList)
            listClone.addAll(it)
            adapterProfile.differ.submitList(listClone)
        }
    }

    override fun addListeners() {
        adapterProfile.onLastItemShown = {
            it.date?.let { date -> viewmodel.getOwnPostsWithPagination(date, HomeFragment.LIMIT) }
        }

        adapterProfile.onPostItemClicked = { item, index ->
            viewmodel.lastClickedItemIndex = index
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra(PostDetailActivity.POST_ID, item.id)
            intent.putExtra(PostDetailActivity.AUTHOR_ID, item.authorId)
            intent.putExtra(PostDetailActivity.AUTHOR_NAME, item.authorName)
            intent.putExtra(PostDetailActivity.POST_DATE, item.date)
            intent.putExtra(PostDetailActivity.POST_BODY, item.body)
            intent.putExtra(PostDetailActivity.POST_IMAGE_URL, item.imageUrl)
            intent.putExtra(PostDetailActivity.REPOST_COUNT, item.rePostCount)
            intent.putExtra(PostDetailActivity.LIKE_COUNT, item.likeCount)
            intent.putExtra(PostDetailActivity.AUTHOR_AVATAR_URL, item.authorAvatarUrl)
            intent.putExtra(
                PostDetailActivity.IS_LIKED,
                MainActivity.viewmodel.userEntity.value?.likedPosts?.contains(item.id) == true
            )
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
        }

        adapterProfile.onOriginalPostItemClicked = {
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