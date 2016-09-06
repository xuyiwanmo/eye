package com.sg.eyedoctor.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/22.
 */
public class PicLocalBean implements Parcelable ,Serializable{

    public String picUrl;
    public String microPicUrl;
    public String localUrl;

    public PicLocalBean(String microPicUrl, String picUrl) {
        this.microPicUrl = microPicUrl;
        this.picUrl = picUrl;
    }

    public PicLocalBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picUrl);
        dest.writeString(this.microPicUrl);
        dest.writeString(this.localUrl);
    }

    protected PicLocalBean(Parcel in) {
        this.picUrl = in.readString();
        this.microPicUrl = in.readString();
        this.localUrl = in.readString();
    }

    public static final Creator<PicLocalBean> CREATOR = new Creator<PicLocalBean>() {
        @Override
        public PicLocalBean createFromParcel(Parcel source) {
            return new PicLocalBean(source);
        }

        @Override
        public PicLocalBean[] newArray(int size) {
            return new PicLocalBean[size];
        }
    };
}
