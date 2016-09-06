package com.sg.eyedoctor.consult.openConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ConsultBean implements Parcelable {

    public int drawableId;
    public int titleId;
    public Class activity;

    public ConsultBean(int drawableId, int titleId, Class activity) {
        this.drawableId = drawableId;
        this.titleId = titleId;
        this.activity = activity;
    }

    public ConsultBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.drawableId);
        dest.writeInt(this.titleId);
        dest.writeSerializable(this.activity);
    }

    protected ConsultBean(Parcel in) {
        this.drawableId = in.readInt();
        this.titleId = in.readInt();
        this.activity = (Class) in.readSerializable();
    }

    public static final Creator<ConsultBean> CREATOR = new Creator<ConsultBean>() {
        @Override
        public ConsultBean createFromParcel(Parcel source) {
            return new ConsultBean(source);
        }

        @Override
        public ConsultBean[] newArray(int size) {
            return new ConsultBean[size];
        }
    };
}
