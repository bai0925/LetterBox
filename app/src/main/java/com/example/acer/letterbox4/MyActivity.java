package com.example.acer.letterbox4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MyActivity extends AppCompatActivity {

    //还不懂这个是怎么实现返回功能的。。。
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://点击设置活动中的返回
                finish();
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);

        Toolbar toolbar=(Toolbar)findViewById(R.id.setting_toolbar) ;
        //标题栏相关设置
        setSupportActionBar(toolbar);
        //设置不现实自带的title文字
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_back);

        try {//设置状态栏和标题栏同色（Android5.0以上才有用）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void showMoreInfo(View v){

        //跳转到更多信息界面，在这里面可以填写用户的各项成绩之类的
        Intent intent=new Intent(MyActivity.this,InputPerformanceActivity.class);
        startActivity(intent);

    }
}
