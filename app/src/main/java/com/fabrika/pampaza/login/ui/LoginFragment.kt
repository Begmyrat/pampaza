package com.fabrika.pampaza.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentLoginBinding
import com.fabrika.pampaza.common.model.LoginStatusType
import com.fabrika.pampaza.login.viewmodel.LoginViewModel

class LoginFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewmodel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[LoginViewModel::class.java]
        addListeners()
        addObservers()
    }

    override fun addObservers() {
        viewmodel.status.observe(this, Observer {
            if(it == LoginStatusType.FAIL){
                (requireActivity() as? LoginActivity)?.showSnackbar(binding.root, getString(R.string.login_error), false)
            } else{
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
            }
        })
    }

    override fun addListeners() {
        binding.tCreateAccount.setOnClickListener(this)
        binding.bLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.t_createAccount -> {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            R.id.b_login -> {
                viewmodel.getUser(binding.eUsername.text.toString(), binding.ePassword.text.toString())
            }
        }
    }
}