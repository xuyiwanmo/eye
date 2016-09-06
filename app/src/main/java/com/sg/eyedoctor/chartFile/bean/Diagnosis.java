package com.sg.eyedoctor.chartFile.bean;

/**
 * Created by zhanghua on 2016/1/14.
 */
public class Diagnosis {
    public String hospitalId;
    public int brid;
    public String name;
    public String medicalTime;
    public String costName;
    public int costPrice;
    public int costAmount;
    public int costTotal;
    public String healthType;

    public Diagnosis(String costName) {
        this.costName = costName;
    }
}
