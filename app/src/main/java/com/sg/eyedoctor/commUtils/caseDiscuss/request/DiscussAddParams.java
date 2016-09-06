package com.sg.eyedoctor.commUtils.caseDiscuss.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class DiscussAddParams extends RequestParams {
    public String patientName;
    public String sex;
    public String age;
    public String illness;
    public String diagnosisResult;
    public String memberList;
    public String teamId;
    public String doctorId;
    public String picList;

    public DiscussAddParams() {
        super(ConstantValues.HOST+"/Doctor/DiscussionAdd");
    }


}
