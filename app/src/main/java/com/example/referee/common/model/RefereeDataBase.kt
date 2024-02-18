package com.example.referee.common.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientsDAO

@Database(entities = [IngredientEntity::class], version = 1)
abstract class RefereeDataBase : RoomDatabase() {
    abstract fun ingredientsDAO():IngredientsDAO
}