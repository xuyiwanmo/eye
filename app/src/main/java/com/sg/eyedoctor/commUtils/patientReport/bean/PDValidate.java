package com.sg.eyedoctor.commUtils.patientReport.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/11.
 */
public class PDValidate implements Parcelable{


    public String id;
    public String patientId;
    public String doctorId;
    public String validateState;
    public String createDate;
    public String name;
    public String idCard;
    public String sex;
    public String age;
    public String phone;
    public String validateId;
    public String treatmentDate;
    public String diagnosisResult;
    public String latestDate;
    public boolean isChecked=false;
    /**
     * userId : 10003
     * friendId : ed694098-618f-4e5e-98c2-8dafb826b6ab
     * picfileName : ~\File//1//201604011802430601.jpg
     */

    public String userId;
    public String friendId;
    public String picfileName;
    /**
     * illness : 朋友
     * patientIM : p13559243607
     */

    public String illness;
    public String patientIM;

    public PDValidate() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.patientId);
        dest.writeString(this.doctorId);
        dest.writeString(this.validateState);
        dest.writeString(this.createDate);
        dest.writeString(this.name);
        dest.writeString(this.idCard);
        dest.writeString(this.sex);
        dest.writeString(this.age);
        dest.writeString(this.phone);
        dest.writeString(this.validateId);
        dest.writeString(this.treatmentDate);
        dest.writeString(this.diagnosisResult);
        dest.writeString(this.latestDate);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.userId);
        dest.writeString(this.friendId);
        dest.writeString(this.picfileName);
        dest.writeString(this.illness);
        dest.writeString(this.patientIM);
    }

    protected PDValidate(Parcel in) {
        this.id = in.readString();
        this.patientId = in.readString();
        this.doctorId = in.readString();
        this.validateState = in.readString();
        this.createDate = in.readString();
        this.name = in.readString();
        this.idCard = in.readString();
        this.sex = in.readString();
        this.age = in.readString();
        this.phone = in.readString();
        this.validateId = in.readString();
        this.treatmentDate = in.readString();
        this.diagnosisResult = in.readString();
        this.latestDate = in.readString();
        this.isChecked = in.readByte() != 0;
        this.userId = in.readString();
        this.friendId = in.readString();
        this.picfileName = in.readString();
        this.illness = in.readString();
        this.patientIM = in.readString();
    }

    public static final Creator<PDValidate> CREATOR = new Creator<PDValidate>() {
        @Override
        public PDValidate createFromParcel(Parcel source) {
            return new PDValidate(source);
        }

        @Override
        public PDValidate[] newArray(int size) {
            return new PDValidate[size];
        }
    };
}
