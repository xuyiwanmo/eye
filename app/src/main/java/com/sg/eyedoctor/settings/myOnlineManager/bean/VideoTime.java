package com.sg.eyedoctor.settings.myOnlineManager.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/5/24.
 */
public class VideoTime implements Parcelable {

    /**
     * id : da2a8c48-23f2-4cbe-8777-ba9655788cec
     * doctorId : 10003
     * week : 3
     * serviceDate : 2016-06-01
     * serviceTime : 04:01-07:02
     * serviceAmount : 3
     * createDate : 2016/5/30 12:01:15
     */


    public String id;
    public String doctorId;
    public String week;
    public String serviceDate;
    public String serviceTime;
    public String serviceAmount;
    public String createDate;

    public String startTime;
    public String endTime;
//    public int count;
//
    public VideoTime(String startTime, String endTime, int count) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceAmount = count+"";
    }


    public VideoTime() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.doctorId);
        dest.writeString(this.week);
        dest.writeString(this.serviceDate);
        dest.writeString(this.serviceTime);
        dest.writeString(this.serviceAmount);
        dest.writeString(this.createDate);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
    }

    protected VideoTime(Parcel in) {
        this.id = in.readString();
        this.doctorId = in.readString();
        this.week = in.readString();
        this.serviceDate = in.readString();
        this.serviceTime = in.readString();
        this.serviceAmount = in.readString();
        this.createDate = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
    }

    public static final Creator<VideoTime> CREATOR = new Creator<VideoTime>() {
        @Override
        public VideoTime createFromParcel(Parcel source) {
            return new VideoTime(source);
        }

        @Override
        public VideoTime[] newArray(int size) {
            return new VideoTime[size];
        }
    };
}
