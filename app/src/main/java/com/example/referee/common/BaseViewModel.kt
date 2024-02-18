package com.example.referee.common

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class BaseViewModel<T>(application: RefereeApplication) : AndroidViewModel(application) {
    protected var _event = MutableLiveData<EventWrapper<T>>()
    val event: LiveData<EventWrapper<T>> = _event
}