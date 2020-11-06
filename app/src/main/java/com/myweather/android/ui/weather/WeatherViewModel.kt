package com.myweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.myweather.android.logic.Repository
import com.myweather.android.logic.model.Location

class WeatherViewModel:ViewModel() {


    //定义一个livedata
    private val locationLiveData = MutableLiveData<Location>()

    //经度
    var locationLng = ""
    //维度
    var locationLat = ""
    //城市名
    var placeName = ""


    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng,lat)  //Location 是定义的数据包，model 层
    }

}