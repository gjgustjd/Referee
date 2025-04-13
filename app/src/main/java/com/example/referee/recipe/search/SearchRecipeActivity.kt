package com.example.referee.recipe.search

import android.content.Context
import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.CommonWebViewActivity
import com.example.referee.common.base.BaseActivity
import com.example.referee.common.extensions.visible
import com.example.referee.common.extensions.gone
import com.example.referee.databinding.ActivitySearchItemBinding
import com.example.referee.network.LinkUtils
import com.example.referee.recipe.RecipeAdapter
import com.example.referee.recipe.search.model.SearchRecipeEvent
import com.jakewharton.rxbinding4.view.clicks
import java.util.concurrent.TimeUnit

class SearchRecipeActivity :BaseActivity<ActivitySearchItemBinding>(R.layout.activity_search_item){

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context,SearchRecipeActivity::class.java)
        }
    }

    private val viewModel:SearchRecipeViewModel by viewModels()
    private val recipeAdapter:RecipeAdapter by lazy {
        RecipeAdapter { position ->
            val intent = CommonWebViewActivity.newIntent(
                this@SearchRecipeActivity,
                "${LinkUtils.RECIPE_1000_URL}${recipeAdapter.getRecipeNumber(position)}",
                onPageFinishedJavaScript = "javascript/Hide10000RecipePageTopBar.js"
            )
            startActivity(intent)
        }
    }

    private val decoration by lazy {
        val margin = CommonUtil.pxToDp(
            this@SearchRecipeActivity,
            resources.getDimension(R.dimen.decorator_default_margin).toInt()
        )

        CommonRecyclerViewDecoration(
            bottomMargin = margin
        )
    }

    override fun initViews() {
        with(binding) {
            title = getString(R.string.recipe_search_title)
            etKeyword.hint = getString(R.string.recipe_search_item)
        }
        initRecyclerView()
    }

    override fun initListeners() {
        super.initListeners()

        with(binding) {
            btnConfirm.clicks()
                .throttleFirst(
                    resources.getInteger(R.integer.click_throttle_default_duration).toLong(),
                    TimeUnit.MILLISECONDS
                )
                .subscribe {
                    showLoading()
                    hideKeyBoard()
                    viewModel.getRecipesByTitle(etKeyword.text.toString())
                }
                .apply { addDisposable(this) }

            etKeyword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnConfirm.performClick()
                    true
                } else {
                    false
                }
            }

            rvSearchResults.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY != oldScrollY) {
                    hideKeyBoard()
                }
            }
        }
        viewModel.event.observe(this) {
            when (it.getContentIfNotHandled()) {
                is SearchRecipeEvent.SearchRecipeSuccess -> {
                    hideLoading()
                    binding.tvEmptyList.gone()
                    val recipes = (it.peekContent() as SearchRecipeEvent.SearchRecipeSuccess).recipes
                    recipeAdapter.submitList(recipes)
                    binding.rvSearchResults.visible()
                }

                else -> Unit
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvSearchResults) {
            adapter = recipeAdapter
            layoutManager =
                LinearLayoutManager(this@SearchRecipeActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }
    }
}