package com.sg.eyedoctor.loginRegister.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 引导页 adapter
 */
public class GuideAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment>  mFragments;

    public GuideAdapter(FragmentManager fm,ArrayList<Fragment>  fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
