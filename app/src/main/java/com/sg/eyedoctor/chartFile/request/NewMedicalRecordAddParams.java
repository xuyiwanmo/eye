package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/6/22.
 */
public class NewMedicalRecordAddParams extends RequestParams {

    public String doctorId;
    public String patientId;
    public String result;
    public String description;
    public String drugList;
    public String checkList;
    public String picList;

    public NewMedicalRecordAddParams(String doctorId, String patientId, String result, String description, String drugList, String checkList, String picList) {
        super(ConstantValues.HOST+"/Doctor/NewMedicalRecordAdd");
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.result = result;
        this.description = description;
        this.drugList = drugList;
        this.checkList = checkList;
        this.picList = picList;
    }
}
