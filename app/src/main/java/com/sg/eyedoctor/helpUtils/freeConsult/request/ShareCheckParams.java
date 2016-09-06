package com.sg.eyedoctor.helpUtils.freeConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ShareCheckParams extends RequestParams {
    public String page;
    public String rows;
    public String fuzzyName;

    public ShareCheckParams(String page, String rows, String fuzzyName) {
        super(ConstantValues.HOST+"/Share/GetShareCheck");
        this.page = page;
        this.rows = rows;
        this.fuzzyName = fuzzyName;
    }
}
