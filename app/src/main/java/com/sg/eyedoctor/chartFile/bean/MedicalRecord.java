package com.sg.eyedoctor.chartFile.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sg.eyedoctor.common.bean.PicBean;

import java.util.ArrayList;

/**
 * Created by zhanghua on 2016/1/14.
 */
public class MedicalRecord implements Parcelable {

    public String hospitalId;
    public String medicalTime;
    public String brid;
    public String name;
    public String medicalCard;
    public String idCard;
    public String patientType;
    public String diagnosis;
    public String illness;
    public String outpatientCard;
    public int sex;
    public String remark;
    public int age;
    public ArrayList<PicBean> picList;


    public MedicalRecord() {

    }

    public MedicalRecord(String name, String outpatientCard, String diagnosis) {
        this.name = name;
        this.outpatientCard = outpatientCard;
        this.diagnosis = diagnosis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hospitalId);
        dest.writeString(this.medicalTime);
        dest.writeString(this.brid);
        dest.writeString(this.name);
        dest.writeString(this.medicalCard);
        dest.writeString(this.idCard);
        dest.writeString(this.patientType);
        dest.writeString(this.diagnosis);
        dest.writeString(this.illness);
        dest.writeString(this.outpatientCard);
        dest.writeInt(this.sex);
        dest.writeString(this.remark);
        dest.writeInt(this.age);
        dest.writeList(this.picList);
    }

    protected MedicalRecord(Parcel in) {
        this.hospitalId = in.readString();
        this.medicalTime = in.readString();
        this.brid = in.readString();
        this.name = in.readString();
        this.medicalCard = in.readString();
        this.idCard = in.readString();
        this.patientType = in.readString();
        this.diagnosis = in.readString();
        this.illness = in.readString();
        this.outpatientCard = in.readString();
        this.sex = in.readInt();
        this.remark = in.readString();
        this.age = in.readInt();
        this.picList = new ArrayList<PicBean>();
        in.readList(this.picList, PicBean.class.getClassLoader());
    }

    public static final Creator<MedicalRecord> CREATOR = new Creator<MedicalRecord>() {
        @Override
        public MedicalRecord createFromParcel(Parcel source) {
            return new MedicalRecord(source);
        }

        @Override
        public MedicalRecord[] newArray(int size) {
            return new MedicalRecord[size];
        }
    };
}
