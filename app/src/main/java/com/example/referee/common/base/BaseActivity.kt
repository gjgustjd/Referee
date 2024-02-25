package com.example.referee.common.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.referee.R
import com.example.referee.common.ProgressDialog
import com.example.referee.common.model.CommonEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.TimeUnit

abstract class BaseActivity<T>(private val layoutResourceId:Int) : AppCompatActivity() where T : ViewDataBinding {

    protected lateinit var binding: T
    protected val compositeDisposable = CompositeDisposable()
    private val hideLoadingRequestSubject: Subject<CommonEvent> = PublishSubject.create()
    private lateinit var hideLodaingRequestObserver:Disposable

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
    open fun initListeners() {
        hideLodaingRequestObserver =
            hideLoadingRequestSubject
                .throttleLatest(
                    resources.getInteger(R.integer.hide_loading_throttle_default_duration).toLong(),
                    TimeUnit.MILLISECONDS
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (progressDialog?.isDetached == false) {
                        progressDialog?.dismiss()
                    }
                }.apply {
                    compositeDisposable.add(this)
                }
    }

    fun showLoading(onBackPressed: (() -> Unit)? = null) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog()
        }

        if (progressDialog?.isAdded != true) {
            progressDialog?.show(supportFragmentManager, ProgressDialog.PROGRESS_DIALOG_TAG)
        }

        progressDialog?.onBackPressed = onBackPressed
    }

     fun hideLoading() {
         hideLoadingRequestSubject.onNext(CommonEvent.HideLodaing)
    }
}