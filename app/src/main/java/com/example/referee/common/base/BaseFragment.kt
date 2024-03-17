package com.example.referee.common.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T>(private val layoutResourceId: Int) :
    Fragment() where T : ViewDataBinding {

    protected lateinit var binding: T
    abstract fun initViews()
    abstract fun initListeners()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return binding.root
    }

    open fun onActivityReenter(resultCode: Int, data: Intent?) = Unit

    protected fun showToast(text:String) {
        val baseActivity = activity as? BaseActivity<*>
        baseActivity?.showToast(text)
    }

    protected fun showLoading(onBackPressed: (() -> Unit)? = null) {
        val baseActivity = activity as? BaseActivity<*>
        baseActivity?.showLoading(onBackPressed)
    }

    protected fun hideLoading() {
        val baseActivity = activity as? BaseActivity<*>
        baseActivity?.hideLoading()
    }
}