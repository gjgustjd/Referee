package com.example.referee.ingredientadd.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.referee.common.model.BaseDAO
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientsDAO :BaseDAO<IngredientEntity> {
    @Query("SELECT * FROM ingredients")
    fun getIngredientList(): Flow<List<IngredientEntity>>

    @Query("SELECT * FROM ingredients where name=:ingName")
    fun getIngredientByName(ingName: String): List<IngredientEntity>
}