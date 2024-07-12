package com.flyjingfish.lightrouter
import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        CollectApp.onCreate(this)
    }
}