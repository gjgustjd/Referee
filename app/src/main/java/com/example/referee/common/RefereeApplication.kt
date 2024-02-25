package com.example.referee.common

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class RefereeApplication :Application() {

    val applicationScope = CoroutineScope(SupervisorJob()+Dispatchers.Default)

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
        applicationScope.cancel()
    }

}
