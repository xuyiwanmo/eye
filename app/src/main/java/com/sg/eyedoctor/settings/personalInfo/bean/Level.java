package com.sg.eyedoctor.settings.personalInfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/1/28.
 */
public class Level implements Parcelable {
    public String code;
    public String name;
    public boolean isChoose=false;

    public Level() {
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

    protected Level(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel source) {
            return new Level(source);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };
}
