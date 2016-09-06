package com.sg.eyedoctor.settings.myWallet.activity;

import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 我要提现
 */
@ContentView(R.layout.activity_want_withdrawal)
public class WantWithdrawalActivity extends BaseActivity {

    @ViewInject(R.id.withdrawal_tv)
    private TextView mWithdrawalTv;

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.withdrawal_tv)
    private void withdrawal(View view){

    }
}
