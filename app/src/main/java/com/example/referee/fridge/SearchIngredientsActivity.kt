package com.example.referee.fridge

import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.base.BaseActivity
import com.example.referee.common.extensions.visible
import com.example.referee.common.extensions.gone
import com.example.referee.databinding.ActivitySearchItemBinding
import com.example.referee.fridge.ingredientpage.IngredientPageActivity
import com.example.referee.fridge.ingredientpage.IngredientPageActivity.Companion.EXTRA_RESULT_INGREDIENT_DATA
import com.example.referee.fridge.model.FridgeIngredientEntity
import com.example.referee.fridge.model.SearchIngredientsEvent

class SearchIngredientsActivity :
    BaseActivity<ActivitySearchItemBinding>(R.layout.activity_search_item) {

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
                    snippet,
                    true
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
        with(binding.etKeyword) {
            requestFocus()
            showKeyBoard()
        }

        initRecyclerView()
    }

    override fun initListeners() {
        super.initListeners()
        with(binding) {
            etKeyword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnConfirm.performClick()
                    true
                } else {
                    false
                }
            }

            btnConfirm.setOnClickListener {
                etKeyword.clearFocus()
                hideKeyBoard()
                showLoading()
                viewModel.searchIngredients(etKeyword.text.toString())
            }

            rvSearchResults.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY != oldScrollY) {
                    etKeyword.clearFocus()
                    hideKeyBoard()
                }
            }

            viewModel.event.observe(this@SearchIngredientsActivity) {
                if (!it.hasBeenHandled) {
                    when (val eventContent = it.peekContent()) {
                        is SearchIngredientsEvent.SearchSuccess -> {
                            tvEmptyList.gone()
                            rvSearchResults.visible()
                            searchAdapter.submitList(eventContent.result)
                            hideLoading()
                        }

                        is SearchIngredientsEvent.SearchFailed,
                        is SearchIngredientsEvent.PageSuccess,
                        is SearchIngredientsEvent.PageFailed -> hideLoading()

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