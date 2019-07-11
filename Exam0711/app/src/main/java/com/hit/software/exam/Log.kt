package com.hit.software.exam
import android.util.Log

object Log {

    fun p(txt:String?){
        if (!txt.isNullOrBlank())
        Log.d("exam_app",txt)
    }
}