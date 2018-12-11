package com.viewpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.acer.letterbox4.R;

/**
 * Created by Acer on 2018/9/27.
 */

public class ViewPageOne extends Fragment {

    private WebView webView;

    //创建“我的”实例
    public static ViewPageOne newInstance() {
        ViewPageOne viewPageOne = new ViewPageOne();
        return viewPageOne;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //与对应的布局绑定
        View view = inflater.inflate(R.layout.viewpage_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView)view.findViewById(R.id.webView);
        //对webview 进行基本设置
        WebSettings settings = webView.getSettings();
        //显示缩放控制器
        settings.setDisplayZoomControls(true);
        //支持网页缩放
        settings.setSupportZoom(true);
        //支持JavaScript
        settings.setJavaScriptEnabled(true);
        //加载URL代表的网页
        webView.loadUrl("http://news.yangtzeu.edu.cn/");

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);//关键点

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放



        settings.setLoadWithOverviewMode(true);

//        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        //设置默认浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);

                return super.shouldOverrideUrlLoading(view,url);
            }
        });
//
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            if(webView != null && webView.canGoBack()){
//                //返回到前一页
//                webView.goBack();
//            }else{
//                finish();
//            }
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }



}