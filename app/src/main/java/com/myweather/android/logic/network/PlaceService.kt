package com.myweather.android.logic.network

import com.myweather.android.MyWeatherApplication
import com.myweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.security.auth.callback.Callback

/**
 *  返回搜索城市信息的接口
 *      调用该方法时 会自动发送一条get请求，retrofit 框架 /  使用@Query 注解配上请求参数
 *          call 为返回值，这里指定PlaceResponse 类型。 自动解析成 PlaceResponse 对象
 */

interface PlaceService {

    @GET("v2/place?token=${MyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String) : Call<PlaceResponse>

}