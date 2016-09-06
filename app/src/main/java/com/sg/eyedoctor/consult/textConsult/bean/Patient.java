package com.sg.eyedoctor.consult.textConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhanghua on 2015/12/31.
 */
public class Patient implements Parcelable {

    //视频加号id
    public String xtrId;


    /**
     * id : 1
     * doctorId : 10002
     * patientId : 3ad77524-dcde-4b0e-98ae-b186cf06d91a
     * orderNo : VAS114615554385
     * orderAmount : 30.0
     * orderType : 1
     * state : 1
     * description : 眼睛红肿，分泌物增多
     * payAccount : 1015407633@qq.com
     * payType : 1
     * newMessage:"
     * createDate : 2016-04-25 11:37:20
     * picFileName : ~\File//1//201604011802430601.jpg
     * age : 27
     * sex : 1
     * patientName : 陈铭锟
     * PatientUser : 1
     */


    public String patientTel;
    public String doctorIM;


    public String id;
    public String doctorId;
    public String patientId;
    public String orderNo;
    public double orderAmount;
    public int orderType;
    public int state;
    public String description;
    public String payAccount;
    public int payType;
    public String createDate;
    public String modifyDate;  //订单完成时间
    public String dealTime;//预约时间
    public String age;
    public String sex;
    public String patientName;
    public String patientUser;
    public String patientIM;
    public String doctorTel;
    public String writeState;
    public String picFileName;
    public String latestDate;//最近聊天时间
    public boolean clickable;
    public int authTime;

    public String newMessage;

    public Patient() {
    }

    public static class ValueEntity {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.xtrId);
        dest.writeString(this.patientTel);
        dest.writeString(this.doctorIM);
        dest.writeString(this.id);
        dest.writeString(this.doctorId);
        dest.writeString(this.patientId);
        dest.writeString(this.orderNo);
        dest.writeDouble(this.orderAmount);
        dest.writeInt(this.orderType);
        dest.writeInt(this.state);
        dest.writeString(this.description);
        dest.writeString(this.payAccount);
        dest.writeInt(this.payType);
        dest.writeString(this.createDate);
        dest.writeString(this.modifyDate);
        dest.writeString(this.dealTime);
        dest.writeString(this.age);
        dest.writeString(this.sex);
        dest.writeString(this.patientName);
        dest.writeString(this.patientUser);
        dest.writeString(this.patientIM);
        dest.writeString(this.doctorTel);
        dest.writeString(this.writeState);
        dest.writeString(this.picFileName);
        dest.writeString(this.latestDate);
        dest.writeByte(this.clickable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.authTime);
        dest.writeString(this.newMessage);
    }

    protected Patient(Parcel in) {
        this.xtrId = in.readString();
        this.patientTel = in.readString();
        this.doctorIM = in.readString();
        this.id = in.readString();
        this.doctorId = in.readString();
        this.patientId = in.readString();
        this.orderNo = in.readString();
        this.orderAmount = in.readDouble();
        this.orderType = in.readInt();
        this.state = in.readInt();
        this.description = in.readString();
        this.payAccount = in.readString();
        this.payType = in.readInt();
        this.createDate = in.readString();
        this.modifyDate = in.readString();
        this.dealTime = in.readString();
        this.age = in.readString();
        this.sex = in.readString();
        this.patientName = in.readString();
        this.patientUser = in.readString();
        this.patientIM = in.readString();
        this.doctorTel = in.readString();
        this.writeState = in.readString();
        this.picFileName = in.readString();
        this.latestDate = in.readString();
        this.clickable = in.readByte() != 0;
        this.authTime = in.readInt();
        this.newMessage = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel source) {
            return new Patient(source);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}
