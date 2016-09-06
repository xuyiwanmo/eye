package com.sg.eyedoctor.consult.textConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class VasDutyRosterSetParams extends RequestParams {
    public String doctorId;
    public String week;
    public String dutyList;


    public VasDutyRosterSetParams(String doctorId, String week, String dutyList) {
        super(ConstantValues.HOST+"/Share/VasDutyRosterSet");
        this.doctorId = doctorId;
        this.week = week;
        this.dutyList = dutyList;
    }
}
