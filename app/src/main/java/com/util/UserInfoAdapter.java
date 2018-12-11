package com.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.acer.letterbox4.R;
import com.tools.UserInfo;

import java.util.List;

/**
 * Created by BaiXiaozhe on 2018/11/25.
 */

public class UserInfoAdapter extends ArrayAdapter<UserInfo>{

    private int resourceId;

    public UserInfoAdapter(Context context, int textViewResourceId, List<UserInfo> objects){

        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;

    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        UserInfo userinfo=getItem(position);//获取当前的UserInfo实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView userName=(TextView)view.findViewById(R.id.user_name);
        TextView userRank=(TextView)view.findViewById(R.id.user_rank);
        userName.setText(userinfo.getName());
        userRank.setText(userinfo.getRank());
        return view;
    }

}
