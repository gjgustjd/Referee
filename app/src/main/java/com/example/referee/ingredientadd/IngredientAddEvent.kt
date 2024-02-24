package com.example.referee.ingredientadd

sealed interface IngredientAddEvent {
    object InsertSuccess:IngredientAddEvent
    object InsertFailed:IngredientAddEvent

    class IsThereIngredient(val value:Boolean):IngredientAddEvent
}