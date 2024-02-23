package com.example.referee.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityMainBinding
import com.example.referee.ingredients.IngredientsFragment

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private var fridgeFragment: FridgeFragment? = null
    private var cookFragment: CookFragment? = null
    private var ingredientsFragment: IngredientsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
        initListeners()
    }

    private fun initView() {
        supportFragmentManager.commit {
            fridgeFragment?.let { fridge ->
                hideOtherFragment(fridgeFragment)
                show(fridge)
            } ?: run {
                fridgeFragment = FridgeFragment()
                fridgeFragment?.let {
                    add(R.id.fragmentMain, it)
                }
            }
            title = getString(R.string.navigation_menu_fridger)
       }
    }

    private fun initListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            val fm = supportFragmentManager.beginTransaction()
            var menuTitle: String? = null
            val currentFragment = when (it.itemId) {
                R.id.menu_ingredients -> {
                    menuTitle = getString(R.string.navigation_menu_ingredient)
                    ingredientsFragment ?: run {
                        ingredientsFragment = IngredientsFragment()
                        ingredientsFragment?.apply {
                            fm.add(R.id.fragmentMain,this)
                        }
                    }
                }

                R.id.menu_fridge -> {
                    menuTitle = getString(R.string.navigation_menu_fridger)
                    fridgeFragment ?: run {
                        fridgeFragment = FridgeFragment()
                        fridgeFragment?.apply {
                            fm.add(R.id.fragmentMain,this)
                        }
                    }
                }

                R.id.menu_cook -> {
                    menuTitle = getString(R.string.navigation_menu_cook)
                    cookFragment ?: run {
                        cookFragment = CookFragment()
                        cookFragment?.apply {
                            fm.add(R.id.fragmentMain,this)
                        }
                    }
                }

                else -> return@setOnItemSelectedListener false
            }

            fm.apply {
                currentFragment?.let {fragment->
                    hideOtherFragment(fragment)
                    show(fragment)
                }
            }.commit()
            title = menuTitle

            return@setOnItemSelectedListener true
       }
    }

    private fun FragmentTransaction.hideOtherFragment(fragment: Fragment?) {
        val fragments = arrayOf(fridgeFragment,cookFragment,ingredientsFragment)
        fragments.filter { it != fragment }.onEach { it?.let(::hide) }
    }
}