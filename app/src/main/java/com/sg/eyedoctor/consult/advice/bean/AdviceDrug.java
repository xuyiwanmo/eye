package com.sg.eyedoctor.consult.advice.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/22.
 */
public class AdviceDrug implements Parcelable {

    /**
     * rows : [{"id":"1","name":"检测1 "}]
     * total : 1
     */

    public String id;
    public String name;
    public String standard;
    public String unit;

    public int count=0;

    public AdviceDrug(String id, String name, String unit, int count) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
    }

    protected AdviceDrug(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<AdviceDrug> CREATOR = new Creator<AdviceDrug>() {
        @Override
        public AdviceDrug createFromParcel(Parcel source) {
            return new AdviceDrug(source);
        }

        @Override
        public AdviceDrug[] newArray(int size) {
            return new AdviceDrug[size];
        }
    };
}
