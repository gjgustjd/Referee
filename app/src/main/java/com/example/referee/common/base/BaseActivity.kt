package com.example.referee.common.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T>(private val layoutResourceId:Int) : AppCompatActivity() where T : ViewDataBinding {

    protected lateinit var binding: T
    protected fun showToast(text:String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)

        initViews()
        initListeners()
    }

    abstract fun initViews()
    abstract fun initListeners()
}