package com.example.referee.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import com.example.referee.R
import com.example.referee.common.base.BaseAppCompatDialogFragment
import com.example.referee.databinding.ViewLoadingProgressbarBinding

class ProgressDialog : BaseAppCompatDialogFragment() {

    override fun onCreateDialog(): AppCompatDialog {
        val binding: ViewLoadingProgressbarBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.view_loading_progressbar,
            null,
            false
        )

        val dialog = AppCompatDialog(requireContext()).apply {
            setContentView(binding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }

        return dialog
    }
}