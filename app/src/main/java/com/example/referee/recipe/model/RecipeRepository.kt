package com.example.referee.recipe.model

import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.flow.Flow

object RecipeRepository:BaseLocalRepository() {

    @Synchronized
    fun getRecipesByTitle(title: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByTitleFts(title)
    }

    fun getRecipesByFridgeIngredientNames(
        limit: Int = 10,
        offset: Int = 0
    ): List<RecipeEntity> {
        return db.recipeDAO().getRecipesContainsFridgeIngredients(limit,offset)
    }

    fun getCachedRecipes(): Flow<List<RecipeEntity>> {
        return db.recipeCacheDAO().getCachedRecipes()
    }

    fun cacheRecipes(recipes: List<RecipeEntity>) {
        val recipeCacheEntities = recipes.map { RecipeCacheEntity(recipe = it) }
        db.recipeCacheDAO().insertList(recipeCacheEntities)
    }

    fun clearRecipeCache() = db.recipeCacheDAO().clearRecipeCache()
}