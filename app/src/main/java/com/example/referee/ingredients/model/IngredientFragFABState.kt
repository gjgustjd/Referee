package com.example.referee.ingredients.model

sealed interface IngredientFragFABState {
    object None : IngredientFragFABState
    object SubMenu : IngredientFragFABState
    object SearchMenu : IngredientFragFABState
    object DeleteMenu : IngredientFragFABState
}