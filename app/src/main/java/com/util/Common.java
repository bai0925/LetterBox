package com.util;

/**
 * Created by BaiXiaozhe on 2018/10/13.
 */

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Common {
    /**
     * 安装应用
     *
     * @param context
     * @param file
     */
    public static void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 解决升级过程中闪退的问题
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean isSDCardAvaliable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取屏幕的大�?：宽�?1：高�?
     */
    public static int[] getScreen(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return new int[]{(int) (outMetrics.density * outMetrics.widthPixels),
                (int) (outMetrics.density * outMetrics.heightPixels)};
    }

    /**
     * 判断网络是否可用
     */
    public static boolean getNetIsAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.isAvailable();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * 编码utf-8
     */
    public String EcodeUTF8(String source) throws UnsupportedEncodingException {
        return new String(source.getBytes("UTF-8"));
    }

    /**
     * 解码utf-8
     */
    public String DecodeUTF8(String source) throws UnsupportedEncodingException {
        return new String(source.getBytes(), "UTF-8");
    }

    /**
     * 提示消息
     */
    public static Toast showMessage(Toast toastMsg, Context context, String msg) {
        if (toastMsg == null) {
            toastMsg = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toastMsg.setText(msg);
        }
        toastMsg.show();
        return toastMsg;
    }

    /**
     * 根据文件名获取不带后�?��的文件名
     */
    public static String clearSuffix(String str) {
        int i = str.lastIndexOf(".");
        if (i != -1) {
            return str.substring(0, i);
        }
        return str;
    }

    /**
     * 计算百分�?
     */
    public static String getPercent(int n, float total) {
        float rs = (n / total) * 100;
        // 判断是否是正整数
        if (String.valueOf(rs).indexOf(".0") != -1) {
            return String.valueOf((int) rs);
        } else {
            return String.format("%.1f", rs);
        }
    }

    /**
     * 获取文件的后�?��，返回大�?
     */
    public static String getSuffix(String str) {
        int i = str.lastIndexOf('.');
        if (i != -1) {
            return str.substring(i + 1).toUpperCase();
        }
        return str;
    }

    /**
     * 修改文件�?
     */
    public static String renameFileName(String str) {
        int i = str.lastIndexOf('.');
        if (i != -1) {
            File file = new File(str);
            file.renameTo(new File(str.substring(0, i)));
            return str.substring(0, i);
        }
        return str;
    }

    /**
     * 根据文件路径获取文件目录
     */
    public static String clearFileName(String str) {
        int i = str.lastIndexOf(File.separator);
        if (i != -1) {
            return str.substring(0, i + 1);
        }
        return str;
    }

    /**
     * 根据文件路径获取不带后缀名的文件�?
     */
    public static String clearDirectory(String str) {
        int i = str.lastIndexOf(File.separator);
        if (i != -1) {
            return clearSuffix(str.substring(i + 1, str.length()));
        }
        return str;
    }

    /**
     * 格式化毫�?>00:00
     */
    public static String formatSecondTime(int millisecond) {
        if (millisecond == 0) {
            return "00:00";
        }
        millisecond = millisecond / 1000;
        int m = millisecond / 60 % 60;
        int s = millisecond % 60;
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

    /**
     * 格式化文件大�?Byte->MB
     */
    public static String formatByteToMB(int size) {
        float mb = size / 1024f / 1024f;
        return String.format("%.2f", mb);
    }

    /**
     * 格式化文件大�?Byte->KB
     */
    public static String formatByteToKB(int size) {
        float kb = size / 1024f;
        return String.format("%.2f", kb);
    }

    /**
     * 判断目录是否存在，不在则创建
     */
    public static File isExistDirectory(String directoryName) {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获得SD目录路径
     */
    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isExistFile(String file) {
        return new File(file).exists();
    }

    /**
     * �?��SD卡是否已装载
     */
    public static boolean isExistSdCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 删除文件并删除媒体库中数�?
     */
    public static boolean deleteFile(Context context, String filePath) {
        new File(filePath).delete();
        ContentResolver cr = context.getContentResolver();
        int id = -1;
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{BaseColumns._ID}, MediaColumns.DATA + "=?",
                new String[]{filePath}, null);
        if (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        if (id != -1) {
            return cr.delete(ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id), null,
                    null) > 0;
        }
        return false;
    }

    public static boolean isStrNull(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        } else if (str.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }
}
