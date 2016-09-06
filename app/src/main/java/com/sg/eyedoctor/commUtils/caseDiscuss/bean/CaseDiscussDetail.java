package com.sg.eyedoctor.commUtils.caseDiscuss.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/29.
 */
public class CaseDiscussDetail implements Parcelable {


    /**
     * disId : b99e1f88-a9a3-415c-a004-9e940b4caf9e
     * doctorId : 89600d57-4e9c-462f-9f05-bc125f136dcf
     * patientName : 你好
     * sex : 0
     * age : 12
     * illness : 12
     * createDate : 2016-03-31 14:55:30
     * memberList : [{"id":"52ee182e-143e-436d-823c-38d76ca398c1","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","memberId":"89600d57-4e9c-462f-9f05-bc125f136dcf","createDate":"2016-03-31 14:55:30","userName":"张华","picFileName":"\\UploadFile\\HeadPic/201602191019403472_1.jpg"},{"id":"45964d19-5e41-44a8-ba7f-62fcfd2e0792","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","memberId":"\"27899cbc-68c9-4954-b4e6-c0baf0256b3d\"]","createDate":"2016-03-31 14:55:30"},{"id":"bd26b286-3704-4c5a-b98c-d5a5b3774c63","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","memberId":"[\"18f4600e-8b79-4a58-aeef-e0a408e50208\"","createDate":"2016-03-31 14:55:30"}]
     * picList : [{"id":"213ecf36-db2c-4b6c-bc97-ca7573cae447","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","picUrl":"\\UploadFile\\CirclePicture/201603311455293724_1.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603311455293724_1_m.jpg","createDate":"2016-03-31 14:55:30"},{"id":"913d6ee7-b1e3-49e2-939c-b539d6305afe","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","picUrl":"\\UploadFile\\CirclePicture/201603311455293724_2.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603311455293724_2_m.jpg","createDate":"2016-03-31 14:55:30"},{"id":"80bf1aec-4e4b-4a75-9ac9-d56193a1f24a","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","picUrl":"\\UploadFile\\CirclePicture/201603311455293880_3.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603311455293880_3_m.jpg","createDate":"2016-03-31 14:55:30"},{"id":"54685a2c-e291-4d97-ba2f-5649ab51b850","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","picUrl":"\\UploadFile\\CirclePicture/201603311455301836_4.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603311455301992_4_m.jpg","createDate":"2016-03-31 14:55:30"},{"id":"5e7d4c80-c8a6-46ba-b71c-dc78131ce115","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","picUrl":"\\UploadFile\\CirclePicture/201603311455302460_5.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603311455302460_5_m.jpg","createDate":"2016-03-31 14:55:30"},{"id":"81c8e032-dfb1-4f50-a83b-0e3620376088","disId":"b99e1f88-a9a3-415c-a004-9e940b4caf9e","picUrl":"\\UploadFile\\CirclePicture/201603311455303084_6.jpg","microPicUrl":"\\UploadFile\\CirclePicture/201603311455303084_6_m.jpg","createDate":"2016-03-31 14:55:30"}]
     */

    public String disId;
    public String doctorId;
    public String patientName;
    public String sex;
    public int age;
    public String illness;
    public String createDate;
    /**
     * id : 52ee182e-143e-436d-823c-38d76ca398c1
     * disId : b99e1f88-a9a3-415c-a004-9e940b4caf9e
     * memberId : 89600d57-4e9c-462f-9f05-bc125f136dcf
     * createDate : 2016-03-31 14:55:30
     * userName : 张华
     * picFileName : \UploadFile\HeadPic/201602191019403472_1.jpg
     */

    public ArrayList<MemberList> memberList;
    /**
     * id : 213ecf36-db2c-4b6c-bc97-ca7573cae447
     * disId : b99e1f88-a9a3-415c-a004-9e940b4caf9e
     * picUrl : \UploadFile\CirclePicture/201603311455293724_1.jpg
     * microPicUrl : \UploadFile\CirclePicture/201603311455293724_1_m.jpg
     * createDate : 2016-03-31 14:55:30
     */

    public ArrayList<PicList> picList;
    public String diagnosisResult;

    public static class MemberList implements Parcelable {


        public String id;
        public String disId;
        public String memberId;
        public String createDate;
        public String userName;
        public String picFileName;

        public MemberList() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.disId);
            dest.writeString(this.memberId);
            dest.writeString(this.createDate);
            dest.writeString(this.userName);
            dest.writeString(this.picFileName);
        }

        public MemberList(String memberId, String userName, String picFileName) {
            this.memberId = memberId;
            this.userName = userName;
            this.picFileName = picFileName;
        }

        protected MemberList(Parcel in) {
            this.id = in.readString();
            this.disId = in.readString();
            this.memberId = in.readString();
            this.createDate = in.readString();
            this.userName = in.readString();
            this.picFileName = in.readString();
        }

        public static final Creator<MemberList> CREATOR = new Creator<MemberList>() {
            @Override
            public MemberList createFromParcel(Parcel source) {
                return new MemberList(source);
            }

            @Override
            public MemberList[] newArray(int size) {
                return new MemberList[size];
            }
        };
    }

    public static class PicList implements Parcelable {



        public String id;
        public String disId;
        public String picUrl;
        public String microPicUrl;
        public String createDate;

        public PicList() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.disId);
            dest.writeString(this.picUrl);
            dest.writeString(this.microPicUrl);
            dest.writeString(this.createDate);
        }

        protected PicList(Parcel in) {
            this.id = in.readString();
            this.disId = in.readString();
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

    public CaseDiscussDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.disId);
        dest.writeString(this.doctorId);
        dest.writeString(this.patientName);
        dest.writeString(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.illness);
        dest.writeString(this.createDate);
        dest.writeTypedList(memberList);
        dest.writeTypedList(picList);
        dest.writeString(this.diagnosisResult);
    }

    protected CaseDiscussDetail(Parcel in) {
        this.disId = in.readString();
        this.doctorId = in.readString();
        this.patientName = in.readString();
        this.sex = in.readString();
        this.age = in.readInt();
        this.illness = in.readString();
        this.createDate = in.readString();
        this.memberList = in.createTypedArrayList(MemberList.CREATOR);
        this.picList = in.createTypedArrayList(PicList.CREATOR);
        this.diagnosisResult = in.readString();
    }

    public static final Creator<CaseDiscussDetail> CREATOR = new Creator<CaseDiscussDetail>() {
        @Override
        public CaseDiscussDetail createFromParcel(Parcel source) {
            return new CaseDiscussDetail(source);
        }

        @Override
        public CaseDiscussDetail[] newArray(int size) {
            return new CaseDiscussDetail[size];
        }
    };
}
