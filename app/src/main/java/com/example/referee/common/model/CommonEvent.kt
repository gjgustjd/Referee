package com.example.referee.common.model

sealed interface CommonEvent {

    class ShowLoading(val onBackPressed: (() -> Unit)?) : CommonEvent
    object HideLoading:CommonEvent
}