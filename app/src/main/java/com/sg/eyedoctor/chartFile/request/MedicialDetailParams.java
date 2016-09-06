package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 病例详情
 */

public class MedicialDetailParams extends RequestParams {
    public String OutpatientCard;

    public MedicialDetailParams() {
        super(ConstantValues.HOST+"/Doctor/GetMedicalDetails");
    }
}
