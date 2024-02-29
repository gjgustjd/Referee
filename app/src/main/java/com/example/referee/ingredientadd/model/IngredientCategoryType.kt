package com.example.referee.ingredientadd.model

import com.example.referee.R

enum class IngredientCategoryType(val categoryName:String,val iconResourceId:Int) {
    MEAT("육류", R.drawable.ingredient_category_icon_meat),
    SEAFOOD("수산물", R.drawable.ingredient_category_icon_seafood),
    VEGETABLES("채소", R.drawable.ingredient_category_icon_vegetables),
    DIARY_PRODUCT("유제품", R.drawable.ingredient_category_icon_diary_product),
    FRESH_PRODUCT("신선식품", R.drawable.ingredient_category_icon_fresh_products),
    FRUIT("과일", R.drawable.ingredient_category_icon_fruits),
    NUTS("견과류", R.drawable.ingredient_category_icon_nuts),
    GRAINS("곡류", R.drawable.ingredient_category_icon_grains),
    SAUCE("장류", R.drawable.ingredient_category_icon_sauces),
    SPICES("향신료", R.drawable.ingredient_category_icon_spices),
    OIL("기름", R.drawable.ingredient_category_icon_oil),
    PROCESSED_FOOD("가공식품", R.drawable.ingredient_category_icon_processed_product);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.ordinal == value }
    }
}