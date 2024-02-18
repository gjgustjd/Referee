package com.example.referee.ingredients

import com.example.referee.ingredientadd.model.IngredientEntity

sealed interface IngredientsEvent {

    sealed interface GetIngredients : IngredientsEvent {
        class Success(val ingredients: List<IngredientEntity>) : GetIngredients
        object Failed : GetIngredients
    }
}