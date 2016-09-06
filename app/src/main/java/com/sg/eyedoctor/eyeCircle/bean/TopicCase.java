package com.sg.eyedoctor.eyeCircle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sg.eyedoctor.lookPicture.bean.Picture;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/3.
 */
public class TopicCase implements Parcelable {

    /**
     * topicId : f0ef1925-eefb-43c0-91e9-9efda23b3d88
     * doctorId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * title : 疾病
     * detail : 疾病
     * type : 1
     * createDate : 2016-03-03 11:00:47
     * userName : 李文生
     * picFileName : \UploadFile\HeadPic/201602051120012297_1.jpg
     * licenseHospital : 厦门大学附属厦门眼科中心
     * licenseTitle : 助理医师
     * licenseDept : 视光科
     * picList : [{"id":"a0092131-973c-4b04-8910-72e6c9322c1f","topicId":"f0ef1925-eefb-43c0-91e9-9efda23b3d88","picUrl":"\\UploadFile\\CirclePicture/201603031100466492_1.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603031100466648_1_m.jpg","type":1,"createDate":"2016-03-03 11:00:47"},{"id":"f38ea029-ca51-46c7-bddb-5cecd3c9a07f","topicId":"f0ef1925-eefb-43c0-91e9-9efda23b3d88","picUrl":"\\UploadFile\\CirclePicture/201603031100473044_2.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603031100473044_2_m.jpg","type":1,"createDate":"2016-03-03 11:00:47"}]
     * commentCount : 0
     * upvoteCount : 38
     */

    public String topicId;
    public String doctorId;
    public String title;
    public String detail;
    public int type;
    public String createDate;
    public String userName;
    public String picFileName;
    public String licenseHospital;
    public String licenseTitle;
    public String licenseDept;
    public int commentCount;
    public int upvoteCount;
    public String isCollect;
    public int collectCount;


    public ArrayList<Picture> picList=new ArrayList<>();
    public ArrayList<Comment> commentList=new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topicId);
        dest.writeString(this.doctorId);
        dest.writeString(this.title);
        dest.writeString(this.detail);
        dest.writeInt(this.type);
        dest.writeString(this.createDate);
        dest.writeString(this.userName);
        dest.writeString(this.picFileName);
        dest.writeString(this.licenseHospital);
        dest.writeString(this.licenseTitle);
        dest.writeString(this.licenseDept);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.upvoteCount);
        dest.writeString(this.isCollect);
        dest.writeInt(this.collectCount);
        dest.writeTypedList(picList);
    }

    public TopicCase() {
    }

    protected TopicCase(Parcel in) {
        this.topicId = in.readString();
        this.doctorId = in.readString();
        this.title = in.readString();
        this.detail = in.readString();
        this.type = in.readInt();
        this.createDate = in.readString();
        this.userName = in.readString();
        this.picFileName = in.readString();
        this.licenseHospital = in.readString();
        this.licenseTitle = in.readString();
        this.licenseDept = in.readString();
        this.commentCount = in.readInt();
        this.upvoteCount = in.readInt();
        this.isCollect = in.readString();
        this.collectCount = in.readInt();
        this.picList = in.createTypedArrayList(Picture.CREATOR);
    }

    public static final Parcelable.Creator<TopicCase> CREATOR = new Parcelable.Creator<TopicCase>() {
        @Override
        public TopicCase createFromParcel(Parcel source) {
            return new TopicCase(source);
        }

        @Override
        public TopicCase[] newArray(int size) {
            return new TopicCase[size];
        }
    };
}
