package com.fabrika.pampaza.home.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentHomeBinding
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.home.ui.adapter.MyPostAdapter
import com.fabrika.pampaza.home.viewmodel.HomeViewModel

class HomeFragment : Fragment(), BaseFragment {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewmodel: HomeViewModel
    val TAG = "HomeFragment"
    val adapterPost: MyPostAdapter by lazy {
        MyPostAdapter(requireActivity() as MainActivity)
    }
    lateinit var postList: MutableList<PostEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        postList = mutableListOf(PostEntity("1"), PostEntity("2"),PostEntity("3"),PostEntity("4"))
        adapterPost.differ.submitList(postList)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.recPosts.adapter = adapterPost

        addListeners()
        addObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun addObservers() {

    }

    override fun addListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            Log.d(TAG, "refresh")
        }


    }
}