package com.example.referee.ingredientadd

import android.graphics.Bitmap

sealed interface IngredientAddEvent {
    object InsertSuccess:IngredientAddEvent
    object InsertFailed:IngredientAddEvent

    object UpdateSuccess : IngredientAddEvent
    object UpdateFailed : IngredientAddEvent

    class IsThereIngredient(val value:Boolean):IngredientAddEvent

    class IngredientBitmap(val bitmap: Bitmap) : IngredientAddEvent
}