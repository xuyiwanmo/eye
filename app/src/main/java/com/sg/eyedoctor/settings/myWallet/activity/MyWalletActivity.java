package com.sg.eyedoctor.settings.myWallet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.WalletLayout;
import com.sg.eyedoctor.settings.myAccount.activity.WithdrawalActivity;
import com.sg.eyedoctor.settings.myWallet.bean.WalletSum;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;


@ContentView(R.layout.activity_my_wallet)
public class MyWalletActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.my_money_tv)
    private TextView mMyMoneyTv;
    @ViewInject(R.id.remain_money_tv)
    private TextView mRemainMoneyTv;
    @ViewInject(R.id.text_consult_wl)
    private WalletLayout mTextConsultWl;
    @ViewInject(R.id.phone_consult_wl)
    private WalletLayout mPhoneConsultWl;
    @ViewInject(R.id.video_consult_wl)
    private WalletLayout mVideoConsultWl;
    @ViewInject(R.id.add_appointment_wl)
    private WalletLayout mAddAppointmentWl;
    @ViewInject(R.id.money_rl)
    private RelativeLayout mMoneyRl;
    @ViewInject(R.id.withdrawal_tv)
    private TextView mWithdrawalTv;

    private WalletSum mWalletSum;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<WalletSum>>() {
                }.getType();
                BaseResp<WalletSum> res = new Gson().fromJson(result, objectType);
                mWalletSum = res.value;
                viewInit();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private void viewInit() {
        mRemainMoneyTv.setText(CommonUtils.getDecimal(mWalletSum.walletSum + ""));
        mAddAppointmentWl.setMoneyTv(mWalletSum.xtrSum);
        mAddAppointmentWl.setCountTv(mWalletSum.xtrCount);
        mPhoneConsultWl.setMoneyTv(mWalletSum.phoneSum);
        mPhoneConsultWl.setCountTv(mWalletSum.phoneCount);
        mVideoConsultWl.setMoneyTv(mWalletSum.videoSum);
        mVideoConsultWl.setCountTv(mWalletSum.videoCount);
        mTextConsultWl.setMoneyTv(mWalletSum.textSum);
        mTextConsultWl.setCountTv(mWalletSum.textCount);
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initListener() {
        mAddAppointmentWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityStart(ConstantValues.TYPE_PLUS);
            }
        });
        mPhoneConsultWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityStart(ConstantValues.TYPE_PHONE);
            }
        });
        mVideoConsultWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityStart(ConstantValues.TYPE_VIDEO);
            }
        });
        mTextConsultWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityStart(ConstantValues.TYPE_TEXT);
            }
        });

    }


    @Override
    protected void initActionbar() {

    }

    @Event(R.id.withdrawal_tv)
    private void clickWithDrawal(View view) {
        startActivity(new Intent(mContext, WithdrawalActivity.class));
    }

    @Event(R.id.money_rl)
    private void moneyDetail(View view) {
        startActivity(new Intent(mContext, IncomeDetailActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getWalletSum(mCallback);
    }

    private void activityStart(int type) {
        Intent intent=new Intent(this,MonthIncomeDetailActivity.class);
        intent.putExtra(ConstantValues.KEY_TYPE,type);
        startActivity(intent);
    }
}

