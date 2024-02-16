package com.example.referee.ingredientadd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.referee.R
import com.example.referee.databinding.ActivityAddIngredientBinding

class IngredientAddActivity:AppCompatActivity() {
    lateinit var binding: ActivityAddIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ingredient)
        initViews()
    }

    private fun initViews() {
        val units = resources.getStringArray(R.array.ingredient_unit)
        binding.rvUnits.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUnits.adapter = IngredientUnitAdapter(units)
    }
}