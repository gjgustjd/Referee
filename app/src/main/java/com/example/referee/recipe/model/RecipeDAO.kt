package com.example.referee.recipe.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Transaction
    @Query(
        """
        SELECT recipes.* FROM recipes 
        JOIN fts_recipes ON recipes.ID = fts_recipes.rowid 
        WHERE fts_recipes.RCP_TTL MATCH :title
        """
    )
    fun getRecipesByTitleFts(title:String):Flow<List<RecipeEntity>>

    @Transaction
    @Query(
        """
        SELECT 
            recipes.*,
            replace(quote(matchInfo(fts_recipes,'b')),'0','') AS MatchCount
        FROM recipes
        JOIN fts_recipes 
            ON recipes.ID = fts_recipes.rowid
        WHERE fts_recipes.CKG_MTRL_CN 
        MATCH 
            (
            SELECT GROUP_CONCAT(name,' OR ') AS concatResult
            FROM fridge
            )
        ORDER BY MatchCount DESC
        LIMIT :limit OFFSET :offset
        """
    )
    fun getRecipesContainsFridgeIngredients(
        limit: Int = 10,
        offset: Int = 0
    ): List<RecipeEntity>

    @RawQuery
    fun excueteRawQuery(query: SupportSQLiteQuery): List<RecipeEntity>
//    fun getRecipesByIngredients(ingredients: List<String>): Flow<List<RecipeEntity>>
//    fun getRecipesByType(type:String):Flow<List<RecipeEntity>>
//    fun getRecipesByMethod(method:String):Flow<List<RecipeEntity>>
}