package com.sg.eyedoctor.helpUtils.doctorAdvice.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/9.
 */
public class HospitalPatient implements Serializable {


    /**
     * patientName : 颜美花
     * patientSex : 女
     * patientBirt : 1962-04-17T00:00:00
     * patientType : 1
     * hisHospitalized : 52918
     * hospitalized : 195532
     * ward : 37
     * admissionDate : 2016-05-16T11:03:00
     * patientDept : 1033
     * chargeType : 1
     * chargeName : 自费
     * moneyPay : 5000
     * moneyCost : 0
     * moneyLeft : 0
     */

    public String patientName;
    public String patientSex;
    public String patientBirt;
    public String bedNumber;
    public String patientType;
    public String hisHospitalized;
    public String hospitalized;
    public String attDoctor;
    public String ward;
    public String admissionDate;
    public String dischargeDate;
    public String patientDept;
    public String residents;
    public String injectionDoctor;
    public String chargeType;
    public String chargeName;

    public int moneyPay;
    public int moneyCost;
    public int moneyLeft;
}
