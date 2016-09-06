package com.sg.eyedoctor.helpUtils.freeConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 图文咨询- 服务价格设置
 */

public class VasPriceSetParams extends RequestParams {
    public String doctorId;
    public String price;
    public String type;

    public VasPriceSetParams(String doctorId, String price, String type) {
        super(ConstantValues.HOST+"/Share/VasPriceSet");
        this.doctorId = doctorId;
        this.price = price;
        this.type = type;
    }



}
