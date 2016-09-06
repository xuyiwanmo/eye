package com.sg.eyedoctor.helpUtils.electronicRecords.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/8/7.
 */
public class NowPubliceEventParams extends RequestParams {
    public String pageNumber;
    public String pageSize;
    public String doctorId;

    public NowPubliceEventParams() {
        super(ConstantValues.HOST+"/Doctor/GetNowPubliceEvent");
    }
}
