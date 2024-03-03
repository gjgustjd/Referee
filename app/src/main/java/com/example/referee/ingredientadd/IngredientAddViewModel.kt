package com.example.referee.ingredientadd

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.RefereeApplication
import com.example.referee.common.applicationScope
import com.example.referee.common.base.BaseViewModel
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientExpirationUnit
import com.example.referee.ingredientadd.model.IngredientRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class IngredientAddViewModel : BaseViewModel<IngredientAddEvent>() {

    var preSavedImageName: Deferred<String?>? = null
        private set

    fun insertIngredient(
        name: String,
        unit: String,
        expiration: IngredientExpirationUnit,
        category:IngredientCategoryType
    ): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val photoName = preSavedImageName?.await()
            val item = IngredientEntity(
                name,
                photoName,
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
    }

    fun isExistSameNameIngredient(name: String) {
        applicationScope.launch(Dispatchers.IO) {
            val result = if (IngredientRepository.getIngredientsByName(name).isNotEmpty()) {
                EventWrapper(IngredientAddEvent.IsThereIngredient(true))
            } else {
                EventWrapper(IngredientAddEvent.IsThereIngredient(false))
            }

            _event.postValue(result)
        }
    }

    fun editIngredient(
        id:Long,
        name: String,
        unit: String,
        prevPhotoName:String?,
        expiration: IngredientExpirationUnit,
        category: IngredientCategoryType
    ) {
        applicationScope.launch {
            val result = try {
                val photoName = withContext(Dispatchers.IO) {
                    preSavedImageName?.await() ?: prevPhotoName
                }
                val entity =
                    IngredientEntity(
                        name,
                        photoName,
                        unit,
                        expiration.days,
                        category.ordinal
                    ).apply {
                        this.id = id
                    }
                val updateResult = withContext(Dispatchers.IO) {
                    IngredientRepository.updateIngredient(entity)
                }

                if (updateResult > 0) {
                    EventWrapper(IngredientAddEvent.UpdateSuccess)
                } else {
                    EventWrapper(IngredientAddEvent.UpdateFailed)
                }
            } catch (e: java.lang.Exception) {
                EventWrapper(IngredientAddEvent.UpdateFailed)
            }
            _event.postValue(result)
        }
    }

     fun saveImage(bitmap: Bitmap?) {
         applicationScope.launch {
             preSavedImageName = async {
                 bitmap?.let {
                     try {
                         val storage = RefereeApplication.instance().cacheDir
                         val fileName = "ingredient_" + System.currentTimeMillis().toString()
                         val tempFile = File(storage, fileName)

                         tempFile.createNewFile()
                         val outStream = FileOutputStream(tempFile)
                         bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                         outStream.close()

                         fileName
                     } catch (e: CancellationException) {
                         null
                     }
                 }
             }
         }
     }

    fun deletePreSavedImage() {
        preSavedImageName?.let { imageName ->
            applicationScope.launch {
                if (imageName.isCompleted) {
                    imageName.getCompleted()
                } else {
                    imageName.await()
                }?.let(::deleteImageFile)
            }
        }
    }

    private fun deleteImageFile(imageName:String) {
        val storage = RefereeApplication.instance().cacheDir
        val file = File(storage, imageName)
        if (file.exists()) {
            file.delete()
        }
    }

    fun getImageBitmap(imageName:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storage = RefereeApplication.instance.applicationContext.cacheDir
                storage.path
                Log.i("PathTest","path:${storage}")
                val path = "${storage}/$imageName"
                val bitmap = BitmapFactory.decodeFile(path)
                _event.postValue(EventWrapper(IngredientAddEvent.IngredientBitmap(bitmap)))
            } catch (e: java.lang.Exception) {
            }
        }
    }
}