package com.example.referee.ingredientadd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    val name: String,
    val photoName: String? = null,
    val unit: String,
    val expiration: Int,
    val category: Int? = null
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}