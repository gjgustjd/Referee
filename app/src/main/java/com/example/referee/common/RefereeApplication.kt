package com.example.referee.common

import android.app.Application
import com.example.referee.common.model.RefereeDataBase

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

    override fun onTerminate() {
        super.onTerminate()
        RefereeDataBase.getInstance(this).close()
    }
}
