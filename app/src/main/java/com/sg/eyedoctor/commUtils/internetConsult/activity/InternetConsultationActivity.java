package com.sg.eyedoctor.commUtils.internetConsult.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.commUtils.internetConsult.fragment.InternetConsultationFragment;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.TopIndicator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 互联网会诊
 */
@ContentView(R.layout.activity_internet_consultation)
public class InternetConsultationActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener,ViewPager.OnPageChangeListener  {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;

    private TabPagerAdapter mPagerAdapter;

    private InternetConsultationFragment mMyFragment;
    private InternetConsultationFragment mJoinFragment;
    private InternetConsultationFragment mNewFragment;

    private ArrayList<Fragment> mFragments;

    @Override
    protected void initView() {

        mActionbar.setTitle(R.string.intent_consult);
        mActionbar.setRightBtnImg(R.drawable.ic_add_case, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InternetAddCaseActivity.class);
                startActivity(intent);

            }
        });

        mMyFragment=InternetConsultationFragment.newInstance(InternetConsultationFragment.TYPE_MY);
        mNewFragment=InternetConsultationFragment.newInstance(InternetConsultationFragment.TYPE_NEW);
        mJoinFragment=InternetConsultationFragment.newInstance(InternetConsultationFragment.TYPE_JOIN);

        mFragments=new ArrayList<>();
        mFragments.add(mNewFragment);
        mFragments.add(mJoinFragment);
        mFragments.add(mMyFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mViewPager,mFragments,this);

        mTopIndicator.setLabels(getResources().getStringArray(R.array.internet_consult_array));
        mTopIndicator.setOnTopIndicatorListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initListener() {
        mSearchSl.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,SearchInternetConsultActivity.class));
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    //*************************viewpager  start****************************
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTopIndicator.setTabsDisplay(this, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //*****************************end**************************************

    @Override
    public void onIndicatorSelected(int index) {
        mViewPager.setCurrentItem(index);
    }


}
