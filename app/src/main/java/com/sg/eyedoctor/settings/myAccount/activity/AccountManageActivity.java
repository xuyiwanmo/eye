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

    @ViewInject(R.id.account_ma)
    private MyActionbar mActionbar;

    @ViewInject(R.id.alipay_mtv)
    private MenuTextView mAlipayMtv;

    @Override
    protected void initView() {
        mAlipayMtv.setShowTv(R.string.my_alipay);
    }

    @Override
    protected void initListener() {
        mAlipayMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyAlipayActivity.class));
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.setting);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
    }

}
