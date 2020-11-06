package com.myweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myweather.android.MainActivity
import com.myweather.android.R
import com.myweather.android.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*
import kotlin.math.log

class PlaceFragment:Fragment() {

    //懒加载 viewModel
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    //定义适配器
    private lateinit var adapter: PlaceAdapter

    //重写 onCreateView 加载布局。 相当于指定哪个fragment 布局
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    /**
     *   这是一个很重要的方法
     *      相当于fragment 业务逻辑的编写
     *      重写父类的 onActivityCreated 方法
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //判断是否已经有存储的数据
        if (viewModel.isPlaceSaved()){
            //如果数据存在，那么获取存储的数据
            val place = viewModel.getSavedPlace()

            //跳转操作，带参数
            val intent = Intent(context,WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }

            //跳转
            startActivity(intent)
            activity?.finish()
            return  //直接return  不往下执行了

        }

        //定义一个布局方式
        val layoutManager = LinearLayoutManager(activity)

        //指定recycleview 的布局方式 这里是线性布局
        recyclerView.layoutManager = layoutManager


        //配置数据源头，传入参数为（fragment，和一个集合）  ，这里传入 viewmodel 中定义的集合
        adapter = PlaceAdapter(this, viewModel.placeList)
        //把数据源传给 recycleview
        recyclerView.adapter = adapter

        /**
         *  这个方法也比较重要
         *      主要是监听 EditView 中数据的变化
         *          每变化一次就执行一次查询，这里是查询城市数据，也就是发送一次请求
         */
        searchPlaceEdit.addTextChangedListener { editbale ->    //editbale 相当于this,就是这个editview本身
            val content = editbale.toString()   //获取到输入的内容，tostring
            if (!content.isEmpty()) {           //如果内容不为空
                viewModel.searchPlaces(content) //执行，查询数据
            } else {                            //如果信息为空
                //Log.d("empty", "onActivityCreated: 执行esle")
                recyclerView.visibility = View.GONE     //隐藏recyclerView
                bgImageView.visibility = View.VISIBLE   //显示背景图片
                viewModel.placeList.clear()             //清空集合
                adapter.notifyDataSetChanged()          //通知adapter 更新数据源
            }

            /**
             * livedata 观察者模式，方式数据丢失
             *  这里的目的主要是为了防止屏幕转换然后数据丢失，
             *      viewLifecycleOwner
             */
            viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->

                var places = result.getOrNull()
                //判断输入框
                if (content.isEmpty()){
                    //如果输入框为空，那么把这里的 places 赋值为null
                    places = null
                }

                if (places != null) {
                    recyclerView.visibility = View.VISIBLE
                    bgImageView.visibility = View.GONE
                    viewModel.placeList.clear()         //清空集合
                    viewModel.placeList.addAll(places)  //数据添加到集合
                    adapter.notifyDataSetChanged()      //通知数据源
                } else {
                    //Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                    result.exceptionOrNull()?.printStackTrace()
                }
            })

        }

    }
}