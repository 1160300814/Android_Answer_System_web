package com.hit.software.exam

import android.app.Application
import io.realm.Realm

class ExamApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        //数据库初始化操作
        Realm.init(this)

    }
}