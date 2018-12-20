package com.example.acer.letterbox4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class InputPerformanceActivity extends AppCompatActivity {

    private Bitmap head;//头像
    private static String path="/sdcard/DemoHead/";//sd路径
    private ImageView headshot;//头像


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
        setContentView(R.layout.activity_input_performance);

        initView();


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

    public void initView(){
        headshot=(ImageView) LayoutInflater.from(InputPerformanceActivity.this).
                inflate(R.layout.myinfo,null).findViewById(R.id.headshot);

        Bitmap bt= BitmapFactory.decodeFile(path+"head.jpg");//从sd中找头像，转换成Bitmap
        if(bt!=null){
            //如果本地有头像图片的话
            headshot.setImageBitmap(bt);
        }else{
            //如果本地没有头像图片则从服务器获取头像，然后保存在SD卡中
        }
    }

    public void changeHeadshot(View view){

        Intent intent1=new Intent(Intent.ACTION_PICK,null);//返回被选中项的URI
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");//得到所有图片的URI
        startActivityForResult(intent1,1);

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            //从相册里面取相片的返回结果
            case 1:
                if(resultCode==RESULT_OK){
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            //调用系统裁剪图片后
            case 2:
                if(data!=null){
                    Bundle extras=data.getExtras();
                    head=extras.getParcelable("data");
                    if(head!=null){
                        //上传服务器代码
                        setPicToView(head);//保存在SD卡中
                        headshot.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    //调用系统的裁剪

}
