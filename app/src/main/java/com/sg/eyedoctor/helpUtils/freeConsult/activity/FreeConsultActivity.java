package com.sg.eyedoctor.helpUtils.freeConsult.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.helpUtils.freeConsult.fragment.FreeConsultFragment;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_free_consult)
public class FreeConsultActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener, ViewPager.OnPageChangeListener {


    @ViewInject(R.id.vp_consult)
    private ViewPager mConsultPager;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;
    @ViewInject(R.id.brush_tv)
    private TextView mBrushTv;

    private TabPagerAdapter mPagerAdapter;

    private FreeConsultFragment mAlreadyFragment;
    private FreeConsultFragment mCompleteFragment;
    private FreeConsultFragment mNewFragment;

    private ArrayList<Fragment> mFragments;
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<String>>(){}.getType();
                BaseResp<String> res=new Gson().fromJson(result, objectType);

                showToast(res.value);
                if(res.value.equals("刷题成功")){
                    mNewFragment.queryData();
                }
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };


    @Override
    protected void initView() {
        mNewFragment = FreeConsultFragment.newInstance(1);
        mAlreadyFragment = FreeConsultFragment.newInstance(2);
        mCompleteFragment = FreeConsultFragment.newInstance(3);

        mFragments = new ArrayList<>();
        mFragments.add(mNewFragment);
        mFragments.add(mAlreadyFragment);
        mFragments.add(mCompleteFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mConsultPager, mFragments, this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.free_consult_array));
        mTopIndicator.setOnTopIndicatorListener(this);
        mConsultPager.setAdapter(mPagerAdapter);
        mConsultPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
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
    //*******************************end********

    @Override
    public void onIndicatorSelected(int index) {
        LogUtils.i("page:    " + index);
        mConsultPager.setCurrentItem(index);
    }

    @Event(R.id.brush_tv)
    private void brush(View view) {
        //刷完题切换到第一页
        mConsultPager.setCurrentItem(0);

        showdialog();
        BaseManager.docSelectQuestion(mCallback);

    }


}
