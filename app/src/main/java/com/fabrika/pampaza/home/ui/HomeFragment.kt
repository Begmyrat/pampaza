package com.fabrika.pampaza.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fabrika.pampaza.common.ui.MyCustomDialog
import com.fabrika.pampaza.common.ui.MyCustomDialogType
import com.fabrika.pampaza.room.model.PostModel
import com.fabrika.pampaza.room.viewmodel.RoomViewModel


class HomeFragment : Fragment(), BaseFragment {
    companion object {
        const val TAG = "HomeFragment"
        const val LIMIT = 7L
        const val OFFSET = Long.MAX_VALUE
    }

    lateinit var binding: FragmentHomeBinding
    lateinit var viewmodel: HomeViewModel
    private lateinit var adapterPost: MyPostAdapter
    private lateinit var postList: MutableList<PostEntity>
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[HomeViewModel::class.java]
        roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
        roomViewModel.deleteAllPost()
        MainActivity.viewmodel.isSplash = false
        postList = mutableListOf()
        adapterPost = MyPostAdapter(requireActivity() as MainActivity)
        binding.recPosts.adapter = adapterPost


        addListeners()
        addObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPostsWithPagination(OFFSET, LIMIT)
    }

    private fun getPostsWithPagination(offset: Long, limit: Long) {
        viewmodel.getPostsWithPagination(offset, limit)
    }

    override fun addObservers() {
        viewmodel.allPosts.observe(this, Observer {
            roomViewModel.addAllPost(it.map { entity -> entity.toPostModel() })
            Log.d("allPosts:", it.toString())
            postList = mutableListOf()
            if(!binding.swipeRefresh.isRefreshing){
               postList.addAll(adapterPost.differ.currentList)
            }
            postList.addAll(it)
            adapterPost.differ.submitList(postList)
            binding.swipeRefresh.isRefreshing = false
            viewmodel.OFFSET_OLD = viewmodel.OFFSET
        })

        viewmodel.isLikeError.observe(this) {
            Log.d(TAG, "likeStatus: $it")
        }

        MainActivity.viewmodel.userEntity.observe(this) {
            it.let {
                adapterPost.notifyDataSetChanged()
            }
        }

        viewmodel.isLoading.observe(this){
            (requireActivity() as? MainActivity)?.showLoading(it)
        }
    }

    override fun addListeners() {

        binding.bCreateNewPost.setOnClickListener{
            val intent = Intent(requireContext(), NewPostActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
        }

        binding.swipeRefresh.setOnRefreshListener {
            Log.d(TAG, "refresh")
            viewmodel.getPostsWithPagination(OFFSET, LIMIT)
        }

        adapterPost.onLastItemShown = {
            it.date?.let { date ->
                viewmodel.OFFSET = date
                getPostsWithPagination(date, LIMIT)
            }
        }

        adapterPost.onCommentButtonClick = {
            Log.d(TAG, "commentClicked")
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
            intent.putExtra(
                PostDetailActivity.IS_LIKED,
                MainActivity.viewmodel.userEntity.value?.likedPosts?.contains(it.id) == true
            )
            intent.putExtra(PostDetailActivity.IS_COMMENT_BUTTON_CLICKED, true)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
        }

        adapterPost.onRePostButtonClick = {
            val intent = Intent(requireContext(), NewPostActivity::class.java)
            intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_USER_NAME, it.authorName)
            intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_USER_ID, it.authorId)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_BODY, it.body)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_ID, it.id)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_IMAGE_URL, it.imageUrl)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_DATE, it.date)
            intent.putExtra(NewPostActivity.ORIGINAL_AUTHOR_AVATAR_URL, it.authorAvatarUrl)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_REPOST_COUNT, it.rePostCount)
            intent.putExtra(NewPostActivity.ORIGINAL_POST_LIKE_COUNT, it.likeCount)
            startActivity(intent)
        }

        adapterPost.onLikeButtonClick = { entity, likeStatus ->
            Log.d(TAG, "likeClicked")
            val obj = postList.find { it.id == entity.id }
            obj?.likeCount = (obj?.likeCount ?: 0) + if(likeStatus) 1 else -1
            val index = postList.indexOf(obj)
            obj?.let { postList.set(index, it) }
            adapterPost.differ.submitList(postList)
            viewmodel.likePost((requireActivity() as MainActivity), entity.id ?: "")
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

        adapterPost.onPostItemClicked = { item, index ->
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
            resultLauncher.launch(intent)
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

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == PostDetailActivity.RESULT_CODE) {
                // There are no request codes
                val data: Intent? = result.data
                viewmodel.lastClickedItemIndex?.let {
                    val list = mutableListOf<PostEntity>()
                    list.addAll(adapterPost.differ.currentList)
                    list[it].commentCount =
                        data?.getLongExtra(PostDetailActivity.COMMENT_COUNT, 0)
                    list[it].likeCount =
                        data?.getLongExtra(PostDetailActivity.LIKE_COUNT, 0)
                    adapterPost.differ.submitList(list)
                }
            }
        }
}