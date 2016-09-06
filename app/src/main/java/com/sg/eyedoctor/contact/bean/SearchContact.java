package com.sg.eyedoctor.contact.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/3.
 */
public class SearchContact implements Serializable{

    /**
     * sex : 1
     * picFileName : \UploadFile\HeadPic/201602051120012297_1.jpg
     * id : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * loginid : 10001
     * password : 123
     * userName : 李文生
     * state : 1
     * isLock : 0
     * createSource : Web
     * createDate : 2016/1/25 15:04:53
     * birthDay : 1978-2-2
     * speciality : 眼底病科主任医师，眼科教授，博士研究生导师。
     * introduce : 眼科学博士，温州医学院博士研究生导师，中华医学会眼科学分会专家会员
     */
    public int sex;
    public String picFileName;
    public String id;
    public String loginid;
    public String password;
    public String userName;
    public int state;
    public int isLock;
    public String createSource;
    public String createDate;
    public String birthDay;
    public String speciality;
    public String introduce;
}
