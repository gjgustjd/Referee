package com.example.referee.fridge.ingredientpage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
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
        const val EXTRA_INGREDIENT_NAME = "EXTRA_INGREDIENT_TITLE"
        const val EXTRA_INGREDIENT_PAGE_ID = "EXTRA_INGREDIENT_PAGE_ID"
        const val EXTRA_INGREDIENT_SNIPPET = "EXTRA_INGREDIENT_SNIPPET"
        const val EXTRA_RESULT_INGREDIENT_DATA = "EXTRA_RESULT_INGREDIENT_DATA"
        const val EXTRA_IS_FROM_SEARCH = "EXTRA_FROM_SEARCH"

        fun newIntent(
            context: Context,
            title: String,
            pageId: Int? = null,
            snippet: String? = null,
            isFromSearch: Boolean = false
        ): Intent {
            return Intent(context, IngredientPageActivity::class.java).apply {
                putExtra(EXTRA_INGREDIENT_NAME, title)
                putExtra(EXTRA_INGREDIENT_PAGE_ID, pageId)
                putExtra(EXTRA_INGREDIENT_SNIPPET, snippet)
                putExtra(EXTRA_IS_FROM_SEARCH, isFromSearch)
            }
        }
    }

    private val viewModel: IngredientPageViewModel by viewModels()
    private var pageimage:String? = null
    private var isFromSearch = false
    private var pageName:String? = null
    private var pageid:Int? = null
    private var snippet: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initExtras()
        super.onCreate(savedInstanceState)
    }

    private fun initExtras() {
        intent.extras?.run {
            pageName = getString(EXTRA_INGREDIENT_NAME)
            isFromSearch = getBoolean(EXTRA_IS_FROM_SEARCH)
            pageid = getInt(EXTRA_INGREDIENT_PAGE_ID)
            snippet = getString(EXTRA_INGREDIENT_SNIPPET)
        }
    }

    override fun initViews() {
        binding.title = pageName
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

        binding.btnInsertToFridge.visibility = if (isFromSearch) {
            pageid?.let { pageId ->
                if (pageId != 0) {
                    viewModel.getIngredientPageData(pageId)
                }
            }
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.btnInsertToFridge.setOnClickListener {
            val dataIntent = Intent()
            pageName?.let {
                val entity = FridgeIngredientEntity(
                    name = it,
                    description = snippet,
                    thumbnailUrl = pageimage
                )
                dataIntent.putExtra(EXTRA_RESULT_INGREDIENT_DATA, entity)
            }
            setResult(RESULT_OK, dataIntent)
            finish()
        }
    }

    override fun initListeners() {
        super.initListeners()

        viewModel.event.observe(this) {
            when (it.getContentIfNotHandled()) {
                is SearchIngredientsEvent.PageSuccess -> {
                    val page = (it.peekContent() as SearchIngredientsEvent.PageSuccess).page
                    pageimage = page.thumbnail.source
                }

                else -> {
                }
            }
        }
    }
}