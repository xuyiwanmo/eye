package com.sg.eyedoctor.eyeCircle.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 医生收藏文章
 */

public class TopicReleaseParams extends RequestParams {
    public String title;
    public String detail;
    public String type;
    public String picList;
    public String doctorId;

    public TopicReleaseParams(String title, String detail, String type, String picList) {
        super(ConstantValues.HOST+"/Circle/TopicRelease");
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.picList = picList;
    }
}
