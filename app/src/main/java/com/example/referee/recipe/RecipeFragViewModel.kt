package com.example.referee.recipe

import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.fridge.model.FridgeRepository
import com.example.referee.recipe.model.RecipeEvent
import com.example.referee.recipe.model.RecipeRepository
import kotlinx.coroutines.launch

class RecipeFragViewModel :BaseViewModel<RecipeEvent>(){

    fun getRecipesByIngredients() {
        viewModelScope.launch {
            FridgeRepository.getFridgeItems().collect { ingredients ->
                val fridgeIngredientName = ingredients.map { it.name }.first()

                RecipeRepository.getRecipesByIngredient(fridgeIngredientName)
                    .collect {
                        _event.value = EventWrapper(RecipeEvent.RecipeSuccess(it))
                    }
            }
        }
    }
}