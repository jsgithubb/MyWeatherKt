package com.myweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.myweather.android.MyWeatherApplication
import com.myweather.android.logic.model.Place

/**
 *  记录城市信息
 */

object PlaceDao {

    //保存信息的方法
    fun savePlace(place: Place){
        //这是扩展函数，edit， 该表达式中拥有sharedPreferences 的上下文
        sharedPreferences().edit{
            putString("place",Gson().toJson(place))     //转json字符串
        }
    }

    //读取的方法
    fun getSavePlace(): Place{

        val placeJson = sharedPreferences().getString("place", "")

        //返回值
        return Gson().fromJson(placeJson,Place::class.java)     //把json字符串转换成place 封装类

    }

    //判断是否有数据存储
    fun isPlaceSaced() = sharedPreferences().contains("place")



    //定义一个sharedPreferences
    private fun sharedPreferences() = MyWeatherApplication.context.getSharedPreferences("my_weather",Context.MODE_PRIVATE)

}