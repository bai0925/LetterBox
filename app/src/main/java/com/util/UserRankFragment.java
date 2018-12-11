package com.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.acer.letterbox4.R;
import com.tools.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaiXiaozhe on 2018/11/25.
 */

public class UserRankFragment extends Fragment {

    private MyListView listView;
//    private ArrayAdapter<String> adapter;
    private List<UserInfo> userInfoList=new ArrayList<>();

//    private String[] data={"李杰雨","成立新","关林","王文治", "池乐",
//            "李杰雨","成立新","关林","王文治", "池乐",
//            "李杰雨","成立新","关林","王文治", "池乐"};

    @Nullable
    @Override
    //先获得一些控件的实例，然后去初始化ArrayAdapter，并将其设置为ListView的适配器。
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //绑定布局
        View view = inflater.inflate(R.layout.user_rank, container, false);
        initUserInfos();//初始化用户信息
        UserInfoAdapter adapter=new UserInfoAdapter(getContext(),R.layout.user_info_item,userInfoList);
        listView=(MyListView)view.findViewById(R.id.listViewForUserRank);
//        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        return view;
    }

    private void initUserInfos(){
        for(int i=0;i<1;i++){
            UserInfo first=new UserInfo("李杰雨","1");
            userInfoList.add(first);
            UserInfo second=new UserInfo("成立新","2");
            userInfoList.add(second);
            UserInfo third=new UserInfo("关林","3");
            userInfoList.add(third);
            UserInfo forth=new UserInfo("王文治","4");
            userInfoList.add(forth);
            UserInfo fifth=new UserInfo("池乐","5");
            userInfoList.add(fifth);
            UserInfo sixth=new UserInfo("陈莉莉","6");
            userInfoList.add(sixth);
            UserInfo seventh=new UserInfo("于志成","7");
            userInfoList.add(seventh);
            UserInfo eighth=new UserInfo("齐楚","8");
            userInfoList.add(eighth);
            UserInfo ninth=new UserInfo("赵晓娜","9");
            userInfoList.add(ninth);
            UserInfo tenth=new UserInfo("孙宁","10");
            userInfoList.add(tenth);
        }
    }


}
