package com.sg.eyedoctor.helpUtils.electronicRecords.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/8/7.
 */
public class CaseDownloadParams extends RequestParams {

    public String eventNo;
    public String patientId;
    public String emrId;
    public String name;

    public CaseDownloadParams() {
        super(ConstantValues.HOST+"/Doctor/GetPubliceContent");
    }
}
