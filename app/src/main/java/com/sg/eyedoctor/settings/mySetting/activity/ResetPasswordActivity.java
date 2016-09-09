package com.sg.eyedoctor.settings.mySetting.activity;

import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.EditView;
import com.sg.eyedoctor.loginRegister.activity.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 设置-修改密码
 */
@ContentView(R.layout.activity_reset_password)
public class ResetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.new_password_tv)
    private EditView mNewPasswordTv;
    @ViewInject(R.id.old_password_tv)
    private EditView mOldPasswordTv;
    @ViewInject(R.id.again_password_tv)
    private EditView mAgainPasswordTv;
    @ViewInject(R.id.ensure_tv)
    private TextView mEnsureTv;

    private String mFirstPassword;
    private String mSecondPassword;
    private String mOldPassword;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                DialogManager.showConfimDlg(mContext, R.string.password_update_ok, new DlgClick() {
                    @Override
                    public boolean click() {
                        logout();
                        LoginActivity.start(mContext, true);
                        return false;
                    }
                });
            } else {
                DialogManager.showConfimDlg(mContext, R.string.password_update_fail, new DlgClick() {
                    @Override
                    public boolean click() {

                        return false;
                    }
                });
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

    @Event(R.id.ensure_tv)
    private void next(View view) {
        mOldPassword = mOldPasswordTv.getEditText();
        if (mOldPassword == null || mOldPassword.equals("")) {
            showToast("请输入原密码");
            return;
        }
        mFirstPassword = mNewPasswordTv.getEditText();
        if (mFirstPassword == null || mFirstPassword.equals("")) {
            showToast("请输入新密码");
            return;
        }

        mSecondPassword = mAgainPasswordTv.getEditText();
        if (mSecondPassword == null || mSecondPassword.equals("")) {
            showToast("请再次输入新密码");
            return;
        }

        String rightpassword = BaseManager.getDoctor().password;
        if (!rightpassword.equals(mOldPassword)) {
            showToast("原密码有误，请重新输入");
            return;
        }

        if (!mFirstPassword.equals(mSecondPassword)) {
            showToast("新密码前后输入不一致，请重新输入");
            return;
        }

        if (mFirstPassword.length() < 6) {
            showToast("新密码不小于六位数");
        }else {
            BaseManager.passwordReset(mFirstPassword, mOldPassword, mCallback);
        }

    }

}
