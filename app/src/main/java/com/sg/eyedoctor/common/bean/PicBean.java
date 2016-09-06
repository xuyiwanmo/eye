package com.sg.eyedoctor.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/22.
 */
public class PicBean implements Parcelable {

    public String picUrl;
    public String microPicUrl;

    public PicBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picUrl);
        dest.writeString(this.microPicUrl);
    }

    protected PicBean(Parcel in) {
        this.picUrl = in.readString();
        this.microPicUrl = in.readString();
    }

    public static final Creator<PicBean> CREATOR = new Creator<PicBean>() {
        @Override
        public PicBean createFromParcel(Parcel source) {
            return new PicBean(source);
        }

        @Override
        public PicBean[] newArray(int size) {
            return new PicBean[size];
        }
    };
}
