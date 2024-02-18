package com.example.referee.ingredientadd

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientExpirationUnit
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class IngredientAddViewModel : BaseViewModel<IngredientAddEvent>() {

    fun insertIngredient(
        name: String,
        photoPath: Bitmap,
        unit: String,
        expiration: IngredientExpirationUnit
    ) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        photoPath.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val item = IngredientEntity(name, byteArray, unit, expiration.days)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = withContext(Dispatchers.IO) {
                    IngredientRepository.insertIngredient(item) > 0
                }

                _event.postValue(
                    if (result) {
                        EventWrapper(IngredientAddEvent.InsertSuccess)
                    } else {
                        EventWrapper(IngredientAddEvent.InsertFailed)
                    }
                )
            } catch (e: Exception) {
                _event.postValue(EventWrapper(IngredientAddEvent.InsertFailed))
            }
        }
    }
}