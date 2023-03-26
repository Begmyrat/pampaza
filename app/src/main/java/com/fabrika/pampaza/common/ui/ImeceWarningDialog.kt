package com.fabrika.pampaza.common.ui

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import androidx.core.view.isVisible
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.UiImeceWarningBinding

enum class MyCustomDialogType {
    WARNING,
    CONFIRMATION,
    SUCCESS,
    ERROR,
    LOGIN,
    NOTIFICATION
}

enum class MyCustomDialogResult {
    OK,
    CANCEL
}

class MyCustomDialog(
    context: Context,
    type: MyCustomDialogType,
    title: String,
    description: String,
    positiveButton: String? = null,
    negativeButton: String? = null,
    onResult: ((result: MyCustomDialogResult) -> Unit)? = null
) : Dialog(context, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth), Animator.AnimatorListener {

    private var binding = UiImeceWarningBinding.inflate(layoutInflater)

    private val ANIMATION_DURATION = 100L

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(true)

        binding.cardDialog.animate().setDuration(ANIMATION_DURATION).alpha(0.6f).start()
        binding.dialogRoot.animate().translationY(0f).setDuration(ANIMATION_DURATION).start()

        when (type) {
            MyCustomDialogType.WARNING -> {
                binding.LottieAnimation.setAnimation(R.raw.info_anim)
            }
            MyCustomDialogType.CONFIRMATION -> {
                binding.LottieAnimation.setAnimation(R.raw.info_anim)
            }
            MyCustomDialogType.SUCCESS -> {
                binding.discard.isVisible = false
                binding.LottieAnimation.setAnimation(R.raw.success_anim)
            }
            MyCustomDialogType.ERROR -> {
                binding.LottieAnimation.setAnimation(R.raw.warning_anim)
            }
            MyCustomDialogType.LOGIN -> {
                binding.LottieAnimation.setAnimation(R.raw.warning_anim)
            }
            MyCustomDialogType.NOTIFICATION -> {
                binding.LottieAnimation.setAnimation(R.raw.internal_notif_anim)
            }
        }

        binding.text.text = title
        binding.description.text = description

        binding.okay.setOnClickListener {
            onResult?.let {
                onResult(MyCustomDialogResult.OK)
            }
            dismiss()
        }

        binding.discard.setOnClickListener {
            onResult?.let {
                onResult(MyCustomDialogResult.CANCEL)
            }
            dismiss()
        }

        positiveButton?.let {
            binding.okay.text = it
        }

        negativeButton?.let {
            binding.discard.text = it
        }
    }

    override fun dismiss() {
        binding.root.isClickable = false
        binding.cardDialog.animate().setListener(this).alpha(0f).translationY(200f).setDuration(ANIMATION_DURATION).start()
    }

    override fun onAnimationEnd(p0: Animator) {
        super.dismiss()
    }

    override fun onAnimationCancel(p0: Animator) {}
    override fun onAnimationRepeat(p0: Animator) {}
    override fun onAnimationStart(p0: Animator) {}
}