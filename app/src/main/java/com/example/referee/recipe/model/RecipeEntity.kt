package com.example.referee.recipe.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("ID") val id:Int,
    @ColumnInfo("RCP_SNO") val recipe_no: Int,
    @ColumnInfo("RCP_TTL") val recipeName: String? = null,
    @ColumnInfo("CKG_NM") val dishName: String? = null,
    @ColumnInfo("CKG_MTH_ACTO_NM") val cookMethod: String? = null,
    @ColumnInfo("CKG_STA_ACTO_NM") val cookUsage: String? = null,
    @ColumnInfo("CKG_MTRL_ACTO_NM") val cookIngredientGroup: String? = null,
    @ColumnInfo("CKG_KND_ACTO_NM") val cookType: String? = null,
    @ColumnInfo("CKG_IPDC") val dishDescription: String? = null,
    @ColumnInfo("CKG_MTRL_CN") val ingredients: String? = null,
    @ColumnInfo("CKG_INBUN_NM") val servings: String? = null,
    @ColumnInfo("CKG_DODF_NM") val cookLevel: String? = null,
    @ColumnInfo("CKG_TIME_NM") val cookTime: String? = null
)