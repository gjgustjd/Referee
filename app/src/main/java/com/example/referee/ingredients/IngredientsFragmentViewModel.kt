package com.example.referee.ingredients

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.RefereeApplication
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientRepository
import com.example.referee.ingredients.model.IngredientFragFABState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class IngredientsFragmentViewModel :
    BaseViewModel<IngredientsEvent>() {

    private var _bitmapFlow: MutableSharedFlow<IngredientsEvent.IngredientBitmap?> =
        MutableSharedFlow(0)
    val bitmapFlow: SharedFlow<IngredientsEvent.IngredientBitmap?> = _bitmapFlow
    val fabState =
        MutableLiveData<EventWrapper<IngredientFragFABState>>(EventWrapper(IngredientFragFABState.None))

    fun getIngredientsList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                IngredientRepository.getIngredientsList().collect { ingredients ->
                    _event.postValue(EventWrapper(IngredientsEvent.GetIngredients.Success(ingredients)))
                }
            } catch (e: Exception) {
                _event.postValue(EventWrapper(IngredientsEvent.GetIngredients.Failed))
            }
        }
    }

    fun getImageBitmap(imageName:String,position:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storage = RefereeApplication.instance.applicationContext.cacheDir
                val path = "${storage}/$imageName"
                val bitmap = BitmapFactory.decodeFile(path)
                _bitmapFlow.emit(IngredientsEvent.IngredientBitmap(position, bitmap))
            } catch (e: java.lang.Exception) {
            }
        }
    }
}