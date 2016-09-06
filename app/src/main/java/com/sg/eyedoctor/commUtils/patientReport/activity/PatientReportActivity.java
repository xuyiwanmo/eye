package com.sg.eyedoctor.commUtils.patientReport.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.fragment.AlreadyReportFragment;
import com.sg.eyedoctor.commUtils.patientReport.fragment.PatientReportFragment;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 患者报到
 */
@ContentView(R.layout.activity_patient_report)
public class PatientReportActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener,ViewPager.OnPageChangeListener  {

    public static int mType= ConstantValues.REPORT_TYPE_ACCEPT;

    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    private PatientReportFragment mAcceptFragment;
    private AlreadyReportFragment mAlreadyFragment;
    private ArrayList<Fragment> mFragments;
    private TabPagerAdapter mPagerAdapter;

    @Override
    protected void initView() {
        mAcceptFragment=new PatientReportFragment();
        mAlreadyFragment=new AlreadyReportFragment();

        mFragments=new ArrayList<>();
        mFragments.add(mAcceptFragment);
        mFragments.add(mAlreadyFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mViewPager,mFragments,this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.patient_reprot_array));
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
                startActivity(new Intent(mContext,SearchPatientReportActivity.class));
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
        mTopIndicator.setTabsDisplay(mContext, position);
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
