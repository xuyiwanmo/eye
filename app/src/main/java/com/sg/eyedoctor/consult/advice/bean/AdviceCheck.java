package com.sg.eyedoctor.consult.advice.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/22.
 */
public class AdviceCheck implements Parcelable {

    /**
     * value : {"rows":[{"id":"1","name":"药品1","standard":"60","unit":"毫升"},{"id":"2","name":"药品2","standard":"33","unit":"毫升"}],"total":2}
     * code : 10000
     */
    public String id;
    public String name;
    public String standard;
    public String unit;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.standard);
        dest.writeString(this.unit);
    }

    public AdviceCheck(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected AdviceCheck(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.standard = in.readString();
        this.unit = in.readString();
    }

    public static final Parcelable.Creator<AdviceCheck> CREATOR = new Parcelable.Creator<AdviceCheck>() {
        @Override
        public AdviceCheck createFromParcel(Parcel source) {
            return new AdviceCheck(source);
        }

        @Override
        public AdviceCheck[] newArray(int size) {
            return new AdviceCheck[size];
        }
    };
}
