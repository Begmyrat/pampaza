package com.fabrika.pampaza.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.fabrika.pampaza.common.ui.BaseFragment
import com.fabrika.pampaza.databinding.FragmentSettingsBinding
import com.fabrika.pampaza.utils.SharedPref


class SettingsFragment : Fragment(), BaseFragment, View.OnClickListener {

    lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        addListeners()
        addObservers()

        binding.switchDarkMode.isChecked = SharedPref.read(SharedPref.IS_DARK_MODE, false)

        return binding.root
    }

    override fun addObservers() {

    }

    override fun addListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(if(isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            SharedPref.write(SharedPref.IS_DARK_MODE, isChecked)
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}