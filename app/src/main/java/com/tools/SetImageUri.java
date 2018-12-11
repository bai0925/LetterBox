package com.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.example.acer.letterbox4.LoginActivity;

import java.io.File;
import java.io.IOException;

public class SetImageUri {
    //启动系统相机
    public static Uri setImageUri(Context context){
        Uri imageUri;
        //创建file对象，用于保存拍照的图片
        //getExternalCacheDir将图放在应用相关联缓存目录
        File outputImage=new File(context.getExternalCacheDir(),"output_image.jpg");
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
            imageUri= FileProvider.getUriForFile(context,"com.example.cameraalbumtest.fileprovider",outputImage);
        }else{
            imageUri= Uri.fromFile(outputImage);//将File对象转为URi对象，Uri标识着图片的绝对路径
        }
        return  imageUri;
    }
}
