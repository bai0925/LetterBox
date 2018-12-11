package com.example.acer.letterbox4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tools.ActivityCollector;
import com.tools.BaseActivity;
import com.tools.Httphelper;
import com.util.Common;
import com.util.MyAdapter;
import com.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.acer.letterbox4.LoginActivity.FACE_INFO_NOT_MATCH;
import static com.example.acer.letterbox4.LoginActivity.PASSWORD_NOT_MATCH;
import static com.example.acer.letterbox4.LoginActivity.USER_NOT_EXIST;

public class GuideActivity extends BaseActivity {
//进入欢迎滑动界面相关
    private ViewPager mVp_Guide;
    private View mGuideRedPoint;
    private LinearLayout mLlGuidePoints;

    private int disPoints;
    private int currentItem;
    private MyAdapter adapter;
    private List<ImageView> guids;

    //向导界面的图片
    private int[] mPics = new int[]{R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4, R.mipmap.icon_5};

    private static final String TAG = "WelcomeActivity";
    private static final String domain = "132.232.64.242:8080";//132.232.64.242
    //用键值对的形式保存账号密码实现记住密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Boolean isRemenberPsw=false;

    String userName="",password="";
    private boolean singinSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        UIUtils.init(getApplicationContext());

    //滑动界面只进入一次实现
//        SharedPreferences name=getSharedPreferences("name",MODE_PRIVATE);
//        SharedPreferences.Editor edit=name.edit();
//        if(name.getString("isShow","true").equals("true")){
            initView();
            initData();
            initEvent();
//            edit.putString("isShow","false").commit();
//        }else{
//            Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
//            startActivity(intent);
//        }

    //忘记密码实现相关
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        isRemenberPsw=pref.getBoolean("remenber_password",false);
        if(isRemenberPsw){//如果之前设置了记住密码
            //将保存的密码账号填到输入框中
            userName=pref.getString("username","");
            password=pref.getString("password","");
            Log.d(TAG, "onCreate: 获取上次保存的账号密码");
            signIn(userName,password);
        }

    }

    ////////////////////////////开始实现滑动界面//////////////////////////////////////////////////////
    private void initView() {
        mVp_Guide = (ViewPager) findViewById(R.id.vp_guide);
        mGuideRedPoint = findViewById(R.id.v_guide_redpoint);
        mLlGuidePoints = (LinearLayout) findViewById(R.id.ll_guide_points);
    }

    private void initData() {
        // viewpaper adapter适配器
        guids = new ArrayList<ImageView>();

        //创建viewpager的适配器
        for (int i = 0; i < mPics.length; i++) {
            ImageView iv_temp = new ImageView(getApplicationContext());
            iv_temp.setBackgroundResource(mPics[i]);

            //添加界面的数据
            guids.add(iv_temp);

            //灰色的点在LinearLayout中绘制：
            //获取点
            View v_point = new View(getApplicationContext());
            v_point.setBackgroundResource(R.drawable.point_smiple);//灰点背景色
            //设置灰色点的显示大小
            int dip = 10;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dp2Px(dip), UIUtils.dp2Px(dip));
            //设置点与点的距离,第一个点除外
            if (i != 0)
                params.leftMargin = 47;
            v_point.setLayoutParams(params);

            mLlGuidePoints.addView(v_point);
        }

        // 创建viewpager的适配器
        adapter = new MyAdapter(getApplicationContext(), guids);
        // 设置适配器
        mVp_Guide.setAdapter(adapter);
    }

    private void initEvent() {
        //监听界面绘制完成
        mGuideRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //取消注册界面而产生的回调接口
                mGuideRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //计算点于点之间的距离
                disPoints = (mLlGuidePoints.getChildAt(1).getLeft() - mLlGuidePoints.getChildAt(0).getLeft());
            }
        });

        //滑动事件监听滑动距离，点更随滑动。
        mVp_Guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
               /* //当前viewpager显示的页码
                //如果viewpager滑动到第三页码（最后一页），显示进入的button
                if (position == guids.size() - 1) {
                    bt_startExp.setVisibility(View.VISIBLE);//设置按钮的显示
                } else {
                    //隐藏该按钮
                    bt_startExp.setVisibility(View.GONE);
                }*/
                currentItem = position;
            }

            /**
             *页面滑动调用，拿到滑动距离设置视图的滑动状态
             * @param position 当前页面位置
             * @param positionOffset 移动的比例值
             * @param positionOffsetPixels 便宜的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算红点的左边距
                float leftMargin = disPoints * (position + positionOffset);
                //设置红点的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGuideRedPoint.getLayoutParams();
                //对folat类型进行四舍五入，
                layoutParams.leftMargin = Math.round(leftMargin);
                //设置位置
                mGuideRedPoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //给页面设置触摸事件
        mVp_Guide.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float endX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (guids.size() - 1) && startX - endX >= (width / 4)) {
                            //进入主页
                            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //这部分代码是切换Activity时的动画，看起来就不会很生硬
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }
    ///////////////////////////////实现滑动界面结束////////////////////////////////////////////////////////

    private void signIn(String name,String password) {

        String url = "http://" + domain + "/user/signin";
        Map<String, String> params = new HashMap<>();

        params.put("name", name);
        params.put("password", password);

        Httphelper.sendRequestWithOkHttp(mHandle,url,params);
        Log.e(TAG,"begin to send userinformation to server,username:"+name);
    }
    //用来处理网络回调的 handler
    private Handler mHandle=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
                Log.d(TAG,"自动登录行为");
                if(Httphelper.result==false) {
                    singinSuccess=false;
                    switch (message.what) {
                        case USER_NOT_EXIST:
                            makeToast("用户未注册");
                            break;
                        case PASSWORD_NOT_MATCH:
                            makeToast("密码不正确");
                            break;
                        case FACE_INFO_NOT_MATCH:
                            makeToast("未能识别出您");
                            break;
                        default:
                            makeToast("服务器错误");
                            break;
                    }
                } else {
                    makeToast("登录成功");
                    singinSuccess=true;
                    ActivityCollector.finishAll();
                    //此处实现主活动的滑动菜单的登录成功后的用户名显示
                    Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                    //mainusername是主界面用户图像下面的用户名
                    intent.putExtra("mainusername",userName);
                    Log.d(TAG, "handleMessage: 登录用户名："+userName);
                    setResult(RESULT_OK, intent);
                    finish();
                    Log.d(TAG, "handleMessage: WelcomeActivity活动已经结束");
                    startActivity(intent);

                }

            return false;
        }
    });

    //显示提示消息，因为提示消息比较多，所以将他封装在函数中方便调用
    public void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
