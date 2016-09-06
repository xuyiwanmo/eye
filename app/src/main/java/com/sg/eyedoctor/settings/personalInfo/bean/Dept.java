package com.sg.eyedoctor.settings.personalInfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/1/28.
 */
public class Dept implements Parcelable {

    public String deptCode;
    public String deptName;
    public boolean isChoose=false;

    public Dept() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deptCode);
        dest.writeString(this.deptName);
        dest.writeByte(isChoose ? (byte) 1 : (byte) 0);
    }

    protected Dept(Parcel in) {
        this.deptCode = in.readString();
        this.deptName = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<Dept> CREATOR = new Creator<Dept>() {
        @Override
        public Dept createFromParcel(Parcel source) {
            return new Dept(source);
        }

        @Override
        public Dept[] newArray(int size) {
            return new Dept[size];
        }
    };
}
