package com.myweather.android.logic

import androidx.lifecycle.liveData
import com.myweather.android.logic.dao.PlaceDao
import com.myweather.android.logic.model.Place
import com.myweather.android.logic.model.Weather
import com.myweather.android.logic.network.MyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 *   类似仓库，存放网络请求后的数据
 */

object Repository {

    /**
     *  刷新（查找）城市信息方法
     *      参数为请求的城市名称
     */
    fun searchPlaces(query:String) = fire(Dispatchers.IO){

            //执行请求方法
            val placeResponse = MyWeatherNetwork.searchPlaces(query)

            //判断响应状态
            if (placeResponse.status == "ok"){

                val place = placeResponse.places
                Result.success(place)

            }else{
                Result.failure(RuntimeException("返回的状态码为${placeResponse.status}"))
            }

    }

    /**
     *  刷新（查找）天气信息的方法
     *      参数为请求的经度与维度
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO){

            //协程作用域
            coroutineScope {

                val deferredRealtime = async {
                   MyWeatherNetwork.getRealtimeWeather(lng,lat)
                }

                val deferredDaily = async {
                    MyWeatherNetwork.getDailyWeather(lng,lat)
                }

                //执行这两条协程,并返回数据
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()

                //进行判断，返回的状态码是否 为 ’OK‘
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                    //说明返回数据是正常的，把数据封装到 Weather 数据类中
                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)

                    //把 weather 封装到 Result 中
                    Result.success(weather)

                }else{
                    Result.failure(RuntimeException(
                            "realtime数据接口返回的状态码是${realtimeResponse.status}," +
                                    "daily数据接口返回的状态码是${dailyResponse.status}"
                        )
                    )
                }

            }


    }


    //解决每次发送请求都抛异常的问题
    private fun <T> fire(context: CoroutineContext,block: suspend () -> Result<T>) =
        liveData<Result<T>> {
            val result = try{
                block()
            }catch (e: Exception){
                Result.failure<T>(e)
            }

            emit(result)
        }


    //实现数据存储的方法
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavePlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaced()


}