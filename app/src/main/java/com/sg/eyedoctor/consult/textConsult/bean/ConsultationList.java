package com.sg.eyedoctor.consult.textConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sg.eyedoctor.common.bean.PicBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ConsultationList implements Parcelable {


    public String createDate;
    public String illDetail;
    public String description;
    public int type;
    public String outpatientCard;
    public String orderId;
    public String newId;
    public ArrayList<PicBean> picList;

    public ConsultationList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createDate);
        dest.writeString(this.illDetail);
        dest.writeString(this.description);
        dest.writeInt(this.type);
        dest.writeString(this.outpatientCard);
        dest.writeString(this.orderId);
        dest.writeString(this.newId);
        dest.writeList(this.picList);
    }

    protected ConsultationList(Parcel in) {
        this.createDate = in.readString();
        this.illDetail = in.readString();
        this.description = in.readString();
        this.type = in.readInt();
        this.outpatientCard = in.readString();
        this.orderId = in.readString();
        this.newId = in.readString();
        this.picList = new ArrayList<PicBean>();
        in.readList(this.picList, PicBean.class.getClassLoader());
    }

    public static final Creator<ConsultationList> CREATOR = new Creator<ConsultationList>() {
        @Override
        public ConsultationList createFromParcel(Parcel source) {
            return new ConsultationList(source);
        }

        @Override
        public ConsultationList[] newArray(int size) {
            return new ConsultationList[size];
        }
    };
}
