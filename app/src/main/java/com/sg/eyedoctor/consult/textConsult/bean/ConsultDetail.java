package com.sg.eyedoctor.consult.textConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ConsultDetail implements Parcelable {


    public String id;
    public String orderId;
    public String doctorId;
    public String patientId;
    public String result;
    public String advice;
    public String createDate;
    public String drugList;
    public String checkList;
    public String description;
    public String patientName;
    public String sex;
    public String age;
    /**
     * id : b2f1f7cb-7ab3-49fc-ab8b-a0805b67ab0f
     * orderId : e76dbf4e-c5a7-47db-8ea6-554b331ccea3
     * picUrl : \CirclePicture/201607121722135620_1.jpg
     * microPicUrl : \CirclePicture/201607121722135620_1_m.jpg
     * createDate : 2016-07-12 17:29:07
     */

    public ArrayList<PicList> picList;

    public static class PicList implements Parcelable {
        public String id;
        public String orderId;
        public String picUrl;
        public String microPicUrl;
        public String createDate;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.orderId);
            dest.writeString(this.picUrl);
            dest.writeString(this.microPicUrl);
            dest.writeString(this.createDate);
        }

        public PicList() {
        }

        protected PicList(Parcel in) {
            this.id = in.readString();
            this.orderId = in.readString();
            this.picUrl = in.readString();
            this.microPicUrl = in.readString();
            this.createDate = in.readString();
        }

        public static final Creator<PicList> CREATOR = new Creator<PicList>() {
            @Override
            public PicList createFromParcel(Parcel source) {
                return new PicList(source);
            }

            @Override
            public PicList[] newArray(int size) {
                return new PicList[size];
            }
        };
    }

    public ConsultDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.orderId);
        dest.writeString(this.doctorId);
        dest.writeString(this.patientId);
        dest.writeString(this.result);
        dest.writeString(this.advice);
        dest.writeString(this.createDate);
        dest.writeString(this.drugList);
        dest.writeString(this.checkList);
        dest.writeString(this.description);
        dest.writeString(this.patientName);
        dest.writeString(this.sex);
        dest.writeString(this.age);
        dest.writeTypedList(this.picList);
    }

    protected ConsultDetail(Parcel in) {
        this.id = in.readString();
        this.orderId = in.readString();
        this.doctorId = in.readString();
        this.patientId = in.readString();
        this.result = in.readString();
        this.advice = in.readString();
        this.createDate = in.readString();
        this.drugList = in.readString();
        this.checkList = in.readString();
        this.description = in.readString();
        this.patientName = in.readString();
        this.sex = in.readString();
        this.age = in.readString();
        this.picList = in.createTypedArrayList(PicList.CREATOR);
    }

    public static final Creator<ConsultDetail> CREATOR = new Creator<ConsultDetail>() {
        @Override
        public ConsultDetail createFromParcel(Parcel source) {
            return new ConsultDetail(source);
        }

        @Override
        public ConsultDetail[] newArray(int size) {
            return new ConsultDetail[size];
        }
    };
}
