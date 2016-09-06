package com.sg.eyedoctor.consult.videoConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 医生开启视频问诊
 */

public class ServiceRecordAddParams extends RequestParams {
    public String doctorId;
    public String serviceAmount;
    public String isFree;

    public ServiceRecordAddParams(String doctorId, String serviceAmount,String isFree) {

        super(ConstantValues.HOST+"/Share/ServiceRecordAdd");
        this.doctorId = doctorId;
        this.serviceAmount = serviceAmount;
        this.isFree = isFree;
    }



}
