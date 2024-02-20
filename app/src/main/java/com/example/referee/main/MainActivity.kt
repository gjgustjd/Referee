package com.example.referee.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityMainBinding
import com.example.referee.ingredients.IngredientsFragment

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment = FridgeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
        initListeners()
    }

    private fun initView() {
       supportFragmentManager.commit {
           replace(R.id.fragmentMain,currentFragment)
       }
    }

    private fun initListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            currentFragment = when (it.itemId) {
                R.id.menu_ingredients -> IngredientsFragment()
                R.id.menu_fridge -> FridgeFragment()
                R.id.menu_cook -> CookFragment()
                else -> return@setOnItemSelectedListener false
            }

            supportFragmentManager.commit {
                replace(R.id.fragmentMain, currentFragment)
            }

            return@setOnItemSelectedListener true
       }
    }
}