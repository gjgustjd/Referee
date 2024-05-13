package com.example.referee.common

import android.content.Context
import android.content.Intent
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityWebviewCommonBinding

class CommonWebViewActivity :BaseActivity<ActivityWebviewCommonBinding>(R.layout.activity_webview_common) {

    companion object {
        const val EXTRA_TITLE="EXTRA_TITLE"
        const val EXTRA_URL="EXTRA_URL"

        fun newIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, CommonWebViewActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_URL, url)
            }
        }
    }

    private var title: String? = null
    private var url: String? = null

    override fun initViews() {
        initExtra()
        initWebView()
    }

    private fun initWebView() {
        url?.let {
            binding.wvContent.loadUrl(it)
        }
    }

    private fun initExtra() {
        title = intent?.getStringExtra(EXTRA_TITLE)
        url = intent?.getStringExtra(EXTRA_URL)
    }
}