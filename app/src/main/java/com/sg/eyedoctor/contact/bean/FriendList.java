package com.sg.eyedoctor.contact.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/3.
 */
public class FriendList extends ContactLetter implements Parcelable {

    /**
     * friendId : edd4b1ea-9d2f-487a-8c72-8d5e7850876d
     * state : 2
     * createDate : 2016-06-07 14:04:52
     * loginId : 15980859163
     * sex : 2
     * userName : 许小应
     * tel : 18559022862
     * birthDay : 1988-08-08
     * speciality : 啦啦啦
     * introduce : 喂喂喂
     */
    public String tel;
    //查询结果
    public String id;
    public String loginid;
    public String password;
    public int isLock;
    public String createSource;


    /**
     * friendId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * state : 1
     * createDate : 2016-01-25 15:04:53
     * loginId : 10001
     * sex : 1
     * picFileName : \UploadFile\HeadPic/201602051120012297_1.jpg
     * userName : 李文生
     * birthDay : 1978-2-2
     * speciality : 眼底病科主任医师，眼科教授，博士研究生导师。
     * introduce : 眼科学博士，温州医学院博士研究生导师，中华医学会眼科学分会专家会员
     * licenseHospital : 厦门大学附属厦门眼科中心
     * licenseDept : 视光科
     * licenseTitle : 助理医师
     */

    public String friendId;
    public int state;
    public String createDate;
    public String loginId;
    public int sex;
    public String picFileName;
    public String userName;
    public String birthDay;
    public String speciality;
    public String introduce;
    public String licenseHospital;
    public String licenseDept;
    public String licenseTitle;
    public boolean isChecked;

    public FriendList(String friendId, String picFileName, String userName) {
        this.friendId = friendId;
        this.picFileName = picFileName;
        this.userName = userName;
    }

    public FriendList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tel);
        dest.writeString(this.id);
        dest.writeString(this.loginid);
        dest.writeString(this.password);
        dest.writeInt(this.isLock);
        dest.writeString(this.createSource);
        dest.writeString(this.friendId);
        dest.writeInt(this.state);
        dest.writeString(this.createDate);
        dest.writeString(this.loginId);
        dest.writeInt(this.sex);
        dest.writeString(this.picFileName);
        dest.writeString(this.userName);
        dest.writeString(this.birthDay);
        dest.writeString(this.speciality);
        dest.writeString(this.introduce);
        dest.writeString(this.licenseHospital);
        dest.writeString(this.licenseDept);
        dest.writeString(this.licenseTitle);
        dest.writeByte(isChecked ? (byte) 1 : (byte) 0);
    }

    protected FriendList(Parcel in) {
        this.tel = in.readString();
        this.id = in.readString();
        this.loginid = in.readString();
        this.password = in.readString();
        this.isLock = in.readInt();
        this.createSource = in.readString();
        this.friendId = in.readString();
        this.state = in.readInt();
        this.createDate = in.readString();
        this.loginId = in.readString();
        this.sex = in.readInt();
        this.picFileName = in.readString();
        this.userName = in.readString();
        this.birthDay = in.readString();
        this.speciality = in.readString();
        this.introduce = in.readString();
        this.licenseHospital = in.readString();
        this.licenseDept = in.readString();
        this.licenseTitle = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<FriendList> CREATOR = new Creator<FriendList>() {
        @Override
        public FriendList createFromParcel(Parcel source) {
            return new FriendList(source);
        }

        @Override
        public FriendList[] newArray(int size) {
            return new FriendList[size];
        }
    };
}
