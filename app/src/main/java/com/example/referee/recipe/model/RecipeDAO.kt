package com.example.referee.recipe.model

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    fun getRecipesByTitle(title: String): Flow<List<RecipeEntity>>
    fun getRecipesByIngredients(ingredients: List<String>): Flow<List<RecipeEntity>>
    fun getRecipesByType(type:String):Flow<List<RecipeEntity>>
    fun getRecipesByMethod(method:String):Flow<List<RecipeEntity>>
}