package com.example.referee.common

import android.app.Application

class RefereeApplication :Application() {

    companion object {
        @Volatile
        lateinit var instance:RefereeApplication
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}
