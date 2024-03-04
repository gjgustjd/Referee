package com.example.referee.main

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.referee.common.base.BaseFragment
import com.example.referee.ingredients.IngredientsFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments: List<BaseFragment<*>> = listOf(
        FridgeFragment(),
        CookFragment(),
        IngredientsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): BaseFragment<*> = fragments[position]
}
