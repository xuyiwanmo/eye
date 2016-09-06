package com.sg.eyedoctor.consult.textConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ShareDrugParams extends RequestParams {
    public String page;
    public String rows;
    public String fuzzyName;

    public ShareDrugParams(String page, String rows, String fuzzyName) {
        super(ConstantValues.HOST+"/Share/GetShareDrug");
        this.page = page;
        this.rows = rows;
        this.fuzzyName = fuzzyName;
    }
}
