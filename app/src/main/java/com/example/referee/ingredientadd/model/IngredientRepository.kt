package com.example.referee.ingredientadd.model

import com.example.referee.common.RefereeApplication
import com.example.referee.common.model.RefereeDataBase

object IngredientRepository {
    private val db by lazy {
        val application = RefereeApplication.instance()
        application?.let {
            RefereeDataBase.getInstance(application = it)
        }
    }

    fun insertIngredient(item: IngredientEntity) =
        db?.ingredientsDAO()?.insertIngredient(item) ?: -1
}