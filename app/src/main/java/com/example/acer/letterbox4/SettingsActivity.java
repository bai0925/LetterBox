package com.example.acer.letterbox4;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.leon.lib.settingview.LSettingItem;
import com.tools.ActivityCollector;
import com.tools.BaseActivity;
import com.tools.HiddenAnimUtils;

public class SettingsActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SettingsActivity";

    private LinearLayout accountSettingLayout;
    private LSettingItem accoutSetting;
    private Button exitSignIn;

    //
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
        setContentView(R.layout.activity_settings);

        Toolbar toolbar=(Toolbar)findViewById(R.id.setting_toolbar) ;
        //标题栏相关设置
        setSupportActionBar(toolbar);
        //设置不现实自带的title文字
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_back);

        exitSignIn=(Button)findViewById(R.id.exit_signin);
        exitSignIn.setOnClickListener(this);
        accoutSetting=(LSettingItem) findViewById(R.id.account_setting);
        accountSettingLayout=(LinearLayout)findViewById(R.id.account_setting_layout);
        accoutSetting.setOnClickListener(new LSettingItem.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "账号设置", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.account_setting:
//                int height= accountSettingLayout.getHeight();
//                Log.d(TAG, "showAccountSetting: height的值为："+height);
//                HiddenAnimUtils.newInstance(SettingsActivity.this,accountSettingLayout,view,height).toggle();
//                break;
            case R.id.exit_signin://点击退出登陆跳转登陆页面
                ActivityCollector.finishAll();
                Intent intent=new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            //case R.id.account_setting:
            default:
        }
    }

    //设置项点击事件
    //点击账号设置显示账号设置里面的更详细的设置
//    public void showAccountSetting(View view){
//        int height= accountSettingLayout.getHeight();
//        Log.d(TAG, "showAccountSetting: height的值为："+height);
//        HiddenAnimUtils.newInstance(SettingsActivity.this,accountSettingLayout,view,height).toggle();
//    }
}
