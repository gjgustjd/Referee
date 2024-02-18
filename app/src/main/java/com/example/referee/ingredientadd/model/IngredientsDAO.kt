package com.example.referee.ingredientadd.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IngredientsDAO {
    @Query("SELECT * FROM ingredients")
    fun getIngredientList():List<IngredientEntity>

    @Insert
    fun insertIngredient(item:IngredientEntity)

    @Delete
    fun deleteIngredient(item:IngredientEntity)

    @Update
    fun updateIngredient(item:IngredientEntity)
}