package com.example.referee.ingredientadd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val photoPath: String,
    val unit: String,
    val expiration: Int
)