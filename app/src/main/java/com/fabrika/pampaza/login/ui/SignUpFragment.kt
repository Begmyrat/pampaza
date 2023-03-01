package com.fabrika.pampaza.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentSignUpBinding
import com.fabrika.pampaza.login.model.LoginStatusType
import com.fabrika.pampaza.login.viewmodel.LoginViewModel

class SignUpFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentSignUpBinding
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

        viewmodel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        addListeners()
        addObservers()
    }

    override fun addObservers() {
        viewmodel.status.observe(this, Observer{
            when (it) {
                LoginStatusType.SUCCESS -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
                }
                LoginStatusType.FAIL -> {
                    (requireActivity() as? LoginActivity)?.showSnackbar(binding.bSignUp, getString(R.string.sign_up_failure), false)
                }
                LoginStatusType.ERROR_FILL_THE_BLANKS -> {
                    (requireActivity() as? LoginActivity)?.showSnackbar(binding.bSignUp, getString(R.string.fill_the_blanks_error), false)
                }
                LoginStatusType.ERROR_PASSWORD_CONFIRMATION -> {
                    (requireActivity() as? LoginActivity)?.showSnackbar(binding.bSignUp, getString(R.string.password_confirmation_error), false)
                }
                else -> {

                }
            }
        })
    }

    override fun addListeners() {
        binding.bSignUp.setOnClickListener(this)
        binding.tLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.t_login -> {
                findNavController().popBackStack()
            }
            R.id.b_signUp -> {
                viewmodel.signUp(binding.eUsername.text.toString(), binding.ePassword.text.toString(), binding.ePasswordConfirm.text.toString())
            }
        }
    }
}