package com.example.referee.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID") val id:Int,
    @SerializedName("RCP_SNO") val recipe_no: Int,
    @SerializedName("RCP_TTL") val recipeName: String? = null,
    @SerializedName("CKG_NM") val dishName: String? = null,
    @SerializedName("CKG_MTH_ACTO_NM") val cookMethod: String? = null,
    @SerializedName("CKG_STA_ACTO_NM") val cookUsage: String? = null,
    @SerializedName("CKG_MTRL_ACTO_NM") val cookIngredientGroup: String? = null,
    @SerializedName("CKG_KND_ACTO_NM") val cookType: String? = null,
    @SerializedName("CKG_IPDC") val dishDescription: String? = null,
    @SerializedName("CKG_MTRL_CN") val ingredients: String? = null,
    @SerializedName("CKG_INBUN_NM") val servings: String? = null,
    @SerializedName("CKG_DODF_NM") val cookLevel: String? = null,
    @SerializedName("CKG_TIME_NM") val cookTime: String? = null
)