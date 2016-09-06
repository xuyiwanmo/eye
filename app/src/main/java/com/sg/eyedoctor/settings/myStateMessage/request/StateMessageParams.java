package com.sg.eyedoctor.settings.myStateMessage.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 站内信息
 */
public class StateMessageParams extends RequestParams {
    public String doctorId;

    public StateMessageParams(String doctorId) {
        super(ConstantValues.HOST+"/Share/GetStateMessageByDoc");
        this.doctorId=doctorId;
    }
}
