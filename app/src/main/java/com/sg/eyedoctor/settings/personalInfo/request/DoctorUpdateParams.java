package com.sg.eyedoctor.settings.personalInfo.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/18.
 */

public class DoctorUpdateParams extends RequestParams implements Serializable {

    public String espaceNumber;

    public String sex;

    public String espacePass;

    public String picFileName;

    public boolean isAuthenticated;

    public String verifyCode;

    public String id;

    public String loginid;

    public String password;

    public String userName;

    public String tel;

    public int state;

    public int isLock;

    public String createDate;

    public String birthDay;

    public String title;
    public String teachTitle;

    public String speciality;
    public String introduce;

    public String idcard;

    public DoctorUpdateParams() {
        super(ConstantValues.HOST+"/Doctor/DoctorInfoUpdate");
    }



}
