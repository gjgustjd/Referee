package com.example.referee.fridge.ingredientpage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.referee.R
import com.example.referee.common.Logger
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityIngredientWebPageBinding
import com.example.referee.fridge.model.FridgeIngredientEntity
import com.example.referee.network.LinkUtils

class IngredientPageActivity :
    BaseActivity<ActivityIngredientWebPageBinding>(R.layout.activity_ingredient_web_page) {

    companion object {
        const val EXTRA_INGREDIENT_TITLE = "EXTRA_INGREDIENT_TITLE"
        const val EXTRA_RESULT_INGREDIENT_DATA = "EXTRA_RESULT_INGREDIENT_DATA"

        fun newIntent(context: Context, title: String): Intent {
            return Intent(context, IngredientPageActivity::class.java).apply {
                putExtra(EXTRA_INGREDIENT_TITLE, title)
            }
        }
    }

    override fun initViews() {
        binding.title = intent.extras?.getString(EXTRA_INGREDIENT_TITLE)
        binding.wvContent.apply {
            webViewClient = object : WebViewClient() {
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
                    hideLoading()
                }
            }
            loadUrl(LinkUtils.MEDIAWIKI_PAGE_URL + binding.title)
        }
        binding.viewTopBar.ivBackButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.btnInsertToFridge.setOnClickListener {
            val intent = Intent()
            binding.title?.let {
                val entity = FridgeIngredientEntity(
                    name = it
                )
                intent.putExtra(EXTRA_RESULT_INGREDIENT_DATA, entity)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}