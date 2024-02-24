package com.example.referee.common.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.referee.common.ProgressDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<T>(private val layoutResourceId:Int) : AppCompatActivity() where T : ViewDataBinding {

    protected lateinit var binding: T
    protected val compositeDisposable = CompositeDisposable()
    protected fun showToast(text:String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }
    protected var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)

        initViews()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    abstract fun initViews()
    abstract fun initListeners()

     fun showLoading() {
        progressDialog = ProgressDialog()
        progressDialog?.show(supportFragmentManager,"progressDialog")
    }

     fun hideLoading() {
        progressDialog?.dismiss()
    }
}