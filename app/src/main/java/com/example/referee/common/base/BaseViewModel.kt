package com.example.referee.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.referee.common.EventWrapper

open class BaseViewModel<T> : ViewModel() {
    protected var _event = MutableLiveData<EventWrapper<T>>()
    val event: LiveData<EventWrapper<T>> = _event
}