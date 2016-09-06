package com.sg.eyedoctor.helpUtils.doctorAdvice.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */


public class AddAdviceParams extends RequestParams {

    /**
     * HIS住院号(zyh)
     */
    public String hisHospitalized;
    /**
     * 项目类别 1 药品 2 诊疗
     */
    public String type;
    /**
     * 项目Id  检查是VEMR_ORDER_ITEM的catalogCode  药品是 drugNumber号
     */
    public String id;
    /**
     * 数量
     */
    public String amount;
    /**
     * 医嘱权限 1 长期   2临时  4出院带药
     */
    public String yzqx;
    /**
     *使用频次 临嘱为st
     */
    public String frequency;
    /**
     * 给药途径
     */
    public String giveDrugWay;
    /**
     * 眼别 左、右、双
     */
    public String eyes;

    public AddAdviceParams() {
        super(ConstantValues.HOST+"/Doctor/AddAdvice");
    }

    @Override
    public String toString() {
        return "AddAdviceParams{" +
                "hisHospitalized='" + hisHospitalized + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", yzqx='" + yzqx + '\'' +
                ", frequency='" + frequency + '\'' +
                ", giveDrugWay='" + giveDrugWay + '\'' +
                ", eyes='" + eyes + '\'' +
                '}';
    }
}
