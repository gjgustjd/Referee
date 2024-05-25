package com.example.referee.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityWebviewCommonBinding

class CommonWebViewActivity :BaseActivity<ActivityWebviewCommonBinding>(R.layout.activity_webview_common) {

    companion object {
        const val EXTRA_TITLE="EXTRA_TITLE"
        const val EXTRA_URL="EXTRA_URL"
        const val EXTRA_ON_PAGE_FINISED_JAVASCRIPT_FILE_NAME = "EXTRA_ON_PAGE_FINISED_JAVASCRIPT_FILE_NAME"

        fun newIntent(
            context: Context,
            url: String,
            title: String? = null,
            onPageFinishedJavaScript: String? = null
        ): Intent {
            return Intent(context, CommonWebViewActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_ON_PAGE_FINISED_JAVASCRIPT_FILE_NAME, onPageFinishedJavaScript)
            }
        }
    }

    private var topBarTitle: String? = null
    private var url: String? = null
    private var onPageFinishedJavaScript: String? = null

    override fun initViews() {
        initExtra()
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        url?.let {
            with(binding.wvContent) {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        gone()

                        if (request != null && request.isRedirect) {
                            view?.loadUrl(request.url.toString())
                            return true
                        }

                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        showLoading()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinishedJavaScript?.let {
                            injectJavaScriptFromAssetsFile(it)
                        }
                        hideLoading()
                        visible()
                    }
                }
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }

                loadUrl(it)
            }
        }

        topBarTitle?.let {
          title = it
        }?:supportActionBar?.hide()
    }

    private fun initExtra() {
        topBarTitle = intent?.getStringExtra(EXTRA_TITLE)
        url = intent?.getStringExtra(EXTRA_URL)
        onPageFinishedJavaScript = intent?.getStringExtra(EXTRA_ON_PAGE_FINISED_JAVASCRIPT_FILE_NAME)
    }
}