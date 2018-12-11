package com.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.gson.Weather;
import com.util.HttpUtil;
import com.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        //更新天气
        updateWeather();
        //创建定时任务，将时间间隔设置为8小时，8小时后
        // AutoUpdateReceiver的onStartCommand()方法会重新执行，也即实现后台定时更新的功能
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=60*60*1000;//这是8小时的毫秒数，暂时先改为1小时
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    //更新天气信息
    private void updateWeather(){
        //将更新后的数据直接储存到SharedPreferences文件中
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存时直接解析天气数据
            //打开WeatherActivity的时候会优先从SharedPreferences缓存中读取数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            String weatherId=weather.basic.weatherId;

            String weatherUrl="http://guolin.tech/api/weather?cityid="
                    +weatherId+"&key=26e1870d2ed6404f81f719a0b7bbf6cf";
            HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){
                @Override
                public void onResponse(Call call, Response response)throws
                        IOException{
                    String responseText=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(responseText);
                    if(weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor=PreferenceManager.
                                getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
                @Override
                public void onFailure(Call call,IOException e){
                    e.printStackTrace();
                }
            });
        }
    }
}