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

        fun newIntent(
            context: Context,
            url: String,
            title: String? = null
        ): Intent {
            return Intent(context, CommonWebViewActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_URL, url)
            }
        }
    }

    private var topBarTitle: String? = null
    private var url: String? = null

    override fun initViews() {
        initExtra()
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        url?.let {
            with(binding.wvContent) {
                webViewClient = object :WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        showLoading()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        hideTopBar()
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


    private fun hideTopBar() {
        // 상단바를 숨기는 자바스크립트를 웹뷰에 로드합니다.
        val js = """
            (function() {
                var topBar = document.querySelector('.navbar.navbar-new.navbar-fixed-top'); // 상단바의 선택자
                
                if (topBar) {
                    topBar.classList.add('hidden');
                }
            })();
        """
        binding.wvContent.evaluateJavascript(js, null)
    }

    private fun initExtra() {
        topBarTitle = intent?.getStringExtra(EXTRA_TITLE)
        url = intent?.getStringExtra(EXTRA_URL)
    }
}