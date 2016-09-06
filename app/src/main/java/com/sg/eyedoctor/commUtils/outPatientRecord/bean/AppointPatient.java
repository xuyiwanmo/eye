package com.sg.eyedoctor.commUtils.outPatientRecord.bean;

import java.io.Serializable;

/**
 * Created by zhanghua on 2016/1/14.
 */
public class AppointPatient implements Serializable {
    /**
     * apmDate : 2016-01-13 08:50:00
     * patientName : 张力
     * sex : 1
     * birthday : 1988-06-02
     * apmtype : app
     * createDate : 2016-01-11 00:00:00
     * status : 1
     * signalSource : 0
     * isCome : 0
     * docName : 谢广斌
     * docCode : 1001
     * deptCode : 99
     * deptName : 白内障
     */
    public int patientId;
    public String apmDate;
    public String patientName;
    public String idCard;
    public String sex;
    public String birthday;
    public String apmtype;
    public String createDate;
    public int status;
    public int signalSource;
    public int isCome;
    public String docName;
    public String docCode;
    public String deptCode;
    public String deptName;
    public String medicalCard;


}
