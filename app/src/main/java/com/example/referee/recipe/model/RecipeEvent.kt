package com.example.referee.recipe.model

sealed interface RecipeEvent {

    class RecipeSuccess(val recipes:List<RecipeEntity>):RecipeEvent
    object RecipeFailure:RecipeEvent
}