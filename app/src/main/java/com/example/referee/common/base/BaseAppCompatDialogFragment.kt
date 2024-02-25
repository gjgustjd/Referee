package com.example.referee.common.base

import android.app.Dialog
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseAppCompatDialogFragment<B : ViewDataBinding>(private val layoutResourceId: Int) :
    AppCompatDialogFragment() {

    var onBackPressed: (() -> Unit)? = null
    protected val binding: B by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            layoutResourceId,
            null,
            false
        )
    }

    /* Dialog가 show를 통해 보여지기 전 */
    override fun onStart() {
        super.onStart()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed?.invoke()
                dismiss()
            }
        }

        val dialog = dialog as? AppCompatDialog
        dialog?.onBackPressedDispatcher?.addCallback(callback)
    }

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return onCreateDialog()
    }

    abstract fun onCreateDialog(): AppCompatDialog
}