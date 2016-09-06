package com.sg.eyedoctor.commUtils.internetConsult.bean;

import com.sg.eyedoctor.eyeCircle.bean.Comment;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/2.
 */
public class TopicCaseDetail {
    /**
     * id : 99c9494c-788c-405b-aff4-37f85afb91c1
     * doctorId : 10001
     * title : 你好
     * detail : 在
     * patientName : 林奕
     * sex : 1
     * age : 33
     * illness : Sfs
     * illDetail : 屈光不正
     * createDate : 2016-06-27 14:04:18
     * userName : 李文生
     * picFileName : \UploadFile\HeadPic/201606221545450727_1.jpg
     * licenseHospital : 厦门大学附属厦门眼科中心
     * licenseTitle : 助理医师
     * licenseDept : 青光眼
     * picList : [{"id":"88c547be-a51b-4d85-b08a-8788c1078bd1","consulId":"99c9494c-788c-405b-aff4-37f85afb91c1","picUrl":"\\UploadFile\\CirclePicture/201606271404187932_1.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201606271404187932_1_m.jpg","createDate":"2016-06-27 14:04:18"}]
     * collectCount : 1
     * isCollect : true
     */

    public String id;
    public String doctorId;
    public String title;
    public String detail;
    public String patientName;
    public String sex;
    public String age;
    public String illness;
    public String illDetail;
    public String createDate;
    public String userName;
    public String picFileName;
    public String licenseHospital;
    public String licenseTitle;
    public String licenseDept;
    public int collectCount;
    public String isCollect;

    public ArrayList<Comment> commentList;
    public ArrayList<Picture> picList;

    public int commentCount;
    public int upvoteCount;

    @Override
    public String toString() {
        return "TopicCaseDetail{" +
                "id='" + id + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", patientName='" + patientName + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", illness='" + illness + '\'' +
                ", illDetail='" + illDetail + '\'' +
                ", createDate='" + createDate + '\'' +
                ", userName='" + userName + '\'' +
                ", picFileName='" + picFileName + '\'' +
                ", licenseHospital='" + licenseHospital + '\'' +
                ", licenseTitle='" + licenseTitle + '\'' +
                ", licenseDept='" + licenseDept + '\'' +
                ", collectCount=" + collectCount +
                ", isCollect='" + isCollect + '\'' +
                ", commentList=" + commentList +
                ", picList=" + picList +
                ", commentCount=" + commentCount +
                ", upvoteCount=" + upvoteCount +
                '}';
    }
}
