package com.sg.eyedoctor.settings.personalInfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/1/28.
 */
public class Title implements Parcelable {
    public String code;
    public String name;
    public boolean isChoose=false;

    public Title() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeByte(isChoose ? (byte) 1 : (byte) 0);
    }

    protected Title(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<Title> CREATOR = new Creator<Title>() {
        @Override
        public Title createFromParcel(Parcel source) {
            return new Title(source);
        }

        @Override
        public Title[] newArray(int size) {
            return new Title[size];
        }
    };
}
