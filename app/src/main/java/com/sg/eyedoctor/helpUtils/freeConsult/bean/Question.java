package com.sg.eyedoctor.helpUtils.freeConsult.bean;

import com.sg.eyedoctor.lookPicture.bean.Picture;

import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 */
public class Question {

    /**
     * questionId : ce1b5b91-9d05-4ef7-84fd-f57e32c2315a
     * patientId : 90cc06db-105f-407c-a4f9-c5593e1ad239
     * doctorId : 10003
     * name : 小小
     * sex : 0
     * age : 28
     * patientIM : p13559243607
     * questionDetail : 医生，请问一下，我太美了怎么办？
     * state : 1
     * picList : [{"id":"6b5b8d35-f092-48a5-95c7-e53a2734184a","questionId":"ce1b5b91-9d05-4ef7-84fd-f57e32c2315a","picUrl":"/UploadFile/CirclePicture/201606160931559112_1.jpg","microPicUrl":"/UploadFile/CirclePicture/201606160931559112_1_m.jpg","createDate":"2016-06-16 09:31:56"}]
     * createDate : 2016-06-16 09:31:56
     * newMessage : 0
     */

    public String questionId;
    public String patientId;
    public String doctorId;
    public String name;
    public String sex;
    public String age;
    public String patientIM;
    public String questionDetail;
    public String state;
    public String createDate;
    public int newMessage;
    /**
     * id : 6b5b8d35-f092-48a5-95c7-e53a2734184a
     * questionId : ce1b5b91-9d05-4ef7-84fd-f57e32c2315a
     * picUrl : /UploadFile/CirclePicture/201606160931559112_1.jpg
     * microPicUrl : /UploadFile/CirclePicture/201606160931559112_1_m.jpg
     * createDate : 2016-06-16 09:31:56
     */

    public List<Picture> picList;
}
