package com.example.referee.fridge

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.Logger
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivitySearchIngredientsBinding
import com.example.referee.fridge.ingredientpage.IngredientPageActivity
import com.example.referee.fridge.ingredientpage.IngredientPageActivity.Companion.EXTRA_RESULT_INGREDIENT_DATA
import com.example.referee.fridge.model.FridgeIngredientEntity
import com.example.referee.fridge.model.SearchIngredientsEvent

class SearchIngredientsActivity :
    BaseActivity<ActivitySearchIngredientsBinding>(R.layout.activity_search_ingredients) {

    private val viewModel:SearchIngredientsViewModel by viewModels()
    private val pageActivityLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
       if(it.resultCode == RESULT_OK) {
           it.data?.getParcelableExtra(
               EXTRA_RESULT_INGREDIENT_DATA,
               FridgeIngredientEntity::class.java
           )?.let { entity ->
               viewModel.insertIngredientToFridge(entity)
           }
       }
    }
    private val searchAdapter by lazy {
        SearchIngredientsAdapter { _, title, pageid, snippet ->
            pageActivityLauncher.launch(
                IngredientPageActivity.newIntent(
                    this@SearchIngredientsActivity,
                    title,
                    pageid,
                    snippet
                )
            )
        }
    }
    private val decoration by lazy {
        val margin = CommonUtil.pxToDp(
            this,
            resources.getDimension(R.dimen.decorator_default_margin).toInt()
        )

        CommonRecyclerViewDecoration(
            bottomMargin = margin
        )
    }

    override fun initViews() {
        initRecyclerView()
    }

    override fun initListeners() {
        super.initListeners()
        binding.etKeyword.addTextChangedListener { }
        binding.btnConfirm.setOnClickListener {
            showLoading()
            viewModel.searchIngredients(binding.etKeyword.text.toString())
        }
        viewModel.event.observe(this) {
            if(!it.hasBeenHandled) {
                when(it.peekContent()) {
                    is SearchIngredientsEvent.SearchSuccess -> {
                        val result =
                            (it.peekContent() as SearchIngredientsEvent.SearchSuccess).result
                        searchAdapter.submitList(result)
                        hideLoading()
                    }

                    is SearchIngredientsEvent.SearchFailed -> {
                        hideLoading()
                    }

                    is SearchIngredientsEvent.PageSuccess -> {
                        hideLoading()
                    }

                    is SearchIngredientsEvent.PageFailed -> {
                        hideLoading()
                    }

                    is SearchIngredientsEvent.InsertFridgeIngredientSuccess -> {
                        hideLoading()
                        showToast(getString(R.string.fridge_add_ingredient_success_toast))
                        finish()
                    }

                    is SearchIngredientsEvent.InsertFridgeIngredientFailure -> {
                        hideLoading()
                        showToast(getString(R.string.fridge_add_ingredient_failed_toast))
                        finish()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSearchResults.apply {
            adapter = searchAdapter
            layoutManager =
                LinearLayoutManager(
                    this@SearchIngredientsActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            addItemDecoration(decoration)
        }
    }
}