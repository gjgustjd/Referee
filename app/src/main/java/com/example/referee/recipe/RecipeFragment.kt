package com.example.referee.recipe

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.CommonWebViewActivity
import com.example.referee.common.base.BaseActivity
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentCookBinding
import com.example.referee.network.LinkUtils
import com.example.referee.recipe.model.RecipeEvent
import com.example.referee.recipe.search.SearchRecipeActivity
import com.jakewharton.rxbinding4.view.clicks
import java.util.concurrent.TimeUnit

class RecipeFragment : BaseFragment<FragmentCookBinding>(R.layout.fragment_cook) {

    companion object {
        @JvmStatic
       fun newInstance(param1: String, param2: String) =
            RecipeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val viewModel:RecipeFragViewModel by activityViewModels()
    private val recipeAdapter:RecipeAdapter by lazy {
        RecipeAdapter { position ->
            context?.let {
                val intent = CommonWebViewActivity.newIntent(
                    it,
                    "${LinkUtils.RECIPE_1000_URL}${recipeAdapter.getRecipeNumber(position)}",
                    onPageFinishedJavaScript = "javascript/Hide10000RecipePageTopBar.js"
                )
                startActivity(intent)
            }
        }
    }

    private val decoration by lazy {
        activity?.let {
            val margin = CommonUtil.pxToDp(
                it,
                resources.getDimension(R.dimen.decorator_default_margin).toInt()
            )

            CommonRecyclerViewDecoration(
                bottomMargin = margin
            )
        }
    }

    override fun initViews() {
        initRecyclerView()
        binding.fabSearchRecipe.clicks()
            .throttleFirst(
                resources.getInteger(R.integer.click_throttle_default_duration).toLong(),
                TimeUnit.MILLISECONDS
            )
            .subscribe {
                activity?.let {
                    startActivity(SearchRecipeActivity.newIntent(it))
                }
            }.apply {
                (activity as? BaseActivity<*>)?.addDisposable(this)
            }
        loadRecipes()
    }

    override fun initListeners() {
        viewModel.event.observe(this) {
            when(it.getContentIfNotHandled()) {
                is RecipeEvent.RecipeSuccess -> {
                    val result = it.peekContent() as RecipeEvent.RecipeSuccess
                    recipeAdapter.submitList(result.recipes)
                    hideLoading()
                }

                is RecipeEvent.RecipeFailure -> Unit

                else -> Unit
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.navigation_menu_recipe)
    }

    private fun loadRecipes() {
        showLoading()
        viewModel.getRecipesByFridgeIngredients()
    }

    private fun initRecyclerView() {
        activity?.let { activity ->
            with(binding.rvRecipes) {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                decoration?.let {
                    addItemDecoration(it)
                }
            }
        }
    }
}