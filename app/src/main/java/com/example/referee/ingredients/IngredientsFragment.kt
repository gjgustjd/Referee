package com.example.referee.ingredients

import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentIngredientsBinding
import com.example.referee.ingredientadd.IngredientAddActivity
import com.example.referee.ingredientadd.model.IngredientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngredientsFragment :
    BaseFragment<FragmentIngredientsBinding>(R.layout.fragment_ingredients) {

    private val viewModel by activityViewModels<IngredientsFragmentViewModel>()
    private var ingredientAdapter: IngredientsAdapter? = null
    private val decoration by lazy {
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = 30
                outRect.left = 30
                outRect.bottom = 30
            }
        }
    }
    override fun initView() {
        initRecyclerView()
    }

    override fun initListeners() {
        binding.fabAddIngredient.setOnClickListener {
            startActivity(Intent(requireActivity(), IngredientAddActivity::class.java))
        }
        viewModel.event.observe(requireActivity()) {
            when (it.getContentIfNotHandled()) {
                is IngredientsEvent.GetIngredients.Success -> {
                    val event = it.peekContent() as IngredientsEvent.GetIngredients.Success
                    updateRecyclerView(event.ingredients)
                    hideLoading()
                }

                else -> Unit
            }
        }
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.bitmapFlow.collect { event ->
                event?.let {
                    ingredientAdapter?.bindThumbnail(event.bitmap, event.position)
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvIngredients) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }
        showLoading()
        viewModel.getIngredientsList()
    }

    private fun updateRecyclerView(items: List<IngredientEntity>) {
        ingredientAdapter = IngredientsAdapter(items, viewModel::getImageBitmap)
        binding.rvIngredients.adapter = ingredientAdapter
    }
}