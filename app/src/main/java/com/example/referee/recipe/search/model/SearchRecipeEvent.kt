package com.example.referee.recipe.search.model

import com.example.referee.recipe.model.RecipeEntity

sealed interface SearchRecipeEvent {

    class SearchRecipeSuccess(val recipes:List<RecipeEntity>):SearchRecipeEvent
}