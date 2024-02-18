package com.example.referee.ingredientadd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    val name: String,
    val photoPath: ByteArray,
    val unit: String,
    val expiration: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}