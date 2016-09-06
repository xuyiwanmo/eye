package com.sg.eyedoctor.settings.myOnlineManager.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 医生开通在线服务
 */
public class VasServiceSetParams extends RequestParams {
    public String doctorId;
    public String type;
    public String price;


    public VasServiceSetParams(String doctorId, String type, String price) {
        super(ConstantValues.HOST+"/Share/VasServiceSet");
        this.doctorId = doctorId;
        this.type = type;
        this.price = price;
    }
}
