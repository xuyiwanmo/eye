package com.sg.eyedoctor.settings.mySetting.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 分享App
 */
public class ShareAppParams extends RequestParams {
    public String type;

    public ShareAppParams() {
        super(ConstantValues.HOST + "/Share/GetInviteCode");
        type="1";
    }


}
