package com.sg.eyedoctor.settings.personalInfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/28.
 */
public class Hospital implements Parcelable {
    public String hosCode;
    public String hosName;
    public boolean isChoose=false;
    /**
     * deptCode : 101
     * deptName : 视光科
     */

    public ArrayList<Dept> deptList=new ArrayList<Dept>();

    public Hospital() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hosCode);
        dest.writeString(this.hosName);
        dest.writeByte(isChoose ? (byte) 1 : (byte) 0);
        dest.writeTypedList(deptList);
    }

    protected Hospital(Parcel in) {
        this.hosCode = in.readString();
        this.hosName = in.readString();
        this.isChoose = in.readByte() != 0;
        this.deptList = in.createTypedArrayList(Dept.CREATOR);
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel source) {
            return new Hospital(source);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };
}
