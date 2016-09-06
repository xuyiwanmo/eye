package com.sg.eyedoctor.settings.myWallet.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;
import com.sg.eyedoctor.settings.myWallet.fragment.MonthIncomeDetailFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 收入明细详情
 */
@ContentView(R.layout.activity_month_income_detail)
public class MonthIncomeDetailActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.vp_consult)
    private ViewPager mConsultPager;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;

    private TabPagerAdapter mPagerAdapter;

    private MonthIncomeDetailFragment mTextFragment;
    private MonthIncomeDetailFragment mVideoFragment;
    private MonthIncomeDetailFragment mPhoneFragment;
    private MonthIncomeDetailFragment mAppointmentFragment;
    private ArrayList<Fragment> mFragments;
    private int mType;

    @Override
    protected void initView() {
        mType=getIntent().getIntExtra(ConstantValues.KEY_TYPE,ConstantValues.TYPE_TEXT);
        mFragments=new ArrayList<>();
        mTextFragment=new MonthIncomeDetailFragment(ConstantValues.TYPE_TEXT);
        mPhoneFragment=new MonthIncomeDetailFragment(ConstantValues.TYPE_PHONE);
        mVideoFragment=new MonthIncomeDetailFragment(ConstantValues.TYPE_VIDEO);
        mAppointmentFragment=new MonthIncomeDetailFragment(ConstantValues.TYPE_PLUS);
        mFragments.add(mTextFragment);
        mFragments.add(mPhoneFragment);
        mFragments.add(mVideoFragment);
        mFragments.add(mAppointmentFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mConsultPager, mFragments, this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.month_income));
        mTopIndicator.setOnTopIndicatorListener(this);
        mConsultPager.setAdapter(mPagerAdapter);
        mConsultPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
        mConsultPager.setCurrentItem(mType-1);

    }

    @Override
    protected void initListener() {

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

    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onIndicatorSelected(int index) {
        mConsultPager.setCurrentItem(index);
    }
    //*******************************end********


}
