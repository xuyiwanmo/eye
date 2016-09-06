package com.sg.eyedoctor.settings.myAccount.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.settings.myAccount.request.AlipayAccount;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 我的支付宝
 */
@ContentView(R.layout.activity_my_alipay)
public class MyAlipayActivity extends BaseActivity {

    @ViewInject(R.id.bind_text_tv)
    private TextView mBindTextTv;
    @ViewInject(R.id.name_et)
    private EditText mNameEt;
    @ViewInject(R.id.alipay_account_et)
    private EditText mAlipayAccountEt;
    @ViewInject(R.id.unbind_tv)
    private TextView mUnbindTv;
    @ViewInject(R.id.bind_tv)
    private TextView mBindTv;

    private boolean mIsBind = false;
    private AlipayAccount mAccount;

    private NetCallback mBindCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.bind_alipay_ok);
                mIsBind = true;
                Type objectType = new TypeToken<BaseResp<AlipayAccount>>() {
                }.getType();
                BaseResp<AlipayAccount> res = new Gson().fromJson(result, objectType);
                mAccount = res.value;
                if (mAccount.payAccount != null) {
                    mIsBind = true;
                } else {
                    mIsBind = false;
                }
                viewInit();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    private NetCallback mUnBindCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.unbind_alipay_ok);
                mAccount = null;
                mIsBind = false;
                viewInit();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    private NetCallback mQueryCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<AlipayAccount>>() {
                }.getType();
                BaseResp<AlipayAccount> res = new Gson().fromJson(result, objectType);
                mAccount = res.value;
                if (mAccount.payAccount != null) {
                    mIsBind = true;
                } else {
                    mIsBind = false;
                }
                viewInit();

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

    @Event(R.id.unbind_tv)
    private void unbindAlipay(View view) {
        if (!mIsBind) {
            showToast(R.string.not_bind_alipay);
            return;
        }
        DialogManager.showConfimCancelDlg(mContext, R.string.unbind_alipay_confirm, new DlgClick() {
            @Override
            public boolean click() {

                showdialog();
                BaseManager.payAccountCancel(mAccount.id, mUnBindCallback);

                return false;
            }
        }, new DlgClick() {
            @Override
            public boolean click() {
                return false;
            }
        });
    }

    @Event(R.id.bind_tv)
    private void bind(View view) {
        if (mIsBind) {
            showToast(R.string.bind_alipay_repeat);
            return;
        }
        String name = mNameEt.getText().toString();
        String payAcount = mAlipayAccountEt.getText().toString();
        if (name == null || name.equals("")) {
            showToast(R.string.name_empty);
        }
        if (payAcount == null || payAcount.equals("")) {
            showToast(R.string.alipay_empty);
        }

        showdialog();
        BaseManager.payAccountAdd(payAcount, name, mBindCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getPayAccount(mQueryCallback);
    }

    private void viewInit() {
        mBindTv.setSelected(!mIsBind);
        mUnbindTv.setSelected(mIsBind);

        setTextView(mNameEt, mDoctor.userName);
        if (mAccount != null) {
            setTextView(mAlipayAccountEt, mAccount.payAccount);
        } else {
            setTextView(mAlipayAccountEt, "");
        }
        mAlipayAccountEt.setFocusable(!mIsBind);
        mAlipayAccountEt.setFocusableInTouchMode(!mIsBind);


    }
}
