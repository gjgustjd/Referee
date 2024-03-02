package com.example.referee.ingredients

import android.graphics.Bitmap
import com.example.referee.ingredientadd.model.IngredientEntity

sealed interface IngredientsEvent {

    sealed interface GetIngredients : IngredientsEvent {
        class Success(val ingredients: List<IngredientEntity>) : GetIngredients
        object Failed : GetIngredients
    }

    class IngredientBitmap(val position: Int, val bitmap: Bitmap) : IngredientsEvent

    sealed interface DeleteIngredients : IngredientsEvent {
        object Success : DeleteIngredients
        object Failed : DeleteIngredients
    }
}