package com.example.referee.ingredientadd.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientsDAO {
    @Query("SELECT * FROM ingredients")
    fun getIngredientList(): Flow<List<IngredientEntity>>

    @Query("SELECT * FROM ingredients where name=:ingName")
    fun getIngredientByName(ingName: String): List<IngredientEntity>

    @Insert
    fun insertIngredient(item:IngredientEntity):Long

    @Delete
    fun deleteIngredient(item:IngredientEntity)

    @Delete
    fun deleteIngredients(items: List<IngredientEntity>)

    @Update
    fun updateIngredient(item:IngredientEntity):Int
}