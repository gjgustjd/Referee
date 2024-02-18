package com.example.referee.ingredientadd

sealed interface IngredientAddEvent {
    object InsertSuccess:IngredientAddEvent
    object InsertFailed:IngredientAddEvent
}