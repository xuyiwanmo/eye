package com.sg.eyedoctor.settings.myAccount.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 支付宝账号绑定
 */
public class PayAccountAddParams extends RequestParams {
    public String doctorId;
    public String payAccount;
    public String name;

    public PayAccountAddParams(String doctorId, String payAccount, String name) {
        super(ConstantValues.HOST+"/Share/PayAccountAdd");
        this.doctorId = doctorId;
        this.payAccount = payAccount;
        this.name = name;
    }
}
