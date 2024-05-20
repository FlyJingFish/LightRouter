package com.flyjingfish.lightrouter
import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CollectApp.onCreate(this)
    }
}