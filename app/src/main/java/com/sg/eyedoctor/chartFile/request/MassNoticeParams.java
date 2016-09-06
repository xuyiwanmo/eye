package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class MassNoticeParams extends RequestParams {
    public String messageTitle;
    public String messageDetail;
    public String publisherId;
    public String receiverList;
    public String publisherName;

    public MassNoticeParams(String messageTitle, String messageDetail, String publisherId, String receiverList,String publisherName) {
        super(ConstantValues.HOST + "/Share/MassNotice");
        this.messageTitle = messageTitle;
        this.messageDetail = messageDetail;
        this.publisherId = publisherId;
        this.receiverList = receiverList;
        this.publisherName=publisherName;
    }

    @Override
    public String toString() {
        return "MassNoticeParams{" +
                "messageTitle='" + messageTitle + '\'' +
                ", messageDetail='" + messageDetail + '\'' +
                ", publisherId='" + publisherId + '\'' +
                ", receiverList='" + receiverList + '\'' +
                ", publisherName='" + publisherName + '\'' +
                '}';
    }
}
