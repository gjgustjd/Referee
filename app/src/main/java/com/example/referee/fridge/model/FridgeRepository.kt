package com.example.referee.fridge.model

import com.example.referee.common.base.BaseLocalRepository

object FridgeRepository: BaseLocalRepository() {

    @Synchronized
    fun insertIngredientToFridge(entity: FridgeIngredientEntity) =
        db.fridgeDAO().insert(entity)

    @Synchronized
    fun getFridgeItems() = db.fridgeDAO().getFridgeIngredients()
}