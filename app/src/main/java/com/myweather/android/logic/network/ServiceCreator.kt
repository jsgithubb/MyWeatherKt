package com.myweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  Retrotif 构造器
 */
object ServiceCreator {

    //定义根目录请求地址
    private const val BASE_URL = "https://api.caiyunapp.com/"

    //定义构造
    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    //定义方法，采用泛型,对外开放创建方式
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //泛型实体化功能，都是对外开放创建的构造器，只是方式不同
    inline fun <reified T> create(): T = create(T::class.java)



}