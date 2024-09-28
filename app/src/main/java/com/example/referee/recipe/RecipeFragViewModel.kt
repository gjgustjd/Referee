package com.example.referee.recipe

import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.Logger
import com.example.referee.common.base.BaseViewModel
import com.example.referee.fridge.model.FridgeRepository
import com.example.referee.recipe.model.RecipeEvent
import com.example.referee.recipe.model.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeFragViewModel : BaseViewModel<RecipeEvent>() {

    fun getRecipesByFridgeIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            FridgeRepository.fridgeItems.collect {
                Logger.i()
                val recipes = RecipeRepository.getRecipesByFridgeIngredientNames()
                _event.postValue(EventWrapper(RecipeEvent.RecipeSuccess(recipes)))
            }
        }
    }
}