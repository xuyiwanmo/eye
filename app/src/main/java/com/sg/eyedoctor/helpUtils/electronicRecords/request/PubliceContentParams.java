package com.sg.eyedoctor.helpUtils.electronicRecords.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/8/7.
 */
public class PubliceContentParams extends RequestParams {
    public String patientId;
    public String emrId;
    public String eventNo;
    public String name;

    public PubliceContentParams() {
        super(ConstantValues.HOST+"/Doctor/GetPubliceContent");
    }
}
