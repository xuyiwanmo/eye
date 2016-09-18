package com.sg.eyedoctor.common.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 发送短信
 */
public class SendMessageParams extends RequestParams {

    public String tel;
    public String doctorName;
    public String type;

    public SendMessageParams() {
        super(ConstantValues.HOST + "/His/SendMessage");
    }

}
