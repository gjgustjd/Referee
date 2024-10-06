package com.example.referee.fridge.model

import androidx.room.Transaction
import com.example.referee.common.applicationScope
import com.example.referee.common.base.BaseLocalRepository
import com.example.referee.recipe.model.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

object FridgeRepository: BaseLocalRepository() {

    val fridgeItems: StateFlow<List<FridgeIngredientEntity>> by lazy {
        getFridgeItems().stateIn(
            scope = applicationScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed()
        )
    }

    @Synchronized
    @Transaction
    fun insertIngredientToFridge(entity: FridgeIngredientEntity):Long {
        RecipeRepository.clearRecipeCache()
        return db.fridgeDAO().insert(entity)
    }

    private fun getFridgeItems() = db.fridgeDAO().getFridgeIngredients()
}