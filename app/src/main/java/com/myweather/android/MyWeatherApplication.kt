package com.myweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyWeatherApplication:Application() {

    /**
     *  提供一个静态方法，全局获取context，以及tonken
     */
    companion object{

        const val TOKEN = "TfD6dtv055yEAJiL" //  彩云令牌值

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext        //赋值给context
    }
}