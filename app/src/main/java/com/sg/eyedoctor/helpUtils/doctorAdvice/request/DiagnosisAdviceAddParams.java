package com.sg.eyedoctor.helpUtils.doctorAdvice.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/4/25.
 */
public class DiagnosisAdviceAddParams extends RequestParams {
    public String orderId;
    public String doctorId;
    public String patientId;
    public String result;
    public String advice;
    public String drugList;
    public String checkList;

    public DiagnosisAdviceAddParams(String orderId, String doctorId, String patientId, String result, String advice,String drugList, String checkList) {

        super(ConstantValues.HOST+"/Share/DiagnosisAdviceAdd");
        this.orderId = orderId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.result = result;
        this.advice = advice;
        this.drugList = drugList;
        this.checkList = checkList;
    }
}
