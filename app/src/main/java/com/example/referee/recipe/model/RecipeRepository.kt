package com.example.referee.recipe.model

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.flow.Flow

object RecipeRepository:BaseLocalRepository() {

    @Synchronized
    fun getRecipesByTitle(title: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByTitle(title)
    }

    @Synchronized
    fun getRecipesByIngredient(ingredient: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByIngredient(ingredient)
    }

    fun getRecipesByFridgeIngredientNames(
        ingredientNames:List<String>,
        limit: Int = 10,
        offset: Int = 0
    ): List<RecipeEntity> {
        val query = createMatchIngredientsQuery(ingredientNames, limit, offset)

        return db.recipeDAO().excueteRawQuery(query)
    }

    private fun createMatchIngredientsQuery(
        ingredients: List<String>,
        limit: Int = 10,
        offset: Int = 0
    ): SupportSQLiteQuery {
        val matchCases =
            ingredients.joinToString(" + ") { "CASE WHEN CKG_MTRL_CN LIKE '%$it%' THEN 1 ELSE 0 END" }
        val query =
            "SELECT *, ($matchCases) AS MatchCount FROM recipes ORDER BY MatchCount DESC LIMIT $limit OFFSET $offset"
        return SimpleSQLiteQuery(query)
    }

//    fun getRecipesByIngredients(ingredients: List<String>): Flow<List<RecipeEntity>> {
//        return db.recipeDAO().getRecipesByIngredients(ingredients)
//    }
//
//    fun getRecipesByType(type: String): Flow<List<RecipeEntity>> {
//        return db.recipeDAO().getRecipesByType(type)
//    }
//
//    fun getRecipesByMethod(method: String): Flow<List<RecipeEntity>> {
//        return db.recipeDAO().getRecipesByMethod(method)
//    }
}