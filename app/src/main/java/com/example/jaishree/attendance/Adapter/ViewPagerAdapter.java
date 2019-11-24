package com.example.jaishree.attendance.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaishree on 04-07-2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList=new ArrayList<>();
    private final List<String> stringList=new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        stringList.add(title);
    }
    public CharSequence getPageTitle(int position){
        return stringList.get(position);
    }
}
