package com.sg.eyedoctor.settings.myAccount.activity;

import android.content.Intent;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.MenuTextView;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 账户管理
 */
@ContentView(R.layout.activity_account_manage)
public class AccountManageActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.bank_card_mtv)
    private MenuTextView mBankCardMtv;
    @ViewInject(R.id.alipay_mtv)
    private MenuTextView mAlipayMtv;
    @ViewInject(R.id.withdrawal_mtv)
    private MenuTextView mWithdrawalMtv;

    @Override
    protected void initView() {
        mBankCardMtv.setShowTv(R.string.my_bank_card);
        mAlipayMtv.setShowTv(R.string.my_alipay);
        mWithdrawalMtv.setShowTv(R.string.withdrawal_password);
    }

    @Override
    protected void initListener() {
        mBankCardMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAlipayMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyAlipayActivity.class));
            }
        });
        mWithdrawalMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.setting);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
    }

}
