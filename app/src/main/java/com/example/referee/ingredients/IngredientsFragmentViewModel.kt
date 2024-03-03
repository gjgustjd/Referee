package com.example.referee.ingredients

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientRepository
import com.example.referee.ingredients.model.IngredientFragFABState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngredientsFragmentViewModel :
    BaseViewModel<IngredientsEvent>() {

    val fabState =
        MutableLiveData<EventWrapper<IngredientFragFABState>>(EventWrapper(IngredientFragFABState.None))

    fun getIngredientsList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("DeleteTest", "getIngredientList")
                IngredientRepository.getIngredientsList().collect { ingredients ->
                    _sharedFlow.emit(EventWrapper(IngredientsEvent.GetIngredients.Success(ingredients)))
                    Log.i("DeleteTest", "getIngredientList emit")
                }
            } catch (e: Exception) {
                Log.i("DeleteTest", "getIngredientList failed")
                _sharedFlow.emit(EventWrapper(IngredientsEvent.GetIngredients.Failed))
            }
        }
    }

    fun removeIngredient(item:IngredientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                IngredientRepository.removeIngredient(item)
                EventWrapper(IngredientsEvent.DeleteIngredients.Success)
            } catch (e:java.lang.Exception) {
                EventWrapper(IngredientsEvent.DeleteIngredients.Failed)
            }

            _sharedFlow.emit(result)
        }
    }
    fun removeIngredients(items: List<IngredientEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                Log.i("DeleteTest","removeIngredients items:$items")
                IngredientRepository.removeIngredients(items)
                EventWrapper(IngredientsEvent.DeleteIngredients.Success)
            } catch (e: java.lang.Exception) {
                Log.i("DeleteTest",e.toString())
                EventWrapper(IngredientsEvent.DeleteIngredients.Failed)
            }

            _sharedFlow.emit(result)
        }
    }
}