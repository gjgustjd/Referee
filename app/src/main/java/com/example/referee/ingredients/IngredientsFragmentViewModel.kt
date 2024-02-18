package com.example.referee.ingredients

import androidx.lifecycle.viewModelScope
import com.example.referee.common.BaseViewModel
import com.example.referee.common.EventWrapper
import com.example.referee.common.RefereeApplication
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class IngredientsFragmentViewModel(application: RefereeApplication) :
    BaseViewModel<IngredientsEvent>(application) {

    fun getIngredientsList() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.async(Dispatchers.IO) {
                val result = IngredientRepository.getIngredientsList()
                _event.value = result?.let {
                    EventWrapper(IngredientsEvent.GetIngredients.Success(it))
                } ?: run {
                    EventWrapper(IngredientsEvent.GetIngredients.Failed)
                }
            }
        }
    }
}