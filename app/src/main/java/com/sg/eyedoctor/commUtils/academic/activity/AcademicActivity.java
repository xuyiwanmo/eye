package com.sg.eyedoctor.commUtils.academic.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.academic.fragment.AcademicFragment;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 学术前沿
 */
@ContentView(R.layout.activity_academic)
public class AcademicActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.academic_ti)
    private TopIndicator mIndicator;
    @ViewInject(R.id.academic_vp)
    private ViewPager mAcademicVp;

    private TabPagerAdapter mPagerAdapter;
    private ArrayList<Fragment> mFragments=new ArrayList<>();

    private AcademicFragment mFragment1;
    private AcademicFragment mFragment2;
    private AcademicFragment mFragment3;
    private AcademicFragment mFragment4;
    private AcademicFragment mFragment5;
    private AcademicFragment mFragment6;
    private AcademicFragment mFragment7;
    private AcademicFragment mFragment8;


    @Override
    protected void initView() {
        mFragment1 =AcademicFragment.newInstance(5);
        mFragment2 =AcademicFragment.newInstance(6);
        mFragment3 =AcademicFragment.newInstance(7);
        mFragment4 =AcademicFragment.newInstance(8);


        mFragments.add(mFragment1);
        mFragments.add(mFragment2);
        mFragments.add(mFragment3);
        mFragments.add(mFragment4);


        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mAcademicVp,mFragments,this);
        mIndicator.setLabels(getResources().getStringArray(R.array.academic_array), 14,48);
        mIndicator.setOnTopIndicatorListener(this);
        mAcademicVp.setAdapter(mPagerAdapter);
        mAcademicVp.invalidate();
        mPagerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initActionbar() {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.setTabsDisplay(mContext, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onIndicatorSelected(int index) {
        mAcademicVp.setCurrentItem(index);
    }


}
