package com.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Acer on 2018/10/5.
 * 处理天气用的工具类，和服务器端的交互
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
