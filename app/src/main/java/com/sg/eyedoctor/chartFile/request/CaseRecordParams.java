package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */
public class CaseRecordParams extends RequestParams {
    public String patientId;

    public CaseRecordParams() {
        super(ConstantValues.HOST+"/Doctor/GetMedicalRecord");
    }

}
