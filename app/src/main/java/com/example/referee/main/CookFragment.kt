package com.example.referee.main

import android.os.Bundle
import com.example.referee.R
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentCookBinding

class CookFragment : BaseFragment<FragmentCookBinding>() {

    override val layoutResourceId = R.layout.fragment_cook

    override fun initView() = Unit
    override fun initListeners() = Unit


    companion object {
        @JvmStatic
       fun newInstance(param1: String, param2: String) =
            CookFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}