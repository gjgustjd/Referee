package com.example.referee.recipe

import android.os.Bundle
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentCookBinding

class RecipeFragment : BaseFragment<FragmentCookBinding>(R.layout.fragment_cook) {

    override fun initViews() = Unit
    override fun initListeners() = Unit


    companion object {
        @JvmStatic
       fun newInstance(param1: String, param2: String) =
            RecipeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}