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
       }
    }

    private fun initListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            val fm = supportFragmentManager.beginTransaction()
            val currentFragment = when (it.itemId) {
                R.id.menu_ingredients -> ingredientsFragment ?: run {
                    ingredientsFragment = IngredientsFragment()
                    ingredientsFragment?.apply {
                        fm.add(R.id.fragmentMain,this)
                    }
                }

                R.id.menu_fridge -> fridgeFragment ?: run {
                    fridgeFragment = FridgeFragment()
                    fridgeFragment?.apply {
                        fm.add(R.id.fragmentMain,this)
                    }
                }

                R.id.menu_cook -> cookFragment ?: run {
                    cookFragment = CookFragment()
                    cookFragment?.apply {
                        fm.add(R.id.fragmentMain,this)
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

            return@setOnItemSelectedListener true
       }
    }

    private fun FragmentTransaction.hideOtherFragment(fragment: Fragment?) {
        val fragments = arrayOf(fridgeFragment,cookFragment,ingredientsFragment)
        fragments.filter { it != fragment }.onEach { it?.let(::hide) }
    }
}