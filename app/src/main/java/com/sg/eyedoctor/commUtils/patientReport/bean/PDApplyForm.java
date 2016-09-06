package com.sg.eyedoctor.commUtils.patientReport.bean;

import com.sg.eyedoctor.lookPicture.bean.Picture;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/12.
 */
public class PDApplyForm {
    /**
     * applyId : a2673857-9c30-446e-bcd8-d0f62f15d2b4
     * patientId : 90cc06db-105f-407c-a4f9-c5593e1ad239
     * treatmentDate : 2016-06-30
     * diagnosisResult : hai
     * illness : ni hao
     * createDate : 2016-06-12 17:33:35
     * picList : [{"id":"b29ff20f-915f-489d-b4d3-91dcdc7007d1","applyId":"a2673857-9c30-446e-bcd8-d0f62f15d2b4","picUrl":"/UploadFile/CirclePicture/201606121733336370_1.jpg","microPicUrl":"/UploadFile/CirclePicture/201606121733336370_1_m.jpg","createDate":"2016-06-12 17:33:35"},{"id":"38d8a9fe-b540-4277-afe2-617cec238c55","applyId":"a2673857-9c30-446e-bcd8-d0f62f15d2b4","picUrl":"/UploadFile/CirclePicture/201606121733353218_1.jpg","microPicUrl":"/UploadFile/CirclePicture/201606121733353530_1_m.jpg","createDate":"2016-06-12 17:33:35"}]
     * name : 小小
     * sex : 0
     * age : 28
     */

        public String applyId;
        public String patientId;
        public String treatmentDate;
        public String diagnosisResult;
        public String illness;
        public String createDate;
        public String name;
        public String sex;
        public String age;
        /**
         * id : b29ff20f-915f-489d-b4d3-91dcdc7007d1
         * applyId : a2673857-9c30-446e-bcd8-d0f62f15d2b4
         * picUrl : /UploadFile/CirclePicture/201606121733336370_1.jpg
         * microPicUrl : /UploadFile/CirclePicture/201606121733336370_1_m.jpg
         * createDate : 2016-06-12 17:33:35
         */

        public ArrayList<Picture> picList;

}
