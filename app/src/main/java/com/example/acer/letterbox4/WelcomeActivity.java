package com.example.acer.letterbox4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.io.IOException;

public class WelcomeActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //启动一个延时线程
        new Thread(this).start();
    }

    public void run(){
        try{
            //延迟一秒时间
            Thread.sleep(1000);
            SharedPreferences preferences=getSharedPreferences("count",0);
            int count=preferences.getInt("count",0);

            if(count==0){
                Intent intent1=new Intent();
                intent1.setClass(WelcomeActivity.this,GuideActivity.class);
                startActivity(intent1);
            }else{
                Intent intent2=new Intent();
                intent2.setClass(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent2);
            }
            finish();
            //实例化Editor对象
            SharedPreferences.Editor editor=preferences.edit();
            //存入数据
            editor.putInt("count",1);
            //提交数据
            editor.commit();
        }catch(InterruptedException e){

        }
    }

}
