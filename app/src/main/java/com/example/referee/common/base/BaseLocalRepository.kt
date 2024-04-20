package com.example.referee.common.base

import com.example.referee.common.RefereeApplication
import com.example.referee.common.model.RefereeDataBase

open class BaseLocalRepository {
    protected val db by lazy {
        val application = RefereeApplication.instance()
        RefereeDataBase.getInstance(application)
    }
}