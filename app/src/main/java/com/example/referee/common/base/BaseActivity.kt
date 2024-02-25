package com.example.referee.common.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.referee.R
import com.example.referee.common.ProgressDialog
import com.example.referee.common.model.CommonEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseActivity<T>(private val layoutResourceId:Int) : AppCompatActivity() where T : ViewDataBinding {

    protected lateinit var binding: T
    protected val compositeDisposable = CompositeDisposable()
    private val hideLoadingRequestSubject: PublishSubject<CommonEvent.HideLoading> =
        PublishSubject.create()
    private val showLoadingRequestSubject: PublishSubject<CommonEvent.ShowLoading> =
        PublishSubject.create()
    private lateinit var loadingRequestObserver: Disposable

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
        val duration = resources.getInteger(
            R.integer.loading_throttle_default_duration
        ).toLong()
        val slSubject = showLoadingRequestSubject.throttleFirst(duration, TimeUnit.MILLISECONDS)
        val hlSubject = hideLoadingRequestSubject.throttleLatest(duration, TimeUnit.MILLISECONDS)

        loadingRequestObserver = PublishSubject.merge(slSubject, hlSubject).subscribe { event ->
            when (event) {
                is CommonEvent.ShowLoading -> {
                    if (progressDialog == null) {
                        progressDialog = ProgressDialog()
                    }

                    if (progressDialog?.isAdded == false) {
                        progressDialog?.show(
                            supportFragmentManager,
                            ProgressDialog.PROGRESS_DIALOG_TAG
                        )
                    }

                    progressDialog?.onBackPressed = event.onBackPressed
                }

                is CommonEvent.HideLoading -> {
                    if (progressDialog?.isDetached == false) {
                        progressDialog?.dismiss()
                    }
                }
            }
        }.apply {
            compositeDisposable.add(this)
        }
    }

    fun showLoading(onBackPressed: (() -> Unit)? = null) {
        showLoadingRequestSubject.onNext(CommonEvent.ShowLoading(onBackPressed))
    }

     fun hideLoading() {
         hideLoadingRequestSubject.onNext(CommonEvent.HideLoading)
    }
}