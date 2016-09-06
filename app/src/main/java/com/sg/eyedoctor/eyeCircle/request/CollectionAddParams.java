package com.sg.eyedoctor.eyeCircle.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 医生收藏文章
 */

public class CollectionAddParams extends RequestParams {
    public String sourceID;
    public String collector;
    public String sourceType;
    public String title;
    public String picUrl;
    public String webUrl;

    public CollectionAddParams(String sourceID, String collector, String sourceType, String title, String picUrl, String webUrl) {
        super(ConstantValues.HOST+"/Share/CollectionAdd");
        this.sourceID = sourceID;
        this.collector = collector;
        this.sourceType = sourceType;
        this.title = title;
        this.picUrl = picUrl;
        this.webUrl = webUrl;
    }
}
