package com.sg.eyedoctor.chartFile.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class PatientByGroup implements Serializable {
    public String tel;
    public String medicalCard;
    public String isFocus;

    /**
     * id : 25a3ce6f-644f-4355-acb6-14ec8c8ac08d
     * assType : 2
     * assUserId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * name : 笑话
     * idCard : 35555
     * sex : 2
     * birth : 2012/4/1 0:00:00
     * illness : 你好
     * remark : 梦想
     * createDate : 2016-04-01 10:18:24
     * grouping : 1
     * modifyDate : 2016-04-01 10:18:24
     * city : 山西 | 晋城 | 高平市
     * caseList : []
     */

        public String id;
        public int assType;
        public String assUserId;
        public String name;
        public String idCard;
        public int sex;
        public String birth;
        public String illness;
        public String remark;
        public String createDate;
        public String grouping;
        public String modifyDate;
        public String city;
        public ArrayList<?> caseList;

}
