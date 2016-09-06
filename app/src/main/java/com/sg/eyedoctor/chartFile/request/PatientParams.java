package com.sg.eyedoctor.chartFile.request;

import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * Created by zhanghua on 2016/1/9.
 */

public class PatientParams extends RequestParams implements Serializable{

    public String id;
    public String name;
    public String idCard;
    public String medicalCard;
    public int sex;
    public String birth;
    public int age;
    public String tel;
    public String city;
    public String illness;
    public String caseNo;
    public String remark;
    public String clinicId;//门诊号码


    public PatientParams(String url) {
        super(url);
    }


}
