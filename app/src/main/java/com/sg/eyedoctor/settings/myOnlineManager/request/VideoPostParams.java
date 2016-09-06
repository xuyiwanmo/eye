package com.sg.eyedoctor.settings.myOnlineManager.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 医生设置开诊公告
 */
public class VideoPostParams extends RequestParams {
    public String doctorId;
    public String week;
    public String annoucementList;


    public VideoPostParams() {
        super(ConstantValues.HOST+"/Share/VasAnnoucementSet");
    }
}
