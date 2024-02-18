package com.example.referee.common

import android.app.Application

class RefereeApplication :Application() {

    companion object {
        @Volatile
        private var instance:RefereeApplication? = null
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}
