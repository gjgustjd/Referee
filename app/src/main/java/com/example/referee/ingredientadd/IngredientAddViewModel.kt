package com.example.referee.ingredientadd

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientExpirationUnit
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception

class IngredientAddViewModel(application: Application) : AndroidViewModel(application) {
    private var _event: MutableLiveData<IngredientAddEvent> = MutableLiveData()
    val event: LiveData<IngredientAddEvent> = _event

    fun insertIngredient(
        name: String,
        photoPath: Bitmap,
        unit: String,
        expiration: IngredientExpirationUnit
    ) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        photoPath.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val item = IngredientEntity(name, byteArray, unit, expiration.days)

        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.async(Dispatchers.IO) {
                try {
                    val result = IngredientRepository.insertIngredient(item) > 0

                    _event.postValue(
                        if (result) {
                            IngredientAddEvent.InsertSuccess
                        } else {
                            IngredientAddEvent.InsertFailed
                        }
                    )
                } catch (e: Exception) {
                    _event.postValue(IngredientAddEvent.InsertFailed)
                }
            }
        }
    }
}