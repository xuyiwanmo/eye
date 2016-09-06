package com.sg.eyedoctor.loginRegister.activity;

import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.EditView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_forget_password)
public class ForgetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.phone_ev)
    private EditView mPhoneEv;
    @ViewInject(R.id.authcode_et)
    private EditText mAuthcodeEt;
    @ViewInject(R.id.get_authcode_tv)
    private TextView mGetAuthcodeTv;
    @ViewInject(R.id.new_password_ev)
    private EditView mNewPasswordEv;
    @ViewInject(R.id.password_confirm_ev)
    private EditView mPasswordConfirmEv;
    @ViewInject(R.id.post_tv)
    private TextView mPostTv;
    @ViewInject(R.id.doctor_assist_tv)
    private TextView mDoctorAssistTv;

    private boolean mClickable = true;
    private int mAuthTime = 40;
    private String mAuthCode = "";
    private String mPassword;
    private String mRightPhone;

    private NetCallback mAuthCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                try {
                    JSONObject object = new JSONObject(result.toString());
                    mAuthCode = object.getString("value");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                showToast(R.string.send_auth_fail);
                resetAuth();
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mUpdateCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                DialogManager.showConfimDlg(mContext, R.string.update_password_ok, new DlgClick() {
                    @Override
                    public boolean click() {
                        LoginActivity.start(mContext, true);
                        return false;
                    }
                });
            } else {
                showToast(CommonUtils.getMsg(result));
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mDoctorAssistTv.setText(Html.fromHtml("<font color='#434a53'>如需帮助,请拨打医助热线</font><font color='#3bafd9'>0592-12548622</font>"));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.get_authcode_tv)
    private void getAuthCode(View view) {
        if (!mClickable) {
            return;
        }
        String phone = mPhoneEv.getEditText();

        if (!CommonUtils.isMobile(phone)) {
            showToast(R.string.enter_right_phone);
        } else {
            mRightPhone =phone;
            BaseManager.getAuth(phone, mAuthCallback);
            mClickable = false;
            setAuthText();
        }
    }

    @Event(R.id.post_tv)
    private void post(View view) {
        String auth = mAuthcodeEt.getText().toString();
        String phone = mPhoneEv.getEditText();

        String newPassword=mNewPasswordEv.getEditText();
        String confirmPassword=mPasswordConfirmEv.getEditText();

        if (!CommonUtils.isMobile(phone)) {
            showToast(R.string.enter_right_phone);
            return;
        }

        if(!mRightPhone.equals(phone)){
            showToast(R.string.can_not_change_phone);
            return;
        }

        if(newPassword==null||newPassword.equals("")){
            showToast(R.string.please_enter_new_password);
            return;
        }
        if(confirmPassword==null||confirmPassword.equals("")){
            showToast(R.string.please_enter_password_again);
            return;
        }
        if(!confirmPassword.equals(newPassword)){
            showToast(R.string.twice_password_not_same);
            return;
        }

        if (mAuthCode.equals(auth) ) {
            String name = phone;
            showdialog();
        } else { //验证码错误
            DialogManager.showConfimDlg(mContext, R.string.auth_end_again, new DlgClick() {
                @Override
                public boolean click() {
                    mAuthcodeEt.setText("");
                    return false;
                }
            });
            return;
        }
        showdialog();
        BaseManager.resetPassword(mRightPhone,confirmPassword,mUpdateCallback);
    }

    private void setAuthText() {
        mGetAuthcodeTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuthTime == 0) {
                    resetAuth();
                } else if (!mClickable) {
                    mAuthTime--;
                    mGetAuthcodeTv.setText(mAuthTime + getString(R.string.after_seconds_resend));
                    mGetAuthcodeTv.setBackgroundResource(R.drawable.bg_auth_grey);
                    mGetAuthcodeTv.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }

    private void resetAuth() {

        mAuthTime = 40;
        mClickable = true;
        mGetAuthcodeTv.setText(R.string.register_get_authcode);
        mGetAuthcodeTv.setBackgroundResource(R.drawable.bg_auth_green);
    }
}
