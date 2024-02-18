package com.example.referee.common

open class EventWrapper<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * 이미 사용한 경우 null을 리턴하여 재사용 방지
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * 처리되었는지 여부와 관계없이 내용 반환
     */
    fun peekContent(): T = content
}