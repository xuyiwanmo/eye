package com.sg.eyedoctor.helpUtils.doctorAdvice.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class GetAdviceCheckParams extends RequestParams {
    public String page;
    public String rows;
    public String fuzzyName;

    public GetAdviceCheckParams() {
        super(ConstantValues.HOST+"/Doctor/GetAdviceCheck");
    }

}
