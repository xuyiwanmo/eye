package com.sg.eyedoctor.consult.videoConsult.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.consult.videoConsult.fragment.VideoAddFragment;
import com.sg.eyedoctor.consult.videoConsult.fragment.VideoConsultFragment;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 视频咨询
 */
@ContentView(R.layout.activity_video_consult)
public class VideoConsultActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.vp_consult)
    private ViewPager mConsultPager;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;

    private TabPagerAdapter mPagerAdapter;

    private VideoAddFragment mAddFragment;
    private VideoConsultFragment mCompleteFragment;
    private VideoConsultFragment mWaitFragment;

    private ArrayList<Fragment> mFragments;

    /**
     * 问诊Id
     */
    private String mXtrId;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.operation_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        mXtrId = getIntent().getStringExtra(ConstantValues.KEY_XTR_ID);
        mAddFragment = new VideoAddFragment(mXtrId);
        mCompleteFragment = new VideoConsultFragment(4, mXtrId);  //已经完成
        mWaitFragment = new VideoConsultFragment(1, mXtrId);//等待视频
        mFragments = new ArrayList<>();
        mFragments.add(mAddFragment);
        mFragments.add(mWaitFragment);
        mFragments.add(mCompleteFragment);

        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mConsultPager, mFragments, this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.video_consult_array));
        mTopIndicator.setOnTopIndicatorListener(this);
        mConsultPager.setAdapter(mPagerAdapter);
        mConsultPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
        mConsultPager.setCurrentItem(1);//默认为待视频
    }

    @Override
    protected void initListener() {
        mSearchSl.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SearchVideoActivity.class);
                intent.putExtra(ConstantValues.KEY_XTR_ID,mXtrId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {

        mActionbar.setRightTv(R.string.close_video, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showConfimCancelDlg(mContext, R.string.close_confirm, new DlgClick() {
                    @Override
                    public boolean click() {
                        showdialog();
                        BaseManager.serviceRecordClose(mCallback);
                        return false;
                    }
                }, new DlgClick() {
                    @Override
                    public boolean click() {
                        return false;
                    }
                });

            }
        });

    }


    //************************Fragemnt页面切换*******
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogUtils.i("onPageScrolled");

    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.i("onPageSelected");
        mTopIndicator.setTabsDisplay(mContext, position);
    }

    @Override
    public void onPageScrollStateChanged(int position) {
        LogUtils.i("onPageScrollStateChanged");
    }
    //*******************************end********

    @Override
    public void onIndicatorSelected(int index) {
        LogUtils.i("onIndicatorSelected");
        mConsultPager.setCurrentItem(index);
    }
}
