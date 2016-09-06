package com.sg.eyedoctor.loginRegister.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class GetAuthParams extends RequestParams {
    public String Loginid;

    public GetAuthParams(String loginid) {

        super(ConstantValues.HOST+"/Doctor/GetVerificationCode");
        Loginid = loginid;
    }


}
