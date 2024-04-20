package com.example.referee.fridge

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentFridgeBinding
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
       FridgeItemsAdapter()
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
                    binding.tvEmptyList.visibility = View.GONE
                    binding.rvIngredients.visibility = View.VISIBLE
                    val data = it.peekContent() as FridgeEvent.FridgeItemsEvent
                    fridgeAdapter.submitList(data.items)
                }

                else -> Unit
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvIngredients) {
            adapter = fridgeAdapter
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
}