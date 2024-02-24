package com.example.referee.common

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.referee.R
import com.example.referee.common.base.BaseDialogFragment

class ProgressDialog :BaseDialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.view_loading_progressbar)
            .setCancelable(false) // ProgressDialog가 취소 가능한지 여부를 설정합니다.
            .show().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            }
    }
}