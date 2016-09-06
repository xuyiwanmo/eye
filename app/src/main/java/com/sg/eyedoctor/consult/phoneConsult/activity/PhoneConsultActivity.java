package com.sg.eyedoctor.consult.phoneConsult.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.consult.phoneConsult.fragment.PhoneConsultFragment;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 视频咨询
 */
@ContentView(R.layout.activity_consult_phone)
public class PhoneConsultActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.vp_consult)
    private ViewPager mConsultPager;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;

    private TabPagerAdapter mPagerAdapter;

    private PhoneConsultFragment mAlreadyFragment;
    private PhoneConsultFragment mNewFragment;

    private ArrayList<Fragment> mFragments;
    private int mIndex = 0;
    private int mType = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showdialog();
        queryData();
    }

    @Override
    protected void initView() {
        mAlreadyFragment = new PhoneConsultFragment(4);
        mNewFragment = new PhoneConsultFragment(1);
        mFragments = new ArrayList<>();
        mFragments.add(mNewFragment);
        mFragments.add(mAlreadyFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mConsultPager, mFragments, this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.phone_consult_array));
        mTopIndicator.setOnTopIndicatorListener(this);
        mConsultPager.setAdapter(mPagerAdapter);
        mConsultPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        mSearchSl.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,SearchPhoneActivity.class));
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PhoneConsultSettingActivity.class));

            }
        });
    }

    //************************Fragemnt页面切换*******
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTopIndicator.setTabsDisplay(mContext, position);
        mIndex = position;
        showdialog();
        queryData();
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }
    //*******************************end********

    @Override
    public void onIndicatorSelected(int index) {
        mConsultPager.setCurrentItem(index);
    }

    private void queryData() {
        if (mIndex == 0) {
            mType = 1;
        } else if (mIndex == 1) {
            mType = 4;
        }

    }


}
