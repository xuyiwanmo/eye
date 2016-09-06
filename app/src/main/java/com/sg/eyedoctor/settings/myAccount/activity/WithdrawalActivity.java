package com.sg.eyedoctor.settings.myAccount.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.view.PasswordView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 我要提现
 */
@ContentView(R.layout.activity_withdrawal)
public class WithdrawalActivity extends BaseActivity {

    @ViewInject(R.id.name_et)
    private EditText mNameEt;
    @ViewInject(R.id.money_et)
    private EditText mMoneyEt;
    @ViewInject(R.id.all_withdrawal_tv)
    private TextView mAllWithdrawalTv;
    @ViewInject(R.id.withdrawal_tv)
    private TextView mWithdrawalTv;
    @ViewInject(R.id.over_tv)
    private TextView mOverTv;
    @ViewInject(R.id.alipay_pv)
    private PasswordView mAlipayPv;
    @ViewInject(R.id.all_ll)
    private LinearLayout mAlipayll;

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
    private void withdrawal(View view) {
        startActivity(new Intent(mContext, AlipayPasswordActvity.class));
    }

    @Event(R.id.all_withdrawal_tv)
    private void clickAllWithdrawal(View view) {
        AlertDialog dialog = DialogManager.showConfimDlg(mContext, R.string.over_money, new DlgClick() {
            @Override
            public boolean click() {
               return false;
            }
        });
    }
}
