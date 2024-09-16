package com.example.referee.recipe.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipes WHERE RCP_TTL LIKE  '%' || :title || '%' LIMIT :limit OFFSET :offset")
    fun getRecipesByTitle(title: String, limit: Int = 20, offset: Int = 0): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE CKG_MTRL_CN LIKE  '%' || :ingredient || '%'")
    fun getRecipesByIngredient(ingredient: String): Flow<List<RecipeEntity>>

    @Transaction
    @Query(
        """
        SELECT recipes.* FROM recipes 
        JOIN fts_recipes ON recipes.ID = fts_recipes.rowid 
        WHERE fts_recipes.RCP_TTL MATCH :title
        """
    )
    fun getRecipesByTitleFts(title:String):Flow<List<RecipeEntity>>

    @RawQuery
    fun excueteRawQuery(query: SupportSQLiteQuery): List<RecipeEntity>
//    fun getRecipesByIngredients(ingredients: List<String>): Flow<List<RecipeEntity>>
//    fun getRecipesByType(type:String):Flow<List<RecipeEntity>>
//    fun getRecipesByMethod(method:String):Flow<List<RecipeEntity>>
}