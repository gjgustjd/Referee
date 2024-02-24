package com.example.referee.ingredientadd

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.RefereeApplication
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientExpirationUnit
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class IngredientAddViewModel : BaseViewModel<IngredientAddEvent>() {

    fun insertIngredient(
        name: String,
        bitmap: Bitmap? = null,
        unit: String,
        expiration: IngredientExpirationUnit,
        category:IngredientCategoryType
    ) {
        val photoName = saveImage(bitmap)
        val item = IngredientEntity(name, photoName, unit, expiration.days,category.ordinal)

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

    fun isExistSameNameIngredient(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = if (IngredientRepository.getIngredientsByName(name).isNotEmpty()) {
                EventWrapper(IngredientAddEvent.IsThereIngredient(true))
            } else {
                EventWrapper(IngredientAddEvent.IsThereIngredient(false))
            }

            _event.postValue(result)
        }
    }

    private fun saveImage(bitmap: Bitmap?): String? {
        return bitmap?.let {
            try {
                val storage = RefereeApplication.instance().cacheDir
                val fileName = "ingredient_" + System.currentTimeMillis().toString()
                val tempFile = File(storage, fileName)

                tempFile.createNewFile()
                val outStream = FileOutputStream(tempFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.close()

                fileName
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }
}