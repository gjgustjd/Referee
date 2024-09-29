package com.example.referee.recipe.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.referee.common.model.BaseDAO
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeCacheDAO : BaseDAO<RecipeCacheEntity> {

    @Transaction
    @Query(
        """
       SELECT
            *
       FROM recipes_cache
       ORDER BY `order` ASC
        """
    )
    fun getCachedRecipes(): Flow<List<RecipeEntity>>

    @Transaction
    @Query("DELETE FROM recipes_cache")
    fun clearRecipeCache()
}