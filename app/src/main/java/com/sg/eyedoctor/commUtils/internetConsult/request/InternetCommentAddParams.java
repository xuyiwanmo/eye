package com.sg.eyedoctor.commUtils.internetConsult.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * Created by zhanghua on 2016/1/13.
 */

//@HttpRequest(
//        host = ConstantValues.HOST,
//        path = "Doctor/CommentAdd",
//        builder = DefaultParamsBuilder.class
//)
public class InternetCommentAddParams extends RequestParams implements Serializable {
    public String consulId;
    public String doctorId;
    public String commentType;
    public String commentDetail;
    public String reviewerId;

    public InternetCommentAddParams(String topicId, String doctorId, String commentType, String comment, String reviewerId) {
        super(ConstantValues.HOST+"/Doctor/CommentAdd");
        this.consulId = topicId;
        this.doctorId = doctorId;
        this.commentType = commentType;
        this.commentDetail = comment;
        this.reviewerId = reviewerId;
    }

    @Override
    public String toString() {
        return "CommentAddParams{" +
                "consulId='" + consulId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", commentType='" + commentType + '\'' +
                ", commentDetail='" + commentDetail + '\'' +
                ", reviewerId='" + reviewerId + '\'' +
                '}';
    }

}
