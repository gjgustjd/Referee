package com.example.referee.ingredients

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class IngredientsFragmentViewModel :
    BaseViewModel<IngredientsEvent>() {

    fun getIngredientsList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async(Dispatchers.IO) {
                    IngredientRepository.getIngredientsList()
                }.await()
                Log.i("ResultTest", result.toString())
                _event.postValue(EventWrapper(IngredientsEvent.GetIngredients.Success(result)))
            } catch (e: Exception) {
                _event.postValue(EventWrapper(IngredientsEvent.GetIngredients.Failed))
            }
        }
    }
}