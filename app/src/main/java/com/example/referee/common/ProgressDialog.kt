package com.example.referee.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import com.example.referee.R
import com.example.referee.common.base.BaseAppCompatDialogFragment
import com.example.referee.databinding.ViewLoadingProgressbarBinding

class ProgressDialog :
    BaseAppCompatDialogFragment<ViewLoadingProgressbarBinding>(R.layout.view_loading_progressbar) {

    companion object {
        const val PROGRESS_DIALOG_TAG = "ProgressDialog"
    }

    override fun onCreateDialog(): AppCompatDialog {
        val bindingParent = binding.root.parent
        (bindingParent as? ViewGroup)?.run {
            removeView(binding.root)
        }

        return AppCompatDialog(requireContext()).apply {
            setContentView(binding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }
}