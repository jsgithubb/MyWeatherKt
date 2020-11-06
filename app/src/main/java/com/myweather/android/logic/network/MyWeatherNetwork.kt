package com.myweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *  网络请求入口
 */

object MyWeatherNetwork {

    //调用ServiceCreator 方法构建 placeService，动态代理对象，拥有PlaceService 里面的方法
    private var placeService = ServiceCreator.create(PlaceService::class.java)

    //动态代理对象
    private var weatherService = ServiceCreator.create(WeatherService::class.java)

    //发送请求方法，带参数    挂起函数  请求城市信息
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    //发送请求方法，带参数    挂起函数  请求每日天气
    suspend fun getDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng,lat).await()

    //发送请求方法，带参数    挂起函数  请求实时天气
    suspend fun getRealtimeWeather(lng: String,lat: String) = weatherService.getRealtimeWeather(lng,lat).await()

    /**
     *  协程相关
     *      这里定义了一个await 函数，作为返回值为 Call<T> 的 函数的 扩展函数
     *
     *      这里定义成这样后 调用网络查询的函数后 都会执行代码里面的函数，使用了协程的方式
     *
     *      例如上面的searchPlaces 方法//
     */
    private suspend fun <T> Call<T>.await():T{

        return suspendCoroutine{continuation ->
            //执行函数，因为是返回值为 Call 函数的扩展函数，所以拥有 它的上下文
            enqueue(object :Callback<T>{
                // 执行成功
                override fun onResponse(call: Call<T>, response: Response<T>) {

                    //获取返回的信息体
                    var body = response.body()
                    //如果body 不为空，则返回消息体
                    if (body != null) continuation.resume(body)

                    else continuation.resumeWithException(
                        //执行异常
                        RuntimeException("返回信息体为空...")
                    )

                }

                //执行失败
                override fun onFailure(call: Call<T>, t: Throwable) {
                    //打印异常
                    continuation.resumeWithException(t)

                }


            })

        }
    }


}