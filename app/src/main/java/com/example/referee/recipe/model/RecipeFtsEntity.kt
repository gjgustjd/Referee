package com.example.referee.recipe.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import com.example.referee.common.DataBaseConst

@Entity(tableName = DataBaseConst.TABLE_NAME_FTS_RECIPES)
@Fts4(contentEntity = RecipeEntity::class)
data class RecipeFtsEntity(
    @ColumnInfo("RCP_SNO") val recipeNo: Int,
    @ColumnInfo("RCP_TTL") val recipeName: String?,
    @ColumnInfo("CKG_NM") val dishName: String?,
    @ColumnInfo("CKG_MTH_ACTO_NM") val cookMethod: String?,
    @ColumnInfo("CKG_STA_ACTO_NM") val cookUsage: String?,
    @ColumnInfo("CKG_MTRL_ACTO_NM") val cookIngredientGroup: String?,
    @ColumnInfo("CKG_KND_ACTO_NM") val cookType: String?,
    @ColumnInfo("CKG_IPDC") val dishDescription: String?,
    @ColumnInfo("CKG_MTRL_CN") val ingredients: String?,
    @ColumnInfo("CKG_INBUN_NM") val servings: String?,
    @ColumnInfo("CKG_DODF_NM") val cookLevel: String?,
    @ColumnInfo("CKG_TIME_NM") val cookTime: String?,
)