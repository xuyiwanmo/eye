package com.sg.eyedoctor.helpUtils.freeClinic.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/5/27.
 */
public class GetFreeClinicList implements Parcelable {

    /**
     * id : c92ce17c-87ec-4634-893a-956631524382
     * doctorId : 10004
     * amount : 3
     * remainingAmount : 3
     * type : 2
     * openDate : 2016/5/28 0:00:00
     * createDate : 2016/5/27 11:02:18
     */

    public String id;
    public String doctorId;
    public int amount;
    public int remainingAmount;
    public int type;
    public String openDate;
    public String createDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.doctorId);
        dest.writeInt(this.amount);
        dest.writeInt(this.remainingAmount);
        dest.writeInt(this.type);
        dest.writeString(this.openDate);
        dest.writeString(this.createDate);
    }

    public GetFreeClinicList() {
    }

    protected GetFreeClinicList(Parcel in) {
        this.id = in.readString();
        this.doctorId = in.readString();
        this.amount = in.readInt();
        this.remainingAmount = in.readInt();
        this.type = in.readInt();
        this.openDate = in.readString();
        this.createDate = in.readString();
    }

    public static final Parcelable.Creator<GetFreeClinicList> CREATOR = new Parcelable.Creator<GetFreeClinicList>() {
        @Override
        public GetFreeClinicList createFromParcel(Parcel source) {
            return new GetFreeClinicList(source);
        }

        @Override
        public GetFreeClinicList[] newArray(int size) {
            return new GetFreeClinicList[size];
        }
    };
}
