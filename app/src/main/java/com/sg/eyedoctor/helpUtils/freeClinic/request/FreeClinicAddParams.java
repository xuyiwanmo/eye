package com.sg.eyedoctor.helpUtils.freeClinic.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/5/20.
 */
public class FreeClinicAddParams extends RequestParams {
    public String doctorId;
    public String clinicList;

    public FreeClinicAddParams() {
        super(ConstantValues.HOST+"/Share/FreeClinicAdd");
    }
}
