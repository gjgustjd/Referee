package com.example.referee.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

val applicationScope = CoroutineScope(Dispatchers.IO + Job())