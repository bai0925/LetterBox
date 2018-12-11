package com.example.acer.letterbox4;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lib.settingview.LSettingItem;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";

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
        setContentView(R.layout.activity_about);

        Toolbar toolbar=(Toolbar)findViewById(R.id.setting_toolbar) ;
        final LinearLayout showUsingInfo=(LinearLayout)findViewById(R.id.usingInformation);
        TextView tv_info=(TextView)findViewById(R.id.tv_info);

        CheckBox cbUsingInfo=(CheckBox)findViewById(R.id.cbUsingInfo);

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

        tv_info.setText("        本app的功能主要在于实现人脸识别开锁的功能。");

        cbUsingInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) showUsingInfo.setVisibility(View.VISIBLE);
                else showUsingInfo.setVisibility(View.GONE);
            }
        });
    }


}
