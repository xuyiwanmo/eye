package com.sg.eyedoctor.consult.phoneConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sg.eyedoctor.common.utils.CommonUtils;

import java.util.Date;

/**
 * Created by Administrator on 2016/5/3.
 */
public class TimeSet implements Parcelable {


    public long time;
    public int count;

    public long lastTime; //30 * 60 * 1000
    public boolean isFirst;

    public TimeSet(long time, int count) {
        this.time = time;
        this.count = count;
    }
    public TimeSet(long time, int count,boolean isFirst) {
        this.time = time;
        this.count = count;
        this.isFirst=isFirst;

    }

    @Override
    public String toString() {
        return "TimeSet{" +
                "isFirst=" + isFirst +
                ", count=" + count +
                ", openDate=" + CommonUtils.getTime(new Date(time)) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.time);
        dest.writeInt(this.count);
        dest.writeLong(this.lastTime);
        dest.writeByte(isFirst ? (byte) 1 : (byte) 0);
    }

    protected TimeSet(Parcel in) {
        this.time = in.readLong();
        this.count = in.readInt();
        this.lastTime = in.readLong();
        this.isFirst = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TimeSet> CREATOR = new Parcelable.Creator<TimeSet>() {
        @Override
        public TimeSet createFromParcel(Parcel source) {
            return new TimeSet(source);
        }

        @Override
        public TimeSet[] newArray(int size) {
            return new TimeSet[size];
        }
    };
}
