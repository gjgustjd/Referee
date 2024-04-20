package com.example.referee.fridge.model

import androidx.room.Dao
import androidx.room.Query
import com.example.referee.common.model.BaseDAO
import kotlinx.coroutines.flow.Flow

@Dao
interface FridgeDAO:BaseDAO<FridgeIngredientEntity> {

    @Query("SELECT * FROM fridge")
    fun getFridgeIngredients(): Flow<List<FridgeIngredientEntity>>
}