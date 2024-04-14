package com.example.referee.fridge.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fridge")
data class FridgeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
)