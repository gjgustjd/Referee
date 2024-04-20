package com.example.referee.fridge

import androidx.lifecycle.viewModelScope
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.fridge.model.FridgeEvent
import com.example.referee.fridge.model.FridgeRepository
import kotlinx.coroutines.launch

class FridgeFragViewModel :BaseViewModel<FridgeEvent>(){

    fun getFridgeItems() {
        viewModelScope.launch {
            FridgeRepository.getFridgeItems().collect {
                _event.value = EventWrapper(FridgeEvent.FridgeItemsEvent(it))
            }
        }
    }
}