package com.example.referee.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.referee.common.RefereeApplication

abstract class BaseFragment<T>(protected val layoutResourceId: Int) :
    Fragment() where T : ViewDataBinding {

    protected lateinit var binding: T
    protected val applicationScope = RefereeApplication.instance.applicationScope

    abstract fun initView()
    abstract fun initListeners()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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

    protected fun showLoading() {
        val baseActivity = activity as? BaseActivity<*>
        baseActivity?.showLoading()
    }

    protected fun hideLoading() {
        val baseActivity = activity as? BaseActivity<*>
        baseActivity?.hideLoading()
    }
}