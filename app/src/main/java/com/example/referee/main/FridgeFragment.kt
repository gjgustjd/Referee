package com.example.referee.main

import android.os.Bundle
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentFridgeBinding

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
    }
    override fun initListeners() = Unit
}