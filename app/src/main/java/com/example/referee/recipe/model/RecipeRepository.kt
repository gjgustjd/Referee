package com.example.referee.recipe.model

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
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
        val keywords = arrayListOf<String>()

        for (i in 1..ingredients.size) {
            keywords.add(ingredients.take(i).joinToString(" "))
        }

        val matchCases =
            keywords.joinToString(" UNION ") { keyword ->
                """
                    SELECT recipes.*,matchInfo(fts_recipes,'p') AS MatchInfo
                    FROM recipes
                    JOIN fts_recipes ON recipes.ID = fts_recipes.rowid
                    WHERE fts_recipes.CKG_MTRL_CN MATCH '$keyword'
                """
            }
        val query =
            """
                WITH matchCases AS($matchCases)
                SELECT * FROM matchCases ORDER BY hex(MatchInfo) DESC LIMIT $limit OFFSET $offset
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