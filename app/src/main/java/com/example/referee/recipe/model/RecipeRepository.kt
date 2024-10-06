package com.example.referee.recipe.model

import com.example.referee.common.applicationScope
import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

object RecipeRepository:BaseLocalRepository() {

    val recipes: StateFlow<List<RecipeEntity>> by lazy {
        getCachedRecipes().stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    }

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

    private fun getCachedRecipes(): Flow<List<RecipeEntity>> {
        return db.recipeCacheDAO().getCachedRecipes()
    }

    fun cacheRecipes(recipes: List<RecipeEntity>) {
        val recipeCacheEntities = recipes.map { RecipeCacheEntity(recipe = it) }
        db.recipeCacheDAO().insertList(recipeCacheEntities)
    }

    fun clearRecipeCache() = db.recipeCacheDAO().clearRecipeCache()
}