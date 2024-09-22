package com.example.referee.recipe.model

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.referee.common.DataBaseConst
import com.example.referee.common.Logger
import com.example.referee.common.base.BaseLocalRepository
import kotlinx.coroutines.flow.Flow

object RecipeRepository:BaseLocalRepository() {

    @Synchronized
    fun getRecipesByTitle(title: String): Flow<List<RecipeEntity>> {
        return db.recipeDAO().getRecipesByTitleFts(title)
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
        val keywords = ingredients.joinToString(" OR ")
        val query =
            """
                SELECT 
                    ${DataBaseConst.TABLE_NAME_RECIPES}.*,
                    replace(quote(matchInfo(${DataBaseConst.TABLE_NAME_FTS_RECIPES},'b')),'0','') AS MatchCount
                FROM ${DataBaseConst.TABLE_NAME_RECIPES}
                JOIN ${DataBaseConst.TABLE_NAME_FTS_RECIPES} 
                    ON recipes.ID = ${DataBaseConst.TABLE_NAME_FTS_RECIPES}.rowid
                WHERE ${DataBaseConst.TABLE_NAME_FTS_RECIPES}.CKG_MTRL_CN MATCH '$keywords'
                ORDER BY MatchCount DESC
                LIMIT $limit OFFSET $offset
            """.trimIndent()
        Logger.i("query:$query")
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