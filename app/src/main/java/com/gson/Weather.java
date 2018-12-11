package com.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by BaiXiaozhe on 2018/10/14.
 * 创建一个总的实例类来引用另外5个实体类。
 */

public class Weather {

    //返回的天气数据中还包含一项status数据，成功返回ok，失败返回具体的原因，因此添加status字段
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    //由于daily_forecast包含的是一个数组，所以这里使用List集合来引用Forecast类
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
