package com.fabrika.pampaza.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentSignUpBinding.inflate(layoutInflater)
        addListeners()
        addObservers()
    }

    override fun addObservers() {

    }

    override fun addListeners() {
        binding.bSignUp.setOnClickListener(this)
        binding.tLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.t_login -> {
//                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                findNavController().popBackStack()
            }
            R.id.b_signUp -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
            }
        }
    }
}