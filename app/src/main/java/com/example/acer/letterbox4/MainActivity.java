package com.example.acer.letterbox4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.tools.ActivityCollector;
import com.tools.BaseActivity;
import com.viewpage.ViewPageOne;
import com.viewpage.ViewPageThree;
import com.viewpage.ViewPageTwo;
import com.viewpage.ViewPagerAdapter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //处理来自LoginActivity中返回参数的变量
    private  static final int SIGNIN_SUCCESS_ACTION=181;
    private static final int JUMPTO_SETTING_ACTION=182;

    //底部导航栏的滑动页面所用变量
    private BottomNavigationView bottomNavigationView;//底部导航栏
    private ViewPagerAdapter viewPagerAdapter;//自定义类
    private ViewPager viewPager;//页面滚动
    private MenuItem menuItem;

    //选择底部导航栏显示不同布局
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem=item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_openlock:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //底部选项栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //侧滑菜单
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        //底部导航栏点击监听事件，监听函数mOnNavigationItemSelectedListener在上面
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        //页面改变时执行
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            //滑动屏幕改变页面时，让底部导航栏跟着变化
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        //view数组
        List<Fragment> list = new ArrayList<>();
        list.add(ViewPageOne.newInstance());
        list.add(ViewPageTwo.newInstance());
        list.add(ViewPageThree.newInstance());
        viewPagerAdapter.setList(list);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //侧滑菜单项的点击监听事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_weather){
            //跳转天气界面
            Intent intent=new Intent(MainActivity.this,PCCForWeatherActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_feedback) {
            //跳转反馈界面（测试），如果需要传参，就需要修改
            Intent intent=new Intent(MainActivity.this,FeedbackActivity.class);
            startActivity(intent);

        }else if(id==R.id.nav_version){
            //显示已经是最新版本
            makeToast("当前已经是最新版本");
        }
        else if (id == R.id.nav_about) {
            //跳转关于软件界面
            Intent intent=new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_exit) {//退出程序
            ActivityCollector.finishAll();
        } else if (id == R.id.nav_setting) {
            //跳转设置界面（测试），如果需要传参，就需要修改
            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //处理来自LoginActivity中handlerForSignIn返回的参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String phoneNumFromLogin = data.getExtras().getString("mainusername");
//        Log.d("MainActivity", "onActivityResult: "+requestCode+" "+resultCode+" "+phoneNumFromLogin);
        switch (requestCode){
            case SIGNIN_SUCCESS_ACTION:
                if(resultCode==RESULT_OK){
                    String phoneNumFromLogin = data.getExtras().getString("mainusername");
                    Log.d("MainActivity", "onActivityResult: "+requestCode+" "+resultCode+" "+phoneNumFromLogin);
                }
                break;
            case JUMPTO_SETTING_ACTION:
                //退出登录还未实现
                break;
        }
    }

    //点击用户头像跳转到用户信息填写界面
    public void doWriteInfo(View v){
        //跳转用户填写信息界面
        Intent intent=new Intent(MainActivity.this,MyActivity.class);
        startActivity(intent);
    }

    /**
     * 封装Toast函数，方便之后调用
     * @param message 需要弹出Toast的信息内容
     */
    public void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
