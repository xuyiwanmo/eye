package com.sg.eyedoctor.settings.myAccount.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 支付宝绑定
 */
@ContentView(R.layout.activity_alipaybind)
public class AlipayBindActivity extends BaseActivity {
    @ViewInject(R.id.bind_name_et)
    private EditText mNameEt;
    @ViewInject(R.id.bind_alipay_account_et)
    private EditText mAlipayAccountEt;
    @ViewInject(R.id.bind_tv)
    private TextView mBindTv;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.bind_alipay_ok);
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

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.bind_tv)
    private void bindAlipay(View view) {
        Intent intent = new Intent(mContext, AlipayBindActivity.class);
        startActivity(intent);
    }

    @Event(R.id.bind_tv)
    private void bind(View view) {
        String name = mNameEt.getText().toString();
        String payAcount = mAlipayAccountEt.getText().toString();
        if(name==null||name.equals("")){
            showToast(R.string.name_empty);
        }
        if(payAcount==null||payAcount.equals("")){
            showToast(R.string.alipay_empty);
        }

        BaseManager.payAccountAdd(payAcount,name,mCallback);
    }




}
