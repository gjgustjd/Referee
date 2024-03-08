package com.example.referee.main

import android.os.Bundle
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentFridgeBinding

class FridgeFragment : BaseFragment<FragmentFridgeBinding>(R.layout.fragment_fridge) {

    override fun initViews() = Unit
    override fun initListeners() = Unit

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FridgeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}