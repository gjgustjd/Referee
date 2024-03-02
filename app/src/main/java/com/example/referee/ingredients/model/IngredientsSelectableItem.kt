package com.example.referee.ingredients.model

import com.example.referee.ingredientadd.model.IngredientEntity

data class IngredientsSelectableItem(
    val entity: IngredientEntity,
    var isSelected: Boolean = false
)