package com.example.referee.fridge.model

sealed interface FridgeEvent {
    class FridgeItemsEvent(val items: List<FridgeIngredientEntity>) : FridgeEvent
}