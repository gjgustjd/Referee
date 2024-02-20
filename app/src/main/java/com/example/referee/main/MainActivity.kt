package com.example.referee.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityMainBinding
import com.example.referee.ingredients.IngredientsFragmentDirections

class MainActivity : BaseActivity() {

    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initListeners()
    }

    private fun initListeners() {
       binding.bottomNavigationView.setOnItemSelectedListener {
           val action = when (it.itemId) {
               R.id.menu_ingredients -> {
                  when (binding.bottomNavigationView.selectedItemId) {
                       R.id.menu_fridge -> {
                           FridgeFragmentDirections.actionFridgeFragmentToIngredientsFragment()
                       }

                       R.id.menu_cook -> {
                           CookFragmentDirections.actionCookFragmentToIngredientsFragment()
                       }
                       else -> null
                   }
               }

               R.id.menu_fridge -> {
                   when (binding.bottomNavigationView.selectedItemId) {
                       R.id.menu_ingredients -> {
                           IngredientsFragmentDirections.actionIngredientsFragmentToFridgeFragment()
                       }

                       R.id.menu_cook -> {
                           CookFragmentDirections.actionCookFragmentToFridgeFragment()
                       }

                       else -> null
                   }
               }

               R.id.menu_cook -> {
                   when (binding.bottomNavigationView.selectedItemId) {
                       R.id.menu_ingredients -> {
                           IngredientsFragmentDirections.actionIngredientsFragmentToCookFragment()
                       }

                       R.id.menu_fridge -> {
                           FridgeFragmentDirections.actionFridgeFragmentToCookFragment()
                       }

                       else -> null
                   }
               }

               else -> null
           }

           action?.let {
               Navigation.findNavController(binding.navHostFragment).navigate(action)
               true
           } ?: run {
               false
           }
       }
    }
}