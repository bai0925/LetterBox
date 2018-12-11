package com.example.acer.letterbox4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.tools.Httphelper;
import com.tools.ImageFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class openCameraActivity extends AppCompatActivity {
    public static Uri imageUri;
    private SharedPreferences pref;
    public static final int USER_OPEN_LOCK_ACTION = 1;
    private static final String domain = "47.101.173.82:8080";
    private static final String TAG = "openCameraActivity";
    public static final int FACE_INFO_NOT_MATCH=402;
    public static final int SERVER_FAIL=0;
    public static final int USER_NOT_EXIST=400;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_camera2);

        openCamera();
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);//指定图片输出地址
        startActivityForResult(intent,USER_OPEN_LOCK_ACTION);//拍完后结果返回到onActivityResult


    }

    //启动系统相机
    public void openCamera(){
        //创建file对象，用于保存拍照的图片
        //getExternalCacheDir将图放在应用相关联缓存目录
        File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
        try{
            //若照片存在，先删除
            if(outputImage.exists()){
                outputImage.delete();
            }
            //然后新建一个储存照片的路径
            outputImage.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT>=24){//判断安卓版本是否高于7.0
            //getUriForFile将File对象转为URi对象，authority必须和Manifest的provider的authority保持一致，但是此字符串可以是唯一的任意值
            imageUri= FileProvider.getUriForFile(openCameraActivity.this,"com.example.cameraalbumtest.fileprovider",outputImage);
        }else{
            imageUri= Uri.fromFile(outputImage);//将File对象转为URi对象，Uri标识着图片的绝对路径
        }

    }

    //用于将照片数据返回上一个活动
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imageBase = "";
        try {
            //将拍摄的照片解析为bmp
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            Bitmap bmp = new ImageFactory().ratio(bitmap, 200, 300);
            //bmp转为byte数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //Intent传输的bytes（里面存放的图片）不能超过40k，超过后活动就不能正常传递数据
            //bmp.compress(Bitmap.CompressFormat.PNG,100,baos);//这种方式几乎不失真，但是图片太大
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            //此为压缩80%，保留图片20%的质量
            byte[] picbyte = baos.toByteArray();
            //这里使用系统自带的Base64的包
            imageBase = Base64.encodeToString(picbyte, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (requestCode) {
            //如果是登录行为，将照片数据传给signIn函数
            case USER_OPEN_LOCK_ACTION:
                String userName = "";
                pref = PreferenceManager.getDefaultSharedPreferences(this);
                userName = pref.getString("username", "");
                Log.d(TAG, "openCameraActivity userName: " + userName);

                String url = "http://" + domain + "/user/openlock";
                Map<String, String> params = new HashMap<>();

                params.put("name", userName);
                params.put("image", imageBase);
                Log.e(TAG,"准备调用sendRequestWithOkHttp");
                Log.d(TAG, "openCameraActivity imagebase: " + imageBase);
                Httphelper.sendRequestWithOkHttp(mHandle,url,params);
                openCameraActivity.this.finish();
        }
    }

    private Handler mHandle=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(Httphelper.result==true){
               makeToast("开锁中，请稍等...");
                }else{
                if(Httphelper.code==FACE_INFO_NOT_MATCH){
                    makeToast("人脸不匹配，请稍后再试...");
                }else if(Httphelper.code==USER_NOT_EXIST){
                    makeToast("服务器错误，请稍后再试...");
                }
            }
            return true;
        }
    });

    public void makeToast(String message){
        Toast.makeText(openCameraActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
