package com.sg.eyedoctor.commUtils.internetConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sg.eyedoctor.eyeCircle.bean.Comment;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/6.
 */
public class InternetConsult implements Parcelable {


    /**
     * id : 0e9071c6-ccf0-4061-8830-1f278588083c
     * doctorId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * title : 33
     * detail : dd
     * patientName : 12
     * sex : 0
     * age : 12
     * illness : 22
     * illDetail : 33
     * createDate : 2016-04-06 17:44:04
     * userName : 李文生
     * picFileName : \UploadFile\HeadPic/201603051721153792_1.jpg
     * licenseHospital : 青光眼
     * licenseTitle : 中级
     * licenseDept : 助理医师
     * picList : [{"id":"95c1a4c5-c8ca-4aee-bd6e-35c3672e4229","consulId":"0e9071c6-ccf0-4061-8830-1f278588083c","picUrl":"\\UploadFile\\CirclePicture/201604061744033736_1.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201604061744033736_1_m.jpg","createDate":"2016-04-06 17:44:04"},{"id":"a2799fad-ea3c-496b-9170-5f6b5f7c5372","consulId":"0e9071c6-ccf0-4061-8830-1f278588083c","picUrl":"\\UploadFile\\CirclePicture/201604061744034672_2.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201604061744034672_2_m.jpg","createDate":"2016-04-06 17:44:04"}]
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
    public int upvoteCount;
    public int commentCount;
    public String isCollect;
    /**
     * id : 95c1a4c5-c8ca-4aee-bd6e-35c3672e4229
     * consulId : 0e9071c6-ccf0-4061-8830-1f278588083c
     * picUrl : \UploadFile\CirclePicture/201604061744033736_1.jpg
     * microPicUrl : \UploadFile\CirclePicture/201604061744033736_1_m.jpg
     * createDate : 2016-04-06 17:44:04
     */

    public ArrayList<Picture> picList=new ArrayList<>();
    public ArrayList<Comment> commentList=new ArrayList<>();


    public InternetConsult() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.doctorId);
        dest.writeString(this.title);
        dest.writeString(this.detail);
        dest.writeString(this.patientName);
        dest.writeString(this.sex);
        dest.writeString(this.age);
        dest.writeString(this.illness);
        dest.writeString(this.illDetail);
        dest.writeString(this.createDate);
        dest.writeString(this.userName);
        dest.writeString(this.picFileName);
        dest.writeString(this.licenseHospital);
        dest.writeString(this.licenseTitle);
        dest.writeString(this.licenseDept);
        dest.writeInt(this.collectCount);
        dest.writeInt(this.upvoteCount);
        dest.writeInt(this.commentCount);
        dest.writeString(this.isCollect);
        dest.writeTypedList(picList);
        dest.writeList(this.commentList);
    }

    protected InternetConsult(Parcel in) {
        this.id = in.readString();
        this.doctorId = in.readString();
        this.title = in.readString();
        this.detail = in.readString();
        this.patientName = in.readString();
        this.sex = in.readString();
        this.age = in.readString();
        this.illness = in.readString();
        this.illDetail = in.readString();
        this.createDate = in.readString();
        this.userName = in.readString();
        this.picFileName = in.readString();
        this.licenseHospital = in.readString();
        this.licenseTitle = in.readString();
        this.licenseDept = in.readString();
        this.collectCount = in.readInt();
        this.upvoteCount = in.readInt();
        this.commentCount = in.readInt();
        this.isCollect = in.readString();
        this.picList = in.createTypedArrayList(Picture.CREATOR);
        this.commentList = new ArrayList<Comment>();
        in.readList(this.commentList, Comment.class.getClassLoader());
    }

    public static final Creator<InternetConsult> CREATOR = new Creator<InternetConsult>() {
        @Override
        public InternetConsult createFromParcel(Parcel source) {
            return new InternetConsult(source);
        }

        @Override
        public InternetConsult[] newArray(int size) {
            return new InternetConsult[size];
        }
    };
}
