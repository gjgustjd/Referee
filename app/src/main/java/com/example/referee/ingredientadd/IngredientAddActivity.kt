package com.example.referee.ingredientadd

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        initUnitRecyclerView()
        initExpirationSpinner()
    }

    private fun initExpirationSpinner() {
        val expirationUnits = resources.getStringArray(R.array.ingredient_expiration_unit)
        with(binding.spExpiration) {
            adapter = ArrayAdapter(
                this@IngredientAddActivity,
                R.layout.item_spinner,
                expirationUnits
            )
        }
    }

    private fun initUnitRecyclerView() {
        val units = resources.getStringArray(R.array.ingredient_unit)
        with(binding.rvUnits) {
            layoutManager =
                LinearLayoutManager(this@IngredientAddActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = IngredientUnitAdapter(this@IngredientAddActivity, units).apply {
                setHasStableIds(true)
            }
            addItemDecoration(object :RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)

                    if (position != units.lastIndex) {
                        outRect.right = 30
                    }
                }
            })
        }
    }
}