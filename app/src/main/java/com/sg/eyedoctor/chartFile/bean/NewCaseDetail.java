package com.sg.eyedoctor.chartFile.bean;

import com.sg.eyedoctor.common.bean.PicBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/22.
 */
public class NewCaseDetail {
    public String newId;

    public String doctorId;
    public String patientId;
    public String result;
    public String advice;
    public String createDate;
    public String description;
    public String patientName;
    public String sex;
    public String age;

    public ArrayList<PicBean> picList;
//    public ArrayList<CheckBean> checkList;
//    public ArrayList<DrugBean> drugList;
    public String checkList;
    public String drugList;
}
