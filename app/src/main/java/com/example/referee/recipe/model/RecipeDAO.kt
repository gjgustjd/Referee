package com.example.referee.recipe.model

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipes WHERE RCP_TTL LIKE  '%' || :title || '%'")
    fun getRecipesByTitle(title: String): Flow<List<RecipeEntity>>
    @Query("SELECT * FROM recipes WHERE CKG_MTRL_CN LIKE  '%' || :ingredient || '%'")
    fun getRecipesByIngredient(ingredient: String): Flow<List<RecipeEntity>>
    fun getRecipesByIngredients(ingredients: List<String>): Flow<List<RecipeEntity>>
    fun getRecipesByType(type:String):Flow<List<RecipeEntity>>
    fun getRecipesByMethod(method:String):Flow<List<RecipeEntity>>
}