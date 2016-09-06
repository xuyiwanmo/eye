package com.sg.eyedoctor.consult.textConsult.request;

/**
 * Created by Administrator on 2016/4/25.
 */
public class DrugBean {
    public String drugId;
    public String drugName;
    public String drugUnit;
    public String drugCount;

    public DrugBean(String drugId, String drugName, String drugUnit, String drugCount) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.drugUnit = drugUnit;
        this.drugCount = drugCount;
    }
}
