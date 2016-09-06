package com.sg.eyedoctor.loginRegister.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.loginRegister.bean.RegisterResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.et_register_phone)
    private EditText mPhoneEt;
    @ViewInject(R.id.et_register_authcode)
    private EditText mAuthcodeEt;
    @ViewInject(R.id.tv_register_get_authcode)
    private TextView mGetAuthcodeTv;
    @ViewInject(R.id.et_register_password)
    private EditText mPasswordEt;
    @ViewInject(R.id.img_password_visible)
    private ImageView mPasswordVisibleImg;
    @ViewInject(R.id.tv_register)
    private TextView mRegisterTv;

    private boolean mClickable = true;
    private int mAuthTime = 40;
    private String mAuthCode = "";
    private String mLoginId = "";

    private boolean mPasswrodVisible = false;

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
    private NetCallback mRegisterCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            RegisterResponse response = new Gson().fromJson(result, RegisterResponse.class);

            if (response.code == 10000) {
                showToast(R.string.register_success);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();

            } else if (response.code == -10000) {
                showToast(response.message);
                mPasswordEt.setText("");
                mPhoneEt.setText("");
                mAuthcodeEt.setText("");

            }
        }

        @Override
        protected void requestError(Throwable ex, boolean isOnCallback) {

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

    @Event(R.id.tv_register_get_authcode)
    private void getAuthcode(View view) {
        if (!mClickable) {
            return;
        }
        String phone = mPhoneEt.getText().toString();

        if (!CommonUtils.isMobile(phone)) {
            showToast(R.string.enter_right_phone);
        } else {
            BaseManager.getAuth(phone, mAuthCallback);
            mClickable = false;
            mLoginId = phone;
            setAuthText();
        }

    }

    @Event(R.id.tv_register)
    private void register(View view) {
        String auth = mAuthcodeEt.getText().toString();
        String phone = mPhoneEt.getText().toString();
        String password = getText(mPasswordEt);

        if (!CommonUtils.isMobile(phone)) {
            showToast(R.string.enter_right_phone);
            return;
        }
        if(TextUtils.isEmpty(password)) {
            showToast(R.string.empty_password);
            return;
        }
        if (mAuthCode.equals(auth) && phone.equals(mLoginId)) {//验证码错误
            String name = getText(mPhoneEt);

            showdialog();
            BaseManager.register(name, password, mRegisterCallback);
        } else {
            showToast(R.string.auth_end_again);

        }

    }

    @Event(R.id.img_password_visible)
    private void checkPasswordVisible(View view) {
        mPasswrodVisible = !mPasswrodVisible;
        if (mPasswrodVisible) {
            // 显示密码
            mPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // 隐藏密码
            mPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        CharSequence text = mPasswordEt.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());// 将光标移动到最后
        }
        mPasswordVisibleImg.setSelected(mPasswrodVisible);

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
