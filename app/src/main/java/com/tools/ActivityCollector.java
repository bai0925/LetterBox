package com.tools;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Acer on 2018/10/3.
 */
//专门用来管理活动
public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();
    //添加活动到表中
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        Log.d(TAG, "finishAll: 一共结束了"+activities.size()+"个活动");
        for (Activity activity:activities){
            activity.finish();
        }
    }
}
