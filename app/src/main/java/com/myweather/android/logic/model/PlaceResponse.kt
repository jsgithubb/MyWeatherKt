package com.myweather.android.logic.model

import com.google.gson.annotations.SerializedName

/*
    json返回的城市数据封装
        返回的数据为嵌套的数据，所以，分成多个类
 */

//总位置回应
data class PlaceResponse(var status:String , val places: List<Place>)

//地点信息
data class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)

//位置信息
data class Location(val lng: String, val lat: String)