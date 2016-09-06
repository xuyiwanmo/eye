package com.sg.eyedoctor.helpUtils.doctorAdvice.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class GetAdviceParams extends RequestParams {
    public String page;
    public String rows;
    public String hisHospitalized;

    public GetAdviceParams() {
        super(ConstantValues.HOST+"/Doctor/GetAdvice");
    }

}
