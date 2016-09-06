package com.sg.eyedoctor.lookPicture.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/29.
 */
public class Picture implements Parcelable,Serializable {

    public String questionId;

    public String applyId;


    /**
     * id : a0092131-973c-4b04-8910-72e6c9322c1f
     * topicId : f0ef1925-eefb-43c0-91e9-9efda23b3d88
     * picUrl : \UploadFile\CirclePicture/201603031100466492_1.jpg
     * microPicUrl : \UploadFile\CirclePicture/201603031100466648_1_m.jpg
     * type : 1
     * createDate : 2016-03-03 11:00:47
     */

    public String topicId;
    public int type;

    public String id;
    public String consulId;
    public String picUrl;
    public String microPicUrl;
    public String createDate;
    public boolean isSDcard;

    public Picture(String picUrl,String microPicUrl) {
        this.picUrl=picUrl;
        this.microPicUrl=microPicUrl;
    }
    public Picture(String picUrl,String microPicUrl,boolean isSDcard) {
        this.picUrl=picUrl;
        this.microPicUrl=microPicUrl;
        this.isSDcard=isSDcard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.applyId);
        dest.writeString(this.topicId);
        dest.writeInt(this.type);
        dest.writeString(this.id);
        dest.writeString(this.consulId);
        dest.writeString(this.picUrl);
        dest.writeString(this.microPicUrl);
        dest.writeString(this.createDate);
        dest.writeByte(isSDcard ? (byte) 1 : (byte) 0);
    }

    protected Picture(Parcel in) {
        this.applyId = in.readString();
        this.topicId = in.readString();
        this.type = in.readInt();
        this.id = in.readString();
        this.consulId = in.readString();
        this.picUrl = in.readString();
        this.microPicUrl = in.readString();
        this.createDate = in.readString();
        this.isSDcard = in.readByte() != 0;
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
