package com.myweather.android.ui.place

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.myweather.android.R
import com.myweather.android.logic.model.Place
import com.myweather.android.ui.weather.WeatherActivity

/**
 *  城市列表RecyclerView 适配器
 */

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    /**
     *  内部类，数据绑定
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    /**
     *  初始化关联
     *      点击事件
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        
        val holder = ViewHolder(view)

        //设置点击事件,整个item 都可以点击
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition   //获取第几项
            val place = placeList[position]         //获取第几项

            //跳转事件  apply 函数
            val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                //携带的数据
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)

            }
            //存入数据
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)  //执行跳转
            fragment.activity?.finish()

        }

        return holder

    }


    /**
     *  赋值，关联
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    /**
     *  返回的条目
     */
    override fun getItemCount(): Int = placeList.size

}