package com.example.referee.recipe.search

import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.recipe.model.RecipeRepository
import com.example.referee.recipe.search.model.SearchRecipeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchRecipeViewModel :BaseViewModel<SearchRecipeEvent>(){

    fun getRecipesByTitle(title:String) {
        viewModelScope.launch(Dispatchers.IO) {
            RecipeRepository.getRecipesByTitle(title).collect {
                _event.postValue(EventWrapper<SearchRecipeEvent>(SearchRecipeEvent.SearchRecipeSuccess(it)))
            }
        }
    }
}