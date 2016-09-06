package com.sg.eyedoctor.settings.myAccount.activity;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.DialogManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 提现详情
 */
@ContentView(R.layout.activity_withdrawal_detail)
public class WithdrawalDetailActivity extends BaseActivity {
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
        AlertDialog dialog = DialogManager.showConfimDlg(mContext, R.string.error_account, new DlgClick() {
            @Override
            public boolean click() {
                return false;
            }
        });
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
