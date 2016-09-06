package com.sg.eyedoctor.eyeCircle.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * Created by zhanghua on 2016/1/13.
 */

public class CommentAddParams extends RequestParams implements Serializable {
    public String topicId;
    public String doctorId;
    public String commentType;
    public String commentDetail;
    public String reviewerId;

    public CommentAddParams(String topicId, String doctorId, String commentType, String comment, String reviewerId) {
        super(ConstantValues.HOST+"/Circle/CommentAdd");
        this.topicId = topicId;
        this.doctorId = doctorId;
        this.commentType = commentType;
        this.commentDetail = comment;
        this.reviewerId = reviewerId;
    }


}
