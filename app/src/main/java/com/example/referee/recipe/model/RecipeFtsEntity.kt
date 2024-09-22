package com.example.referee.recipe.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import com.example.referee.common.DataBaseConst

@Entity(tableName = DataBaseConst.TABLE_NAME_FTS_RECIPES)
@Fts4(contentEntity = RecipeEntity::class)
data class RecipeFtsEntity(
    @ColumnInfo("RCP_TTL") val recipeName: String?,
    @ColumnInfo("CKG_NM") val dishName: String?,
    @ColumnInfo("CKG_MTRL_CN") val ingredients: String?,
)