package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 患者移动分组
 */
public class PatientGroupMoveParams extends RequestParams {
    public String patientId;
    public String groupId;


    public PatientGroupMoveParams(String patientId, String groupId) {
        super(ConstantValues.HOST+"/Doctor/PatientGroupMove");
        this.patientId = patientId;
        this.groupId = groupId;
    }
}
