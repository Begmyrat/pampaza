package com.fabrika.pampaza.newpost.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentNewPostBinding
import com.fabrika.pampaza.newpost.viewmodel.NewPostViewModel

class NewPostFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentNewPostBinding
    private lateinit var viewmodel: NewPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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


    }

    override fun addObservers() {
        viewmodel.isError.observe(this) { success ->
            Toast.makeText(requireContext(), if(success) "Success" else "Failure", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    override fun addListeners() {
        binding.bCross.setOnClickListener(this)
        binding.bSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.b_cross -> {
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.anim_from_left, R.anim.anim_to_right)
            }
            R.id.b_send -> {
                viewmodel.post(binding.eBody.text.toString(), null, null)
            }
        }
    }
}