package com.example.referee.ingredientadd.model

import com.example.referee.common.base.BaseLocalRepository

object IngredientRepository:BaseLocalRepository() {

    @Synchronized
    fun insertIngredient(item: IngredientEntity) = db.ingredientsDAO().insert(item)

    @Synchronized
    fun getIngredientsList() = db.ingredientsDAO().getIngredientList()

    @Synchronized
    fun getIngredientsByName(name:String) = db.ingredientsDAO().getIngredientByName(name)

    @Synchronized
    fun removeIngredient(item: IngredientEntity) = db.ingredientsDAO().delete(item)

    @Synchronized
    fun removeIngredients(items: List<IngredientEntity>) = db.ingredientsDAO().deleteList(items)

    @Synchronized
    fun updateIngredient(item: IngredientEntity) = db.ingredientsDAO().update(item)
}