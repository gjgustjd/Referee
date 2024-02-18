package com.example.referee.ingredientadd.model

enum class IngredientExpirationUnit(val unitName:String, val days:Int) {
    ONE_DAY("1일",1),
    TWO_DAY("3일",2),
    THREE_DAY("3일",3),
    ONE_WEEK("1주",7),
    TWO_WEEK("2주",14),
    THREE_WEEK("3주",21),
    ONE_MONTH("1개월",30),
    TWO_MONTH("2개월",60),
    THREE_MONTH("3개월",90),
    HALF_YEAR("반 년",180),
    ONE_YEAR("1년",365),
    TWO_YEAR("2년",730);

    companion object {
        fun fromString(name: String) = values().firstOrNull { it.unitName == name }
    }
}