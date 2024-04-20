package com.example.referee.fridge.ingredientpage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityIngredientWebPageBinding
import com.example.referee.fridge.model.FridgeIngredientEntity
import com.example.referee.fridge.model.SearchIngredientsEvent
import com.example.referee.network.LinkUtils

class IngredientPageActivity :
    BaseActivity<ActivityIngredientWebPageBinding>(R.layout.activity_ingredient_web_page) {

    companion object {
        const val EXTRA_INGREDIENT_TITLE = "EXTRA_INGREDIENT_TITLE"
        const val EXTRA_INGREDIENT_PAGE_ID = "EXTRA_INGREDIENT_PAGE_ID"
        const val EXTRA_INGREDIENT_SNIPPET = "EXTRA_INGREDIENT_SNIPPET"
        const val EXTRA_RESULT_INGREDIENT_DATA = "EXTRA_RESULT_INGREDIENT_DATA"

        fun newIntent(context: Context, title: String, pageId: Int, snippet: String): Intent {
            return Intent(context, IngredientPageActivity::class.java).apply {
                putExtra(EXTRA_INGREDIENT_TITLE, title)
                putExtra(EXTRA_INGREDIENT_PAGE_ID, pageId)
                putExtra(EXTRA_INGREDIENT_SNIPPET, snippet)
            }
        }
    }

    private val viewModel: IngredientPageViewModel by viewModels()
    private var pageimage:String? = null

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
            val dataIntent = Intent()
            val name = binding.title
            val snippet = intent.extras?.getString(EXTRA_INGREDIENT_SNIPPET)
            val thumbnailUrl = "${LinkUtils.MEDIAWIKI_PAGE_URL}${name}#/media/파일:${pageimage}"
            name?.let {
                val entity = FridgeIngredientEntity(
                    name = it,
                    description = snippet,
                    thumbnailUrl = thumbnailUrl
                )
                dataIntent.putExtra(EXTRA_RESULT_INGREDIENT_DATA, entity)
            }
            setResult(RESULT_OK, dataIntent)
            finish()
        }

        intent.getIntExtra(EXTRA_INGREDIENT_PAGE_ID, -1).let { pageId ->
            if (pageId != -1) {
                viewModel.getIngredientPageData(pageId)
            }
        }
    }

    override fun initListeners() {
        super.initListeners()

        viewModel.event.observe(this) {
            when (it.getContentIfNotHandled()) {
                is SearchIngredientsEvent.PageSuccess -> {
                    val page = (it.peekContent() as SearchIngredientsEvent.PageSuccess).page
                    pageimage = page.pageimage
                }

                else -> {
                }
            }
        }
    }
}