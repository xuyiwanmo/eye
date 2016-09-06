package com.sg.eyedoctor.consult.textConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 图文咨询-患者详情
 * 患者中心
 */

public class CounsultPatient implements Parcelable {


    public String city;
    public int assType;
    public String assUserId;
    public int age;
    public String isFocus;
    public String id;
    public String name;
    public String idCard;
    public int sex;
    public String createDate;
    public String grouping;
    public String modifyDate;
    public String birth;
    public String tel;
    public String medicalCard;
    public ArrayList<ConsultationList> consultationList;
    public String clinicId;
    public String illness;
    public String remark;

    public CounsultPatient() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeInt(this.assType);
        dest.writeString(this.assUserId);
        dest.writeInt(this.age);
        dest.writeString(this.isFocus);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.idCard);
        dest.writeInt(this.sex);
        dest.writeString(this.createDate);
        dest.writeString(this.grouping);
        dest.writeString(this.modifyDate);
        dest.writeString(this.birth);
        dest.writeString(this.tel);
        dest.writeString(this.medicalCard);
        dest.writeTypedList(consultationList);
        dest.writeString(this.clinicId);
        dest.writeString(this.illness);
        dest.writeString(this.remark);
    }

    protected CounsultPatient(Parcel in) {
        this.city = in.readString();
        this.assType = in.readInt();
        this.assUserId = in.readString();
        this.age = in.readInt();
        this.isFocus = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.idCard = in.readString();
        this.sex = in.readInt();
        this.createDate = in.readString();
        this.grouping = in.readString();
        this.modifyDate = in.readString();
        this.birth = in.readString();
        this.tel = in.readString();
        this.medicalCard = in.readString();
        this.consultationList = in.createTypedArrayList(ConsultationList.CREATOR);
        this.clinicId = in.readString();
        this.illness = in.readString();
        this.remark = in.readString();
    }

    public static final Creator<CounsultPatient> CREATOR = new Creator<CounsultPatient>() {
        @Override
        public CounsultPatient createFromParcel(Parcel source) {
            return new CounsultPatient(source);
        }

        @Override
        public CounsultPatient[] newArray(int size) {
            return new CounsultPatient[size];
        }
    };
}
