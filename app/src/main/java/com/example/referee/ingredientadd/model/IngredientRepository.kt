package com.example.referee.ingredientadd.model

import com.example.referee.common.RefereeApplication
import com.example.referee.common.model.RefereeDataBase

object IngredientRepository {
    private val db by lazy {
        val application = RefereeApplication.instance()
        RefereeDataBase.getInstance(application)
    }

    fun insertIngredient(item: IngredientEntity) = db.ingredientsDAO().insert(item)

    fun getIngredientsList() = db.ingredientsDAO().getIngredientList()
    fun getIngredientsByName(name:String) = db.ingredientsDAO().getIngredientByName(name)

    fun removeIngredient(item: IngredientEntity) = db.ingredientsDAO().delete(item)
    fun removeIngredients(items: List<IngredientEntity>) = db.ingredientsDAO().deleteList(items)

    fun updateIngredient(item: IngredientEntity) = db.ingredientsDAO().update(item)
}