package com.sg.eyedoctor.loginRegister.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/9.
 */

public class ResetPasswordParams extends RequestParams {
    public String loginid;
    public String password;

    public ResetPasswordParams() {
        super(ConstantValues.HOST+"/Doctor/ResetPassword");
    }
}
