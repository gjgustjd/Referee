package com.example.referee.ingredients.model

enum class IngredientsFABType {
    MAIN_FAB,
    SUB_FIRST_FAB,
    SUB_SECOND_FAB;

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.ordinal == value }
    }
}