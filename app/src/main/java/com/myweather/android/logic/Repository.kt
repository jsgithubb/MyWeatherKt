package com.myweather.android.logic

import androidx.lifecycle.liveData
import com.myweather.android.logic.model.Place
import com.myweather.android.logic.network.MyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

/**
 *   类似仓库，存放网络请求后的数据
 */

object Repository {

    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
        val result = try {
            //执行请求方法
            val placeResponse = MyWeatherNetwork.searchPlaces(query)

            //判断响应状态
            if (placeResponse.status == "ok"){

                val place = placeResponse.places
                Result.success(place)

            }else{
                Result.failure(RuntimeException("返回的状态码为${placeResponse.status}"))
            }

        }catch (e:Exception){
            e.printStackTrace()
            Result.failure<List<Place>>(e)
        }

        emit(result)    //挂起函数
    }

}