package com.example.referee.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.referee.common.ContainerFragment
import com.example.referee.common.base.BaseFragment
import com.example.referee.ingredients.IngredientsFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments: List<ContainerFragment> = listOf(
        ContainerFragment().apply { childFragment = FridgeFragment() },
        ContainerFragment().apply { childFragment = CookFragment() },
        ContainerFragment().apply { childFragment = IngredientsFragment() }
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getBaseFragment(position: Int) = fragments[position].childFragment as BaseFragment<*>
}
