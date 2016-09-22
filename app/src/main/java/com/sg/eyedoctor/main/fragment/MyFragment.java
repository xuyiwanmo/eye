package com.sg.eyedoctor.main.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.main.bean.Earnings;
import com.sg.eyedoctor.settings.myAccount.activity.AccountManageActivity;
import com.sg.eyedoctor.settings.myOnlineManager.activity.OnlineManagerActivity;
import com.sg.eyedoctor.settings.mySetting.activity.SettingActivity;
import com.sg.eyedoctor.settings.mySetting.activity.ShareAppActivity;
import com.sg.eyedoctor.settings.myStateMessage.activity.StateMessageActivity;
import com.sg.eyedoctor.settings.myStore.activity.MyStoreActivity;
import com.sg.eyedoctor.settings.myWallet.activity.MyWalletActivity;
import com.sg.eyedoctor.settings.personalInfo.activity.DoctorInfoActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 我的
 */
@ContentView(R.layout.fragment_my)
public class MyFragment extends BaseFragment {
    @ViewInject(R.id.img_doctor_head)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.tv_my_username)
    private TextView mNameTv;
    @ViewInject(R.id.tv_my_earn)
    private TextView mMonthMoneyTv;
    @ViewInject(R.id.tv_account_earn)
    private TextView mLeftMoneyTv;
    @ViewInject(R.id.my_station_notice_rl)
    private RelativeLayout mNoticeRl;
    @ViewInject(R.id.rl_my_setting)
    private RelativeLayout mSettingRl;
    @ViewInject(R.id.rl_my_account_manager)
    private RelativeLayout mAccountRl;
    @ViewInject(R.id.rl_my_wallet)
    private RelativeLayout mWalletRl;
    @ViewInject(R.id.my_store_rl)
    private RelativeLayout mStoreRl;
    @ViewInject(R.id.rl_my_share_app)
    private RelativeLayout mShareAppRl;

    private Earnings mEarnings;

    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<Earnings>>(){}.getType();
                BaseResp<Earnings> res=new Gson().fromJson(result, objectType);
                mEarnings=res.value;
                mMonthMoneyTv.setText(CommonUtils.getDecimal(mEarnings.monthlyEarnnings+""));
                mLeftMoneyTv.setText(CommonUtils.getDecimal(mEarnings.walletSum+""));

            }else{
                showToast("获取金额失败!");
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        CommonUtils.loadImg(mDoctor.picFileName, mHeadImg);
        mNameTv.setText(mDoctor.userName);
        BaseManager.getEarnings(mCallback);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Event(R.id.img_doctor_head)
    private void onclick(View view) {
        startActivity(new Intent(getActivity(), DoctorInfoActivity.class));
    }
    @Event(R.id.my_station_notice_rl)
    private void clickNotice(View view) {
        startActivity(new Intent(getActivity(), StateMessageActivity.class));
    }
    @Event(R.id.rl_my_setting)
    private void clickSetting(View view) {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }
    @Event(R.id.rl_my_account_manager)
    private void clickAccount(View view) {
        if(!mIsAuth){
            startAuthActivity(R.string.my_account_manager);
            return;
        }
        startActivity(new Intent(getActivity(), AccountManageActivity.class));
    }
    @Event(R.id.rl_my_wallet)
    private void clickWallet(View view) {
        if(!mIsAuth){
            startAuthActivity(R.string.my_wallet);
            return;
        }
        startActivity(new Intent(getActivity(), MyWalletActivity.class));
    }
    @Event(R.id.my_store_rl)
    private void clickStore(View view) {
        startActivity(new Intent(getActivity(), MyStoreActivity.class));
    }
    @Event(R.id.rl_my_share_app)
    private void shareApp(View view) {
        startActivity(new Intent(getActivity(), ShareAppActivity.class));
    }
    @Event(R.id.rl_my_service_manager)
    private void clickService(View view) {
        if(!mIsAuth){
            startAuthActivity(R.string.online_manager);
            return;
        }
        startActivity(new Intent(getActivity(), OnlineManagerActivity.class));
    }




}
