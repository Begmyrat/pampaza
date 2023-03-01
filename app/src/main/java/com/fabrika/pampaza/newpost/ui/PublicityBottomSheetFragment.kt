package com.fabrika.pampaza.newpost.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.PublicityBottomSheetBinding
import com.fabrika.pampaza.newpost.model.PublicityType
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PublicityBottomSheetFragment(
    var onTypeSelectred: ((type: PublicityType) -> Unit)? = null
) : BottomSheetDialogFragment() {

    lateinit var binding: PublicityBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PublicityBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lAll.setOnClickListener {
            onTypeSelectred?.invoke(PublicityType.PUBLIC)
            dismiss()
        }
        binding.lFriends.setOnClickListener {
            onTypeSelectred?.invoke(PublicityType.FRIENDS)
            dismiss()
        }
        binding.lOwn.setOnClickListener {
            onTypeSelectred?.invoke(PublicityType.OWN)
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}

