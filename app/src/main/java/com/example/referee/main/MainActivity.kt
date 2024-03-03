package com.example.referee.main

import androidx.viewpager2.widget.ViewPager2
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initViews() {
        with(binding.vpMain) {
            adapter = MainPagerAdapter(this@MainActivity)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigationView.menu.getItem(position).isChecked = true
                }
            })
        }
    }

    override fun initListeners() {
        super.initListeners()

        binding.bottomNavigationView.setOnItemSelectedListener {
            val menuTitle: String
            binding.vpMain.currentItem =
                when (it.itemId) {
                    R.id.menu_fridge -> {
                        menuTitle = getString(R.string.navigation_menu_fridger)
                        0
                    }

                    R.id.menu_cook -> {
                        menuTitle = getString(R.string.navigation_menu_cook)
                        1
                    }

                    R.id.menu_ingredients -> {
                        menuTitle = getString(R.string.navigation_menu_ingredient)
                        2
                    }

                    else -> return@setOnItemSelectedListener false
                }
            title = menuTitle

            return@setOnItemSelectedListener true
        }
    }
}