package com.example.referee

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.referee.databinding.ActivityAddIngredientBinding

class IngredientAddActivity:AppCompatActivity() {
    lateinit var  binding:ActivityAddIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_add_ingredient)
    }
}