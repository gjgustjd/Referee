package com.example.referee.fridge.model

import com.example.referee.common.base.BaseLocalRepository

object FridgeRepository: BaseLocalRepository() {

    fun insertIngredientToFridge(entity: FridgeIngredientEntity) =
        db.fridgeDAO().insert(entity)

    fun getFridgeItems() = db.fridgeDAO().getFridgeIngredients()
}