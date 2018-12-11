package com.tools;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 29924 on 2018/8/12.
 */

public class Httphelper {
    private static final String TAG = "Httphelper";

    public static boolean result;
    public static int code;
    public static String codeGet;
    /**
     *
     * @param handler 为了将服务器发来的消息传回到MainActivity
     * @param url
     * @param params 将用户信息通过键值对的方式封装在Map中
     */
    public static void sendRequestWithOkHttp(final Handler handler, final String url,
                                             final Map<String,String> params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody.Builder formBody=new FormBody.Builder();
                    if(params!=null){
                        for (Map.Entry<String,String> entry : params.entrySet()) {
                            formBody.add(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
                        }
                    }
                    RequestBody requestBody=formBody.build();
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            //.retryOnConnectionFailure(true)//使用Java服务器添加的
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(100, TimeUnit.SECONDS)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Connection","close")
                            .post(requestBody)//post请求添加此句
                            .build();

                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String reseponseData=response.body().string();
                        Log.i("WY","打印POST响应的数据：" + reseponseData);
                        parseJSONWithGSON(reseponseData);
                        Message message=new Message();
                        message.what=code;
                        message.obj=codeGet;
                        handler.sendMessage(message);
                        Log.d(TAG,"服务器发送返回信息");
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void parseJSONWithGSON(String jsonData){
        App appList=new Gson().fromJson(jsonData,App.class);
        //获取服务器发来的result和code，用于MainActivity中告知用户服务器处理用户信息的结果
        result=appList.getResult();
        code=appList.getCode();
        codeGet=appList.getCodeGet();

        Log.d(TAG,"Httphelper   "+appList.getResult());
        Log.d(TAG,"Httphelper   "+appList.getCode());
        Log.d(TAG,"Httphelper   "+appList.getCodeGet());

    }
}
