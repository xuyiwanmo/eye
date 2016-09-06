package com.sg.eyedoctor.helpUtils.freeConsult.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ConsultDetail implements Parcelable {


    /**
     * id : 89181f60-d957-43c0-abf0-b6a9d39d1c3d
     * orderId : 29d59bd9-d2a4-41fb-a636-f610b431sx3e
     * doctorId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * patientId : 48b197c1-ab8d-4731-9b67-7670c3261aa6
     * result : 眼睛发炎
     * advice : 建议去医院做验光检查
     * createDate : 2016-04-25 18:18:53
     * drugList : [{"id":"fa039164-1655-42a2-8a6c-e9309eb528b4","adviceId":"89181f60-d957-43c0-abf0-b6a9d39d1c3d","drugName":"药品1","drugUnit":"50ml/瓶","drugCount":1}]
     * checkList : [{"id":"577abed6-3f99-44e7-b690-07c8fe9130bf","adviceId":"89181f60-d957-43c0-abf0-b6a9d39d1c3d","checkName":"验光"}]
     * description : 图文咨询订单
     * patientName : 王春梅
     * sex : 1
     * age : 18
     * picList : [{"id":"1","orderId":"29d59bd9-d2a4-41fb-a636-f610b431sx3e","picUrl":"/UploadFile/CirclePicture/201603211440049477_1.jpg","microPicUrl":"/UploadFile/CirclePicture/201603211440049477_1_m.jpg","createDate":"2016-04-25 11:38:07"},{"id":"2","orderId":"29d59bd9-d2a4-41fb-a636-f610b431sx3e","picUrl":"\\UploadFile\\CirclePicture/201604011400131839_2.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201604011400131839_2_m.jpg","createDate":"2016-04-25 11:38:07"}]
     */
    public String id;
    public String orderId;
    public String doctorId;
    public String patientId;
    public String result;
    public String advice;
    public String createDate;
    public String description;
    public String patientName;
    public String sex;
    public String age;
    /**
     * id : fa039164-1655-42a2-8a6c-e9309eb528b4
     * adviceId : 89181f60-d957-43c0-abf0-b6a9d39d1c3d
     * drugName : 药品1
     * drugUnit : 50ml/瓶
     * drugCount : 1
     */

    public ArrayList<DrugList> drugList;
    /**
     * id : 577abed6-3f99-44e7-b690-07c8fe9130bf
     * adviceId : 89181f60-d957-43c0-abf0-b6a9d39d1c3d
     * checkName : 验光
     */

    public ArrayList<CheckList> checkList;
    /**
     * id : 1
     * orderId : 29d59bd9-d2a4-41fb-a636-f610b431sx3e
     * picUrl : /UploadFile/CirclePicture/201603211440049477_1.jpg
     * microPicUrl : /UploadFile/CirclePicture/201603211440049477_1_m.jpg
     * createDate : 2016-04-25 11:38:07
     */

    public ArrayList<PicList> picList;

    public static class DrugList implements Parcelable {

        public String id;
        public String drugId;
        public String adviceId;
        public String drugName;
        public String drugUnit;
        public int drugCount;

        public DrugList() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.drugId);
            dest.writeString(this.adviceId);
            dest.writeString(this.drugName);
            dest.writeString(this.drugUnit);
            dest.writeInt(this.drugCount);
        }

        protected DrugList(Parcel in) {
            this.id = in.readString();
            this.drugId = in.readString();
            this.adviceId = in.readString();
            this.drugName = in.readString();
            this.drugUnit = in.readString();
            this.drugCount = in.readInt();
        }

        public static final Creator<DrugList> CREATOR = new Creator<DrugList>() {
            @Override
            public DrugList createFromParcel(Parcel source) {
                return new DrugList(source);
            }

            @Override
            public DrugList[] newArray(int size) {
                return new DrugList[size];
            }
        };
    }

    public static class CheckList implements Parcelable {


        public String id;
        public String checkId;
        public String adviceId;
        public String checkName;

        public CheckList() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.checkId);
            dest.writeString(this.adviceId);
            dest.writeString(this.checkName);
        }

        protected CheckList(Parcel in) {
            this.id = in.readString();
            this.checkId = in.readString();
            this.adviceId = in.readString();
            this.checkName = in.readString();
        }

        public static final Creator<CheckList> CREATOR = new Creator<CheckList>() {
            @Override
            public CheckList createFromParcel(Parcel source) {
                return new CheckList(source);
            }

            @Override
            public CheckList[] newArray(int size) {
                return new CheckList[size];
            }
        };
    }

    public static class PicList implements Parcelable {

        public String id;
        public String orderId;
        public String picUrl;
        public String microPicUrl;
        public String createDate;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.orderId);
            dest.writeString(this.picUrl);
            dest.writeString(this.microPicUrl);
            dest.writeString(this.createDate);
        }

        public PicList() {
        }

        protected PicList(Parcel in) {
            this.id = in.readString();
            this.orderId = in.readString();
            this.picUrl = in.readString();
            this.microPicUrl = in.readString();
            this.createDate = in.readString();
        }

        public static final Creator<PicList> CREATOR = new Creator<PicList>() {
            @Override
            public PicList createFromParcel(Parcel source) {
                return new PicList(source);
            }

            @Override
            public PicList[] newArray(int size) {
                return new PicList[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.orderId);
        dest.writeString(this.doctorId);
        dest.writeString(this.patientId);
        dest.writeString(this.result);
        dest.writeString(this.advice);
        dest.writeString(this.createDate);
        dest.writeString(this.description);
        dest.writeString(this.patientName);
        dest.writeString(this.sex);
        dest.writeString(this.age);
        dest.writeList(this.drugList);
        dest.writeList(this.checkList);
        dest.writeList(this.picList);
    }

    public ConsultDetail() {
    }

    protected ConsultDetail(Parcel in) {
        this.id = in.readString();
        this.orderId = in.readString();
        this.doctorId = in.readString();
        this.patientId = in.readString();
        this.result = in.readString();
        this.advice = in.readString();
        this.createDate = in.readString();
        this.description = in.readString();
        this.patientName = in.readString();
        this.sex = in.readString();
        this.age = in.readString();
        this.drugList = new ArrayList<DrugList>();
        in.readList(this.drugList, DrugList.class.getClassLoader());
        this.checkList = new ArrayList<CheckList>();
        in.readList(this.checkList, CheckList.class.getClassLoader());
        this.picList = new ArrayList<PicList>();
        in.readList(this.picList, PicList.class.getClassLoader());
    }

    public static final Creator<ConsultDetail> CREATOR = new Creator<ConsultDetail>() {
        @Override
        public ConsultDetail createFromParcel(Parcel source) {
            return new ConsultDetail(source);
        }

        @Override
        public ConsultDetail[] newArray(int size) {
            return new ConsultDetail[size];
        }
    };
}
