package com.example.referee.common.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity :AppCompatActivity(){
    protected fun showToast(text:String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }
}