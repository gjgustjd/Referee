package com.example.referee.ingredients

import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientsFragmentViewModel :
    BaseViewModel<IngredientsEvent>() {

    fun getIngredientsList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.IO) {
                    IngredientRepository.getIngredientsList().collect {
                        _event.postValue(EventWrapper(IngredientsEvent.GetIngredients.Success(it)))
                    }
                }
            } catch (e: Exception) {
                _event.postValue(EventWrapper(IngredientsEvent.GetIngredients.Failed))
            }
        }
    }
}