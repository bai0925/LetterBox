package com.viewpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Acer on 2018/9/27.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    public void setList(List<Fragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    //返回要滑动的View的个数
    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }
}
