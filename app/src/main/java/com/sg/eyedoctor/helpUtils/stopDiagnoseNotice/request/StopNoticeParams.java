package com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 停诊通知
 */

public class StopNoticeParams extends RequestParams {
    public String doctorId;
    public String doctorName;
    public String stopReason;
    public String startDate;
    public String endDate;
    public String startSpan;
    public String endSpan;
    public String typeList;


    public StopNoticeParams(String doctorId, String doctorName, String stopReason, String startDate, String endDate, String startSpan, String endSpan, String stopTypeList) {
        super(ConstantValues.HOST+"/Share/StopNotice");
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.stopReason = stopReason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startSpan = startSpan;
        this.endSpan = endSpan;
        this.typeList = stopTypeList;
    }

}
