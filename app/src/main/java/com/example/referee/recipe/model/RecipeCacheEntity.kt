package com.example.referee.recipe.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.referee.common.DataBaseConst

@Entity(tableName = DataBaseConst.TABLE_NAME_RECIPES_CACHE)
data class RecipeCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("order")
    val order: Int,
    @Embedded
    val recipe: RecipeEntity
)