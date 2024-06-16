package com.example.referee.fridge.model

import com.example.referee.common.applicationScope
import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

object FridgeRepository: BaseLocalRepository() {

    val fridgeItems: SharedFlow<List<FridgeIngredientEntity>> by lazy {
        getFridgeItems().shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )
    }

    @Synchronized
    fun insertIngredientToFridge(entity: FridgeIngredientEntity) =
        db.fridgeDAO().insert(entity)

    private fun getFridgeItems() = db.fridgeDAO().getFridgeIngredients()
}