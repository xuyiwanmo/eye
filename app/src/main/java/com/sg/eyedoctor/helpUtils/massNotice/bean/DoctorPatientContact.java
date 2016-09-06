package com.sg.eyedoctor.helpUtils.massNotice.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 群发通知-选择联系人
 */
public class DoctorPatientContact implements Parcelable {


    public int type;
    public String id;
    public String picFile;
    public String name;

    public DoctorPatientContact(String id, String picFile, String name,int type) {
        this.id = id;
        this.picFile = picFile;
        this.name = name;
        this.type=type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.id);
        dest.writeString(this.picFile);
        dest.writeString(this.name);
    }

    protected DoctorPatientContact(Parcel in) {
        this.type = in.readInt();
        this.id = in.readString();
        this.picFile = in.readString();
        this.name = in.readString();
    }

    public static final Creator<DoctorPatientContact> CREATOR = new Creator<DoctorPatientContact>() {
        @Override
        public DoctorPatientContact createFromParcel(Parcel source) {
            return new DoctorPatientContact(source);
        }

        @Override
        public DoctorPatientContact[] newArray(int size) {
            return new DoctorPatientContact[size];
        }
    };
}
