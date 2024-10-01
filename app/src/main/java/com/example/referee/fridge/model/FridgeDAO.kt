package com.example.referee.fridge.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.referee.common.model.BaseDAO
import kotlinx.coroutines.flow.Flow

@Dao
interface FridgeDAO:BaseDAO<FridgeIngredientEntity> {

    @Query("SELECT * FROM fridge")
    fun getFridgeIngredients(): Flow<List<FridgeIngredientEntity>>

    @Transaction
    fun insertIngredientAndClearRecipeCache(ingredient:FridgeIngredientEntity) {
        insert(ingredient)
        clearRecipeCache()
    }

    @Query("DELETE FROM recipes_cache")
    fun clearRecipeCache()
}