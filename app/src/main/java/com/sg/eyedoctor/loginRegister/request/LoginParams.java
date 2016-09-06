package com.sg.eyedoctor.loginRegister.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */
//@HttpRequest(
//        host = ConstantValues.HOST,
//        path = ConstantValues.URL_LOGIN,
//        builder = DefaultParamsBuilder.class
//)
public class LoginParams extends RequestParams {
    public String loginId;
    public String password;

    public LoginParams() {
        super(ConstantValues.HOST+"/Doctor/DoctorLogin");
    }
}
