package com.tools;

/**
 * Created by BaiXiaozhe on 2018/11/25.
 */

public class UserInfo {

    private String name;
    private String rank;

    public UserInfo(String name, String rank){
        this.name=name;
        this.rank=rank;
    }

    public String getName(){
        return name;
    }

    public String getRank(){
        return rank;
    }
}
