package com.example.referee.common

import android.util.Log
import java.lang.StringBuilder

class Logger {

    companion object {
        private const val TAG = "RefereeLogger"
        fun i(message: String? = null, prefix: String? = null) {
            Log.i(TAG, getDecoratedLog(prefix, message))
        }

        private fun getDecoratedLog(prefix: String? = null, message: String?=null):String {
            val level = 5
            val ste: StackTraceElement = Thread.currentThread().stackTrace[level]
            val fileName = ste.fileName.replace(".java", "")
            val methodName = ste.methodName
            val lineNumber = ste.lineNumber
            val sb = StringBuilder().apply{
                prefix?.let{
                    append("[$it]")
                }
                append(" ")
                append(fileName)
                append("::")
                append(methodName)
                append("($fileName:$lineNumber)")
                message?.let {
                     append(":$message")
                }
            }
            return sb.toString()
        }
    }
}