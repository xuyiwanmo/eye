package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */
//@HttpRequest(
//        host = ConstantValues.HOST,
//        path = "Doctor/GetPatientByGroup",
//        builder = DefaultParamsBuilder.class
//)
public class PatientByGroupParams extends RequestParams {
    public String groupId;


    public PatientByGroupParams() {
        super(ConstantValues.HOST+"/Doctor/GetPatientByGroup");
    }

}
