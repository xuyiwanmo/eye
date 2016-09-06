package com.sg.eyedoctor.helpUtils.freeClinic.bean;

/**
 * 义诊批量新增
 */
public class PostClinicList {

    public String openDate;
    public int amount;
    public int type;//服务类型(1：图文咨询|2：电话咨询)

    public PostClinicList(String openDate, int count, int type) {
        this.openDate = openDate;
        this.amount = count;
        this.type = type;
    }
}
