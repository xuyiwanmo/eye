package com.sg.eyedoctor.commUtils.academic.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.academic.fragment.AcademicFragment;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.ConstantValues;
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

    private AcademicFragment mArticleFragment;
    private AcademicFragment mInformationFragment;
    private AcademicFragment mMeetingFragment;
    private AcademicFragment mNewsFragment;

    @Override
    protected void initView() {
        mArticleFragment=AcademicFragment.newInstance(ConstantValues.ACAD_TYPE_ARTICLE);
        mInformationFragment=AcademicFragment.newInstance(ConstantValues.ACAD_TYPE_INFORMATION);
        mMeetingFragment=AcademicFragment.newInstance(ConstantValues.ACAD_TYPE_MEETING);
        mNewsFragment=AcademicFragment.newInstance(ConstantValues.ACAD_TYPE_NEWS);

        mFragments.add(mArticleFragment);
        mFragments.add(mInformationFragment);
        mFragments.add(mMeetingFragment);
        mFragments.add(mNewsFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mAcademicVp,mFragments,this);
        mIndicator.setLabels(getResources().getStringArray(R.array.academic_array), 14);
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
