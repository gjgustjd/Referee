package com.example.referee.ingredientadd.model

import com.example.referee.common.RefereeApplication
import com.example.referee.common.model.RefereeDataBase

object IngredientRepository {
    private val db by lazy {
        val application = RefereeApplication.instance()
        RefereeDataBase.getInstance(application)
    }

    fun insertIngredient(item: IngredientEntity) = db.ingredientsDAO().insertIngredient(item)

    fun getIngredientsList() = db.ingredientsDAO().getIngredientList()

    fun removeIngredient(item: IngredientEntity) = db.ingredientsDAO().deleteIngredient(item)

    fun updateIngredient(item: IngredientEntity) = db.ingredientsDAO().updateIngredient(item)
}