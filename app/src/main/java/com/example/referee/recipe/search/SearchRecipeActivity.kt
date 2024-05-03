package com.example.referee.recipe.search

import android.content.Context
import android.content.Intent
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivitySearchItemBinding

class SearchRecipeActivity :BaseActivity<ActivitySearchItemBinding>(R.layout.activity_search_item){

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context,SearchRecipeActivity::class.java)
        }
    }

    override fun initViews() {
        with(binding) {
            title = getString(R.string.recipe_search_title)
            etKeyword.hint = getString(R.string.recipe_search_item)
        }
    }
}