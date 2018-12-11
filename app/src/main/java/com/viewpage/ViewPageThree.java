package com.viewpage;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.acer.letterbox4.LoginActivity;
import com.example.acer.letterbox4.R;
import com.example.acer.letterbox4.openCameraActivity;
import com.passwordview.OnPasswordInputFinish;
import com.passwordview.PasswordView;
import com.tools.Httphelper;
import com.trycatch.mysnackbar.TSnackbar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 2018/9/27.
 */

public class ViewPageThree extends Fragment {
    private static final String TAG = "ViewPageThree";

    private static final int FACE_INFO_NOT_MATCH = 402;//开锁网络返回信息
    private static final String domain = "47.101.173.82:8080";//47.101.173.82:8080
    private String phone_num = "1667108";

    private Button pswOpenLock;
    private Button faceOpenLock;
    public static Uri imageUri;


    //点击开锁图标底部弹出选项所用变量
    private PopupWindow popupWindow;
    private PopupWindow popPswWindow;
    private View contentView;
    private View contentView2;

    //密码输入变量
    private PopupWindow mPopupWindow;
    private View popView;
    private PasswordView passwordView;

    //类似于toast的东西
    private TSnackbar snackbar;
    private int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;

    //创建“我的”实例
    public static ViewPageThree newInstance() {
        ViewPageThree viewPageThree = new ViewPageThree();
        return viewPageThree;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //与对应的布局绑定
        View view = inflater.inflate(R.layout.viewpage_three, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pswOpenLock=view.findViewById(R.id.psw_openlock);
        faceOpenLock=view.findViewById(R.id.face_openlock);

        pswOpenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示密码输入弹窗
                popPswWindow.showAtLocation(contentView2, Gravity.BOTTOM, 0, 0);
            }
        });
        faceOpenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),openCameraActivity.class);
                startActivity(intent);
            }
        });

        //初始化密码开锁弹出密码输入框
        showPswPopupWindow();

    }

    public void showPswPopupWindow() {

        contentView2 = LayoutInflater.from(getContext()).inflate(
                R.layout.pop_window, null);
        passwordView = (PasswordView) contentView2.findViewById(R.id.pwd_view);
        popPswWindow = new PopupWindow(contentView2,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popPswWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popPswWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部不消失
        popPswWindow.setOutsideTouchable(false);
        //设置可以点击
        popPswWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popPswWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //当密码输入完成时
        passwordView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                popPswWindow.dismiss();
                doOpenLock();
                makeToast("正在开锁");
            }
        });
    }

    private void doOpenLock(){
        String url = "http://" + domain + "/user/testOpenLock";
        Map<String, String> params = new HashMap<>();

        params.put("name", phone_num);
        Log.d(TAG, "doOpenLock: "+phone_num);
//        params.put("image", imageBase);

        Httphelper.sendRequestWithOkHttp(mHandle,url,params);
    }

    ////////////////////////////处理网络回调//////////////////////////
    private Handler mHandle=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            makeToast("开锁中...");

            return false;
        }

    });

    //点击密码输入弹窗上的×图标，关闭密码输入弹窗
    public void closePswWindow(View view){
        popPswWindow.dismiss();
    }

    //显示提示消息，因为提示消息比较多，所以将他封装在函数中方便调用
    public void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}