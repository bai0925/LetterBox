package com.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BaiXiaozhe on 2018/10/14.
 * 由于JSON中的一些字段不适合直接作为Java字段来命名，
 * 因此使用@Serialized注解的方式让JSON字段和Java字段之间建立映射关系
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
