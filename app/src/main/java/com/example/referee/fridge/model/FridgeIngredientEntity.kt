package com.example.referee.fridge.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "fridge")
@Parcelize
data class FridgeIngredientEntity(
    val name: String,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
) : Parcelable {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}