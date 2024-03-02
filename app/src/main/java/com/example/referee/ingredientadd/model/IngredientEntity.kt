package com.example.referee.ingredientadd.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ingredients")
data class IngredientEntity(
    val name: String,
    val photoName: String? = null,
    val unit: String,
    val expiration: Int,
    val category: Int? = null,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    @Transient
    var imageBitmap: Bitmap? = null
}