package com.sg.eyedoctor.commUtils.outPatientRecord.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class DeletePatientParams extends RequestParams {
    public String id;

    public DeletePatientParams() {
        super(ConstantValues.HOST+"/Doctor/PatientDelete");
    }
}
