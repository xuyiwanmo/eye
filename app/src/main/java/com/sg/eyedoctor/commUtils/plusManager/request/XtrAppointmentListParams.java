package com.sg.eyedoctor.commUtils.plusManager.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class XtrAppointmentListParams extends RequestParams {
    public String doctorId;

    public String page;
    public String rows;


    public XtrAppointmentListParams(String doctorId, String page, String rows) {
        super(ConstantValues.HOST+"/Share/GetXtrAppointmentList");
        this.doctorId = doctorId;
        this.page = page;
        this.rows = rows;

    }


}
