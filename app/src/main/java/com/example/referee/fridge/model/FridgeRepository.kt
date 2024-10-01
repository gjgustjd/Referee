package com.example.referee.fridge.model

import com.example.referee.common.applicationScope
import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object FridgeRepository: BaseLocalRepository() {

    val fridgeItems: StateFlow<List<FridgeIngredientEntity>> by lazy {
        getFridgeItems().stateIn(
            scope = applicationScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed()
        )
    }

    @Synchronized
    fun insertIngredientToFridge(entity: FridgeIngredientEntity) =
        db.fridgeDAO().insert(entity)

    private fun getFridgeItems() = db.fridgeDAO().getFridgeIngredients()
}