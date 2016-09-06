package com.sg.eyedoctor.main.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.eyeCircle.fragment.CircleTopicCaseFragment;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 眼科圈
 */
@ContentView(R.layout.fragment_circle)
public class EyeCircleFragment extends BaseFragment implements TopIndicator.OnTopIndicatorListener,ViewPager.OnPageChangeListener  {

    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private TabPagerAdapter mPagerAdapter;

    private CircleTopicCaseFragment mTopicFragment;
    private CircleTopicCaseFragment mCaseFragment;

    private ArrayList<Fragment> mFragments;


    //*************************viewpager  start****************************
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        mTopIndicator.setTabsDisplay(getActivity(), position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //*****************************end**************************************

    @Override
    public void onIndicatorSelected(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void initView() {
        mTopicFragment=CircleTopicCaseFragment.newInstance(CircleTopicCaseFragment.TYPE_TOPIC);
        mCaseFragment=CircleTopicCaseFragment.newInstance(CircleTopicCaseFragment.TYPE_CASE);

        mFragments=new ArrayList<>();
        mFragments.add(mCaseFragment);
        mFragments.add(mTopicFragment);

        mPagerAdapter = new TabPagerAdapter(getFragmentManager(),mViewPager,mFragments,this);

        mTopIndicator.setLabels(getResources().getStringArray(R.array.team_array));
        mTopIndicator.setOnTopIndicatorListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    protected void initListener() {

    }

}
