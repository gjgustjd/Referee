package com.example.referee.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.referee.common.EventWrapper
import com.example.referee.common.RefereeApplication

open class BaseViewModel<T> : ViewModel() {

    protected val applicationScope = RefereeApplication.instance.applicationScope
    protected var _event = MutableLiveData<EventWrapper<T>>()
    val event: LiveData<EventWrapper<T>> = _event
}