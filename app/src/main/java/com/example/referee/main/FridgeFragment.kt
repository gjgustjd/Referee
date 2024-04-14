package com.example.referee.main

import android.content.Intent
import android.os.Bundle
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentFridgeBinding
import com.example.referee.fridge.SearchIngredientsActivity

class FridgeFragment : BaseFragment<FragmentFridgeBinding>(R.layout.fragment_fridge) {

    companion object {
        @JvmStatic
        fun newInstance() =
            FridgeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun initViews() {
        binding.fabAddIngredient.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, SearchIngredientsActivity::class.java))
            }
        }
    }
    override fun initListeners() = Unit
}