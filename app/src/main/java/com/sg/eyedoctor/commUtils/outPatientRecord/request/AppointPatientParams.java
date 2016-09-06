package com.sg.eyedoctor.commUtils.outPatientRecord.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * Created by zhanghua on 2016/1/13.
 */

public class AppointPatientParams extends RequestParams implements Serializable {
    public String docCode;
    public String queryTpye;
    public String queryDate;
    public String type;

    public AppointPatientParams() {
        super(ConstantValues.HOST+"/Linet/PatientAppointment");
    }

}
