package com.sg.eyedoctor.commUtils.plusManager.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class XtrAppointmentUpdateParams extends RequestParams {
    public String Id;
    public String timeType;
    public String beginSpan;
    public String endSpan;
    public String xtrAccount;
    public String xtrHospital;
    public String HospitalId;
    public String xtrType;
    public String xtrPrice;
    public String remark;

    public XtrAppointmentUpdateParams(
            String id, String beginSpan, String endSpan, String xtrAccount, String xtrHospital, String hosId,String xtrType, String xtrPrice, String remark) {
        super(ConstantValues.HOST+"/Share/XtrAppointmentUpdate");
        Id = id;

        this.beginSpan = beginSpan;
        this.endSpan = endSpan;
        this.xtrAccount = xtrAccount;
        this.xtrHospital = xtrHospital;
        this.xtrType = xtrType;
        this.xtrPrice = xtrPrice;
        this.remark = remark;
        this.HospitalId=hosId;
    }


}
