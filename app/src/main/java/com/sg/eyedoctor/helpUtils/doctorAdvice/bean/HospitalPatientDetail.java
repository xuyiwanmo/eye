package com.sg.eyedoctor.helpUtils.doctorAdvice.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/9.
 */

@Table(name = "PatientDetail")
public class HospitalPatientDetail implements Serializable{

    @Column(name="id" ,isId = true)
    public int Id;
    /**
     * adviceId : 1003561473
     * hisHospitalized : 53216
     * deptName : 眼底病专科
     * ward : 40
     * bedNumber : 21
     * type : 2
     * adviceDept :
     * adviceDoctor : 罗向东
     * adviceTime : 2016-05-18T15:11:00
     * adviceName : 测血压
     * originDrug : 0
     * frequency : st
     * drugWay : 0
     * giveDrugWay :
     * dose : 2
     * measureUnit : 次
     * total : 2
     * totalUnit :
     * stopTime : 2016-05-18T15:11:00
     * totalDoctor : 罗向东
     * state : 5
     * proofreadingDoctor : 付玉娟
     * stopDoctor : 罗向东
     * eyes :
     */

    @Column(name="adviceId")
    public String adviceId;
    @Column(name="hisHospitalized")
    public String hisHospitalized;
    @Column(name="deptName")
    public String deptName;
    @Column(name="ward")
    public String ward;
    @Column(name="bedNumber")
    public String bedNumber;
    @Column(name="type")
    public String type;
    @Column(name="adviceDept")
    public String adviceDept;
    @Column(name="adviceDoctor")
    public String adviceDoctor;
    @Column(name="adviceTime")
    public String adviceTime;
    @Column(name="adviceName")
    public String adviceName;
    @Column(name="originDrug")
    public String originDrug;
    @Column(name="frequency")
    public String frequency;
    @Column(name="drugWay")
    public String drugWay;
    @Column(name="giveDrugWay")
    public String giveDrugWay;
    @Column(name="giveDrugWayId")
    public int giveDrugWayId;
    @Column(name="dose")
    public String dose;
    @Column(name="measureUnit")
    public String measureUnit;
    @Column(name="total")
    public String total;
    @Column(name="totalUnit")
    public String totalUnit;
    @Column(name="stopTime")
    public String stopTime;
    @Column(name="totalDoctor")
    public String totalDoctor;
    @Column(name="state")
    public String state;
    @Column(name="proofreadingDoctor")
    public String proofreadingDoctor;
    @Column(name="stopDoctor")
    public String stopDoctor;
    @Column(name="eyes")
    public String eyes;
    @Column(name="price")
    public double price;

    @Column(name="mAdviceType")
    public int mAdviceType;

    /**
     * 医嘱权限 1 长期   2临时  4出院带药
     */
    @Column(name="yzqx")
    public String yzqx;

    @Column(name="checkOrdrugId")
    public String checkOrdrugId;

    /**
     * 瓶  ,支
     */
    @Column(name="pharmacyUnit")
    public String pharmacyUnit;

    @Column(name="firstDayCount")
    public String firstDayCount;
}
