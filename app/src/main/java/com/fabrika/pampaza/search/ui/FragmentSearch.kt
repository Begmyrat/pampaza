package com.fabrika.pampaza.search.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.databinding.FragmentSearchBinding
import com.fabrika.pampaza.home.viewmodel.HomeViewModel
import com.fabrika.pampaza.search.viewmodel.SearchViewModel


class FragmentSearch : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewmodel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[SearchViewModel::class.java]
        addListeners()
        addObservers()
    }

    private fun addObservers() {
        viewmodel.data.observe(this){
            Log.d("Search Result: ", it.toString())
        }
    }

    private fun addListeners() {
        binding.tiSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewmodel.search(binding.tiSearch.text.toString())
            }
            true
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}