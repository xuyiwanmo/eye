package com.sg.eyedoctor.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 德医互联 on 2016/9/12.
 */
public class TeamRead implements Parcelable{

    public String teamId;
    public int readCount;

    public TeamRead(String teamId, int readCount) {
        this.teamId = teamId;
        this.readCount = readCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teamId);
        dest.writeInt(this.readCount);
    }

    protected TeamRead(Parcel in) {
        this.teamId = in.readString();
        this.readCount = in.readInt();
    }

    public static final Creator<TeamRead> CREATOR = new Creator<TeamRead>() {
        @Override
        public TeamRead createFromParcel(Parcel source) {
            return new TeamRead(source);
        }

        @Override
        public TeamRead[] newArray(int size) {
            return new TeamRead[size];
        }
    };
}
