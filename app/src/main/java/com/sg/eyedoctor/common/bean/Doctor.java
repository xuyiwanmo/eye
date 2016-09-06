package com.sg.eyedoctor.common.bean;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;

/**
 * Created by zhanghua on 2016/1/17.
 */
@Table(name = "Doctor")
public class Doctor{

    /**
     * espaceNumber :
     * espacePass :
     * sex : 1
     * picFileName : F:\HXYK\UploadFile\Certification/201601151732499594_1.jpg
     * isAuthenticated : 已认证   0未认证  认证中
     * verifyCode : 2709f9b58fca409388766ba2c3c37343
     * id : 3ad95f34-bf82-42ed-a7ef-6e28ed5709c3
     * loginid : 123
     * password : 123
     * userName : 张思
     * tel : 18250794126
     * state : 0    0未认证   1认证中    2已认证
     * isLock : 0
     * createDate : 2016/1/6 15:40:11
     * birthDay : 1989-03-09
     * title : 高级医师
     * speciality : 擅长青光眼
     */

    @Column(name = "id",isId = true)
    public int Id;

    @Column(name = "sex")
    public int sex;

    @Column(name = "createSource")
    public String createSource;

    @Column(name = "certNo")
    public String certNo;


    @Column(name = "picFileName")
    public String picFileName;

    @Column(name = "Authenticated")
    public String Authenticated;

    @Column(name = "verifyCode")
    public String verifyCode;

    @Column(name = "remoteId")
    public String id;

    @Column(name = "loginid")
    public String loginid;

    @Column(name = "password")
    public String password;

    @Column(name = "userName")
    public String userName;

    @Column(name = "tel")
    public String tel;

    @Column(name = "state")
    public int state;

    @Column(name = "isLock")
    public int isLock;

    @Column(name = "createDate")
    public String createDate;

    @Column(name = "birthDay")
    public String birthDay;

    @Column(name = "title")
    public String licenseTitle;

    @Column(name = "speciality")
    public String speciality;

    @Column(name="licenseImages")
    public String licenseImages;

    @Column(name="totalAppointment")
    public String totalAppointment;

    @Column(name="appointment")
    public String appointment;

    @Column(name = "teachTitle")
    public String teachTitle;

    @Column(name = "introduce")
    public String introduce;

    @Column(name = "idcard")
    public String idCard;

    @Column(name="level")
    public String licenseLevel;

    @Column(name="hospital")
    public String licenseHospital;

    @Column(name="dept")
    public String licenseDept;

    @Column(name = "operation")
    public String licenseCard;
    /**
     * type : 1
     * price : 0.01
     * isOpen : True
     */

    public ArrayList<Vas> VasSet;

    @Column(name = "textId")
    public String textId;

    @Column(name = "textPrice")
    public String textPrice;

    @Column(name = "textIsOpen")
    public String textIsOpen;

    @Column(name = "phoneId")
    public String phoneId;

    @Column(name = "phonePrice")
    public String phonePrice;

    @Column(name = "phoneIsOpen")
    public String phoneIsOpen;

    @Column(name = "videoPrice")
    public String videoPrice;

    @Column(name = "videoId")
    public String videoId;

    @Column(name = "videoIsOpen")
    public String videoIsOpen;

    @Column(name = "addId")
    public String addId;

    @Column(name = "addPrice")
    public String addPrice;

    @Column(name = "addIsOpen")
    public String addIsOpen;



}
