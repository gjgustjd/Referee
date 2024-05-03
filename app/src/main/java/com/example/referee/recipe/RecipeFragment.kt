package com.example.referee.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.base.BaseActivity
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentCookBinding
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
    private val recipeAdapter by lazy {
        RecipeAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRecipesByIngredients()
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
    }

    override fun initListeners() {
        viewModel.event.observe(this) {
            when(it.getContentIfNotHandled()) {
                is RecipeEvent.RecipeSuccess -> {
                    val result = it.peekContent() as RecipeEvent.RecipeSuccess
                    recipeAdapter.submitList(result.recipes)
                }

                is RecipeEvent.RecipeFailure -> {

                }

                else -> Unit
            }
        }
    }

    private fun initRecyclerView() {
        activity?.let { activity ->
            with(binding.rvRecipes) {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(
                    CommonRecyclerViewDecoration(
                        bottomMargin = activity.resources.getDimension(
                            R.dimen.decorator_default_margin
                        ).toInt()
                    )
                )
            }
        }
    }
}