package com.example.referee.recipe.model

import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.flow.Flow

object RecipeRepository:BaseLocalRepository() {

    fun getRecipesByTitle(title: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByTitle(title)
    }

    fun getRecipesByIngredients(ingredients: List<String>): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByIngredients(ingredients)
    }

    fun getRecipesByType(type: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByType(type)
    }

    fun getRecipesByMethod(method: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByMethod(method)
    }
}