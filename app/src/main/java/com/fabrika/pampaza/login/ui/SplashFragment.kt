package com.fabrika.pampaza.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentSplashBinding

class SplashFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentSplashBinding
//    lateinit var viewmodel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = FragmentSplashBinding.inflate(layoutInflater)
        addListeners()
        addObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewmodel.splashDelay(1000)
        MainActivity.viewmodel.isSplash = true
//        MainActivity.viewmodel.getUser("GmBegmyrat", "123123")
    }

    override fun addObservers() {
//        viewmodel.status.observe(this) {
//            when(it){
//                LoginStatusType.SPLASH_SUCCESS -> {
//                    val intent = Intent(requireContext(), MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    startActivity(intent)
//                    requireActivity().overridePendingTransition(R.anim.anim_from_right, R.anim.anim_to_left)
//                }
//                else -> {
//
//                }
//            }
//        }
    }

    override fun addListeners() {

    }

    override fun onClick(p0: View?) {

    }
}