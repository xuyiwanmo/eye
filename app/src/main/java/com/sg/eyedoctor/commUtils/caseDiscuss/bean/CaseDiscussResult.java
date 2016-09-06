package com.sg.eyedoctor.commUtils.caseDiscuss.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CaseDiscussResult implements Parcelable {

    /**
     * disId : 2bab8142-198f-426d-be4e-aed5fa1f07c3
     * doctorId : 10002
     * patientName : 哈哈哈
     * sex : 1
     * age : 12
     * illness : 55
     * diagnosisResult : 58
     * teamId : 7491739
     * picList : [{"microPicUrl":"\\File\\UploadFile\\CirclePicture/201607131607378716_1_m.jpg","picUrl":"\\File\\UploadFile\\CirclePicture/201607131607378716_1.jpg"},{"microPicUrl":"\\File\\UploadFile\\CirclePicture/201607131607384800_2_m.jpg","picUrl":"\\File\\UploadFile\\CirclePicture/201607131607384800_2.jpg"},{"microPicUrl":"\\File\\UploadFile\\CirclePicture/201607131607384956_3_m.jpg","picUrl":"\\File\\UploadFile\\CirclePicture/201607131607384956_3.jpg"}]
     */
    public String disId;
    public String doctorId;
    public String patientName;
    public String sex;
    public int age;
    public String illness;
    public String diagnosisResult;
    public String teamId;
    public String picList;

    public CaseDiscussResult() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.disId);
        dest.writeString(this.doctorId);
        dest.writeString(this.patientName);
        dest.writeString(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.illness);
        dest.writeString(this.diagnosisResult);
        dest.writeString(this.teamId);
        dest.writeString(this.picList);
    }

    protected CaseDiscussResult(Parcel in) {
        this.disId = in.readString();
        this.doctorId = in.readString();
        this.patientName = in.readString();
        this.sex = in.readString();
        this.age = in.readInt();
        this.illness = in.readString();
        this.diagnosisResult = in.readString();
        this.teamId = in.readString();
        this.picList = in.readString();
    }

    public static final Creator<CaseDiscussResult> CREATOR = new Creator<CaseDiscussResult>() {
        @Override
        public CaseDiscussResult createFromParcel(Parcel source) {
            return new CaseDiscussResult(source);
        }

        @Override
        public CaseDiscussResult[] newArray(int size) {
            return new CaseDiscussResult[size];
        }
    };
}
