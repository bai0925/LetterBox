package com.example.acer.letterbox4;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;


import com.passwordview.OnPasswordInputFinish;
import com.passwordview.PasswordView;
import com.tools.ActivityCollector;
import com.tools.BaseActivity;
import com.tools.Httphelper;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.viewpage.ViewPageOne;
import com.viewpage.ViewPageThree;
import com.viewpage.ViewPageTwo;
import com.viewpage.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
//    private static final int OPEN_CAMERA_ACTION=180;
    private  static final int SIGNIN_SUCCESS_ACTION=181;
    private static final int JUMPTO_SETTING_ACTION=182;

    private static final int FACE_INFO_NOT_MATCH=402;//开锁网络返回信息

    private static final String domain = "47.101.173.82:8080";//132.232.64.242
    private String phone_num="1667108";
    
    //底部导航栏的滑动页面所用变量
    private BottomNavigationView bottomNavigationView;//底部导航栏
    private ViewPagerAdapter viewPagerAdapter;//自定义类
    private ViewPager viewPager;//页面滚动
    private MenuItem menuItem;

//    //点击开锁图标底部弹出选项所用变量
//    private PopupWindow popupWindow;
//    private PopupWindow popPswWindow;
//    private View contentView;
//    private View contentView2;

//    //密码输入变量
//    private PopupWindow mPopupWindow;
//    private View popView;
//    private PasswordView passwordView;

    //类似于toast的东西
    private TSnackbar snackbar;
    private int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;

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

//        showPopwindow();//初始化底部弹出的选项菜单，便宜后面显示
//        //初始化密码开锁弹出密码输入框
//        showPswPopupWindow();

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if(id==R.id.open_lock){//快捷开锁图标被点击
//            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
//            changeBGColor();
//        }
//        if(id==R.id.weather){//天气图标被点击
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


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

//    //按手机返回键，让底部弹出的选项(两种开锁)消失
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //按下手机的返回键，popwindow消失
//        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
//            if(popupWindow!=null&&popupWindow.isShowing()){
//                popupWindow.dismiss();
//                return true;
//            }
//        }
//        return false;
//    }

//    /**
//     * 显示popupWindow，在onCreate中调用
//     */
//    private void showPopwindow() {//onCreate中调用
//        //底部显示菜单项——人脸开锁和密码开锁
//        //加载弹出框的布局
//        contentView = LayoutInflater.from(MainActivity.this).inflate(
//                R.layout.pop, null);
//
//
//        popupWindow = new PopupWindow(contentView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setFocusable(true);// 取得焦点
//        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        //点击外部消失
//        popupWindow.setOutsideTouchable(true);
//        //设置可以点击
//        popupWindow.setTouchable(true);
//        //进入退出的动画，指定刚才定义的style
//        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                //当弹窗消失时背景恢复原来的背景
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
//
//        // 按下android回退物理键 PopipWindow消失解决（在上面的onKeyDown实现）
//
//    }
//    //点击取消
//    public void closePopWindow(View v) {
//        //从底部显示
//        popupWindow.dismiss();
//    }
//
//    public void showPswPopupWindow(){
//        contentView2 = LayoutInflater.from(MainActivity.this).inflate(
//                R.layout.pop_window, null);
//        passwordView = (PasswordView) contentView2.findViewById(R.id.pwd_view);
//        popPswWindow = new PopupWindow(contentView2,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popPswWindow.setFocusable(true);// 取得焦点
//        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
//        popPswWindow.setBackgroundDrawable(new BitmapDrawable());
//        //点击外部不消失
//        popPswWindow.setOutsideTouchable(false);
//        //设置可以点击
//        popPswWindow.setTouchable(true);
//        //进入退出的动画，指定刚才定义的style
//        popPswWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//        //当密码输入完成时
//        passwordView.setOnFinishInput(new OnPasswordInputFinish() {
//            @Override
//            public void inputFinish() {
//                popPswWindow.dismiss();
//                final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();//十分重要，否则不能顶部显示
//
//                //显示正在开锁
//                snackbar = TSnackbar.make(viewGroup, "正在开锁，请稍后...", TSnackbar.LENGTH_SHORT, APP_DOWn);
//                snackbar.setAction("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//                snackbar.setPromptThemBackground(Prompt.SUCCESS);//设置颜色和图标（如果没有下面一行代码）
//                snackbar.addIconProgressLoading(0,true,false);//设置坐标的加载动图
//                snackbar.show();
//            }
//        });
////        passwordView.getCancelImageView().setOnClickListener((View.OnClickListener) this);
////        passwordView.getForgetTextView().setOnClickListener((View.OnClickListener) this);
//
//        popPswWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                //当弹窗消失时背景恢复原来的背景
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
//    }
//
//    //设置弹窗出来时，背景变灰色半透明
//    public void changeBGColor(){
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.5f;
//        getWindow().setAttributes(lp);
//    }
////布局中onclick点击事件都在下面
//    //选择密码开锁
//    public void passworOpenLock(View view){
//        popupWindow.dismiss();
//        //显示密码输入弹窗
//        popPswWindow.showAtLocation(contentView2, Gravity.BOTTOM, 0, 0);
//        changeBGColor();
//    }
//    //选择刷脸开锁
//    public void faceOpenLock(View view){
////        popupWindow.dismiss();
////        //显示密码输入弹窗
////        popPswWindow.showAtLocation(contentView2, Gravity.BOTTOM, 0, 0);
////        changeBGColor();
//
//        //打开相机活动
//        Intent intent=new Intent(MainActivity.this,CameraActivity.class);
//        //intent.putExtra("denglu","DENGLU");//待删除
//        startActivityForResult(intent,OPEN_CAMERA_ACTION);
//    }
//
//    //点击密码输入弹窗上的×图标，关闭密码输入弹窗
//    public void closePswWindow(View view){
//        popPswWindow.dismiss();
//    }
//
//    //开锁密码忘记
//    public void forgetOpenLockPsw(View view){
//
//    }

    public void turnToFeedBack(){

    }

    //处理其他活动返回的参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
//            case OPEN_CAMERA_ACTION:
//                if(resultCode==RESULT_OK){
//                    String imageBase = "";
//                    imageBase = data.getExtras().getString("image_base");
//                    doOpenLock(imageBase);
//                }
//                break;
            case SIGNIN_SUCCESS_ACTION:
                if(resultCode==RESULT_OK){
                    phone_num=data.getExtras().getString("mainusername");
                    Log.d(TAG, "handleMessage: 登录用户："+phone_num);

                }
                break;
            case JUMPTO_SETTING_ACTION:
                //退出登录还未实现
                break;
        }
    }
//    ////////////////////////////处理网络回调//////////////////////////
//    private Handler mHandle=new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            if(Httphelper.result==false){
//                switch (message.what){
//                    case FACE_INFO_NOT_MATCH:
//                        makeToast("未能识别出您");break;
//                    default:makeToast("服务器错误");break;
//                }
//            }else {
//                makeToast("开锁中...");
//            }
//            return false;
//        }
//    });
//
//    /**
//     * 登录成功后开锁
//     * @param imageBase
//     */
//    private void doOpenLock(String imageBase){
//        String url = "http://" + domain + "/user/openlock";
//        Map<String, String> params = new HashMap<>();
//
//        params.put("name", phone_num);
//        Log.d(TAG, "doOpenLock: "+phone_num);
//        params.put("image", imageBase);
//
//        Httphelper.sendRequestWithOkHttp(mHandle,url,params);
//    }

    //点击用户头像跳转到用户信息填写界面
    public void doWriteInfo(View v){
        //跳转用户填写信息界面
        Intent intent=new Intent(MainActivity.this,MyActivity.class);
        startActivity(intent);
    }

    //显示提示消息，因为提示消息比较多，所以将他封装在函数中方便调用
    public void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
