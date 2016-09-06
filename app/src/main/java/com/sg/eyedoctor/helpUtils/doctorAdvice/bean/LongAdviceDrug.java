package com.sg.eyedoctor.helpUtils.doctorAdvice.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/10.
 */
public class LongAdviceDrug implements Serializable {

    /**
     * drugNumber : 200
     * drugName : 破伤风抗毒素注射液
     * drugDose : 1500
     * measureUnit : IU
     * drugStandard : 2ml:1500IU
     * pharmacyUnit : 支
     * pharmacyPackaging : 1
     * inventory : 66
     * healthType : 甲类无
     * price : 11.73
     * pydm : PSFKDSZS
     */

    public String drugNumber;
    public String drugName;
    public String drugDose;
    public String measureUnit;
    public String drugStandard;
    public String pharmacyUnit;
    public String pharmacyPackaging;
    public int inventory;
    public String healthType;
    public double price;
    public String pydm;
}
