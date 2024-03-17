package com.example.referee.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.referee.common.EventWrapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

open class BaseViewModel<T> : ViewModel() {
    private val SHARED_FLOW_REPLAY_COUNT = 1
    protected var _event = MutableLiveData<EventWrapper<T>>()
    val event: LiveData<EventWrapper<T>> = _event

    protected var _sharedFlow = MutableSharedFlow<EventWrapper<T>>(SHARED_FLOW_REPLAY_COUNT)
    val sharedFlow: SharedFlow<EventWrapper<T>> = _sharedFlow
}