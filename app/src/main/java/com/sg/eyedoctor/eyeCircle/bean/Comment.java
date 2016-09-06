package com.sg.eyedoctor.eyeCircle.bean;

import java.io.Serializable;

/**
 * 评论列表 实例
 */

public class Comment implements Serializable{
    /**
     * id : 5a83025f-5f88-43a1-8af1-1dbebdf1a818
     * topicId : 753e556d-08e0-4bf6-95ec-fe08924f9234
     * doctorId : 27c3dffd-b7cb-440a-accb-dea5136a4875
     * commentType : 2
     * comment : 你好
     * createDate : 2016-03-02 11:02:53
     * reviewerId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * picFileName : \UploadFile\HeadPic/201601261015472038_1.jpg
     * userName : 潘美华
     * reviewerName : 李文生
     */

    public String id;
    public String topicId;
    public String doctorId;
    public int commentType;
    public String commentDetail;
    public String createDate;
    public String reviewerId;
    public String picFileName;
    public String userName;
    public String reviewerName;

    public Comment(String topicId, String doctorId, int commentType, String comment, String reviewerId, String picFileName, String userName, String reviewerName) {
        this.topicId = topicId;
        this.doctorId = doctorId;
        this.commentType = commentType;
        this.commentDetail = comment;
        this.reviewerId = reviewerId;
        this.picFileName = picFileName;
        this.userName = userName;
        this.reviewerName = reviewerName;
    }

    public Comment( String doctorId,String picFileName) {
        this.doctorId = doctorId;
        this.picFileName = picFileName;

    }
    public Comment( String doctorId,String picFileName,String userName) {
        this.doctorId = doctorId;
        this.picFileName = picFileName;
        this.userName = userName;
    }

    public Comment(String topicId, String doctorId, int commentType, String commentDetail, String createDate, String reviewerId, String picFileName, String userName, String reviewerName) {
        this.topicId = topicId;
        this.doctorId = doctorId;
        this.commentType = commentType;
        this.commentDetail = commentDetail;
        this.createDate = createDate;
        this.reviewerId = reviewerId;
        this.picFileName = picFileName;
        this.userName = userName;
        this.reviewerName = reviewerName;
    }
}
