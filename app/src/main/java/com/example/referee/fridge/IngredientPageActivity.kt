package com.example.referee.fridge

import android.content.Context
import android.content.Intent
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityIngredientWebPageBinding
import com.example.referee.network.LinkUtils

class IngredientPageActivity : BaseActivity<ActivityIngredientWebPageBinding>(R.layout.activity_ingredient_web_page) {

    companion object {
       const val EXTRA_INGREDIENT_TITLE="EXTRA_INGREDIENT_TITLE"

        fun newIntent(context: Context, title: String): Intent {
            return Intent(context, IngredientPageActivity::class.java).apply {
                putExtra(EXTRA_INGREDIENT_TITLE, title)
            }
        }
    }

    override fun initViews() {
        binding.title = intent.extras?.getString(EXTRA_INGREDIENT_TITLE)
        binding.wvContent.loadUrl(LinkUtils.MEDIAWIKI_PAGE_URL + title)
        binding.viewTopBar.ivBackButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.btnInsertToFridge.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}