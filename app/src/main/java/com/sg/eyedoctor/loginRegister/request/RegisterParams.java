package com.sg.eyedoctor.loginRegister.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/9.
 */

public class RegisterParams extends RequestParams {
    public String Loginid;
    public String Password;

    public RegisterParams() {
        super(ConstantValues.HOST+"/Doctor/DoctorRegister");
    }
}
