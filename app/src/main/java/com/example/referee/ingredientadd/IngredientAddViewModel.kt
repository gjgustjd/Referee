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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class IngredientAddViewModel : BaseViewModel<IngredientAddEvent>() {

    private var savedImageName: String? = null
    private var saveImageJob: Job? = null

    fun insertIngredient(
        name: String,
        unit: String,
        expiration: IngredientExpirationUnit,
        category:IngredientCategoryType
    ): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            var photoName = savedImageName
            photoName?.let {
                insertIngredientIntoDB(name, unit, it.ifEmpty { null }, expiration, category)
            } ?: saveImageJob?.invokeOnCompletion {
                viewModelScope.launch(Dispatchers.IO) {
                    photoName = savedImageName?.ifEmpty { null }
                    insertIngredientIntoDB(name, unit, photoName, expiration, category)
                }
            }
        }
    }

    private suspend fun insertIngredientIntoDB(
        name: String,
        unit: String,
        imageName: String?,
        expiration: IngredientExpirationUnit,
        category: IngredientCategoryType
    ) {
        val item = IngredientEntity(
            name,
            imageName,
            unit,
            expiration.days,
            category.ordinal
        )
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

     fun saveImage(bitmap: Bitmap?) {
        saveImageJob = viewModelScope.launch(Dispatchers.IO) {
            savedImageName = bitmap?.let {
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
                    ""
                }
            }
        }
    }
}