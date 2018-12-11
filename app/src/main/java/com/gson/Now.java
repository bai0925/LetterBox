package com.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BaiXiaozhe on 2018/10/14.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
