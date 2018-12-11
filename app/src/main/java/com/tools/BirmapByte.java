package com.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by 29924 on 2018/8/13.
 */

public class BirmapByte {

    public static Bitmap Bytes2Bimap(byte[] b) {
        Log.d(TAG, "Bytes2Bimap: 执行");
        if (b.length != 0) {
            Log.d(TAG, "Bytes2Bimap: 收到相机传来的图片");
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            Log.d(TAG, "Bytes2Bimap: 未收到相机传来的图片");
            return null;
        }
    }
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

}
