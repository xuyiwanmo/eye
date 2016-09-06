package com.sg.eyedoctor.commUtils.internetConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/13.
 */


public class NetConsultationAddParams extends RequestParams{
    public String title;
    public String detail;
    public String doctorId;
    public String patientName;
    public String sex;
    public String age;
    public String illness;
    public String picList;
    public String illDetail;

    public NetConsultationAddParams() {
        super(ConstantValues.HOST+"/Doctor/NetConsultationAdd");
    }

}
