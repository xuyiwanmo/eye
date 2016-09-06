package com.sg.eyedoctor.consult.textConsult.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.consult.textConsult.fragment.ConsultNewFragment;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 图文咨询
 */
@ContentView(R.layout.activity_consult_text)
public class TextConsultActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;

    @ViewInject(R.id.vp_consult)
    private ViewPager mConsultPager;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;

    private TabPagerAdapter mPagerAdapter;

    private  ConsultNewFragment mAlreadyFragment;
    private  ConsultNewFragment mCompleteFragment;
    private  ConsultNewFragment mNewFragment;

    private ArrayList<Fragment> mFragments;
    private int mIndex;

    @Override
    protected void initView() {
        mAlreadyFragment = ConsultNewFragment.newInstance(5);
        mCompleteFragment = ConsultNewFragment.newInstance(4);
        mNewFragment = ConsultNewFragment.newInstance(1);
        mFragments = new ArrayList<>();
        mFragments.add(mNewFragment);
        mFragments.add(mAlreadyFragment);
        mFragments.add(mCompleteFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mConsultPager, mFragments, this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.text_consult_array));
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
                startActivity(new Intent(mContext,SearchTextActivity.class));
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    //************************Fragemnt页面切换*******
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTopIndicator.setTabsDisplay(mContext, position);
        mIndex = position;
//        showdialog();
//        queryData();
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }
    //*******************************end********

    @Override
    public void onIndicatorSelected(int index) {
        LogUtils.i("page:    "+index);
        mConsultPager.setCurrentItem(index);
    }

    public void patientPushAgree(String orderNo){
        ((ConsultNewFragment)mFragments.get(mIndex)).queryData();
    }
}
