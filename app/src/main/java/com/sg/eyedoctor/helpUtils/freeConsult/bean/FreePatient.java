package com.sg.eyedoctor.helpUtils.freeConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sg.eyedoctor.lookPicture.bean.Picture;

import java.util.ArrayList;

/**
 * Created by zhanghua on 2015/12/31.
 */
public class FreePatient implements Parcelable {

    public String modifyDate;
    public String picFileName;

    public FreePatient(String name, String sex, String age, String questionDetail, ArrayList<Picture> picList) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.questionDetail = questionDetail;
        this.picList = picList;
    }

    /**
     * questionId : ce1b5b91-9d05-4ef7-84fd-f57e32c2315a
     * patientId : 90cc06db-105f-407c-a4f9-c5593e1ad239
     * doctorId : 10003
     * name : 小小
     * sex : 0
     * age : 28
     * patientIM : p13559243607
     * questionDetail : 医生，请问一下，我太美了怎么办？
     * state : 1
     * picList : [{"id":"6b5b8d35-f092-48a5-95c7-e53a2734184a","questionId":"ce1b5b91-9d05-4ef7-84fd-f57e32c2315a","picUrl":"/UploadFile/CirclePicture/201606160931559112_1.jpg","microPicUrl":"/UploadFile/CirclePicture/201606160931559112_1_m.jpg","createDate":"2016-06-16 09:31:56"}]
     * createDate : 2016-06-16 09:31:56
     * newMessage : 0
     * isVideo 1
     */

    public String questionId;
    public String patientId;
    public String doctorId;
    public String name;
    public String sex;
    public String age;
    public String patientIM;
    public String questionDetail;
    public String state;
    public String createDate;
    public int newMessage;
    public int isVideo;
    /**
    /**
     * id : 6b5b8d35-f092-48a5-95c7-e53a2734184a
     * questionId : ce1b5b91-9d05-4ef7-84fd-f57e32c2315a
     * picUrl : /UploadFile/CirclePicture/201606160931559112_1.jpg
     * microPicUrl : /UploadFile/CirclePicture/201606160931559112_1_m.jpg
     * createDate : 2016-06-16 09:31:56
     */

    public ArrayList<Picture> picList;

    public FreePatient() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.modifyDate);
        dest.writeString(this.picFileName);
        dest.writeString(this.questionId);
        dest.writeString(this.patientId);
        dest.writeString(this.doctorId);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.age);
        dest.writeString(this.patientIM);
        dest.writeString(this.questionDetail);
        dest.writeString(this.state);
        dest.writeString(this.createDate);
        dest.writeInt(this.newMessage);
        dest.writeTypedList(picList);
    }

    protected FreePatient(Parcel in) {
        this.modifyDate = in.readString();
        this.picFileName = in.readString();
        this.questionId = in.readString();
        this.patientId = in.readString();
        this.doctorId = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.age = in.readString();
        this.patientIM = in.readString();
        this.questionDetail = in.readString();
        this.state = in.readString();
        this.createDate = in.readString();
        this.newMessage = in.readInt();
        this.picList = in.createTypedArrayList(Picture.CREATOR);
    }

    public static final Creator<FreePatient> CREATOR = new Creator<FreePatient>() {
        @Override
        public FreePatient createFromParcel(Parcel source) {
            return new FreePatient(source);
        }

        @Override
        public FreePatient[] newArray(int size) {
            return new FreePatient[size];
        }
    };
}
