package com.example.referee.fridge

import android.content.Intent
import android.os.Bundle
import com.example.referee.common.extensions.visible
import com.example.referee.common.extensions.gone
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentFridgeBinding
import com.example.referee.fridge.ingredientpage.IngredientPageActivity
import com.example.referee.fridge.model.FridgeEvent

class FridgeFragment : BaseFragment<FragmentFridgeBinding>(R.layout.fragment_fridge) {

    companion object {
        @JvmStatic
        fun newInstance() =
            FridgeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val viewModel:FridgeFragViewModel by activityViewModels()

    private val fridgeAdapter by lazy {
        FridgeItemsAdapter { name ->
            context?.let {
                startActivity(IngredientPageActivity.newIntent(it, name))
            }
       }
    }
    private val decoration by lazy {
        context?.let {
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
        binding.fabAddIngredient.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, SearchIngredientsActivity::class.java))
            }
        }

        initRecyclerView()
        viewModel.getFridgeItems()
    }
    override fun initListeners() {
        viewModel.event.observe(activity as LifecycleOwner) {
            when (it.getContentIfNotHandled()) {
                is FridgeEvent.FridgeItemsEvent -> {
                    val data = it.peekContent() as FridgeEvent.FridgeItemsEvent

                    with(binding) {
                        if (data.items.isEmpty()) {
                            tvEmptyList.visible()
                            rvIngredients.gone()
                        } else {
                            tvEmptyList.gone()
                            rvIngredients.visible()
                            fridgeAdapter.submitList(data.items)
                        }
                    }
                }

                else -> Unit
            }
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        if(menuVisible) activity?.title = getString(R.string.navigation_menu_fridger)
    }

    private fun initRecyclerView() {
        with(binding.rvIngredients) {
            context?.let {
                adapter = fridgeAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                decoration?.let {
                    addItemDecoration(it)
                }
            }
        }
    }
}