package com.sg.eyedoctor.chartFile.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/31.
 */
public class CaseRecord {
    /**
     * id : def9d7a1-e998-4889-9701-83df64ec055e
     * assType : 2
     * assUserId : 89600d57-4e9c-462f-9f05-bc125f136dcf
     * name : 范先生
     * idCard : 350203201107080612
     * sex : 1
     * createDate : 2016-03-31 11:51:01
     * grouping : 1
     * modifyDate : 2016-03-31 11:51:01
     * caseList : [{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-03-21 11:29:12","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"809291"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-03-21 10:55:06","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"809207"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-03-08 10:51:24","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜干燥","outpatientCard":"796118"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-02-23 10:42:53","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"779974"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-02-16 11:14:56","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"770963"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-02-16 10:55:54","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"变应性结膜炎","outpatientCard":"770897"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-02-04 11:55:27","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"763589"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-02-04 11:43:59","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","outpatientCard":"763572"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2016-01-21 17:03:36","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"749057"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2014-08-20 17:17:05","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"233025"},{"hospitalId":"7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8","medicalTime":"2014-08-18 20:22:48","brid":"173710","name":"范铠铭","medicalCard":"D94167243","idCard":"350203201107080612","patientType":"医保","diagnosis":"结膜炎","outpatientCard":"230154"}]
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
    public String city;
    public String createDate;
    public String grouping;
    public String modifyDate;
    public String isFocus;
    public int age;

    /**
     * hospitalId : 7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8
     * medicalTime : 2016-03-21 11:29:12
     * brid : 173710
     * name : 范铠铭
     * medicalCard : D94167243
     * idCard : 350203201107080612
     * patientType : 医保
     * diagnosis : 结膜炎
     * outpatientCard : 809291
     */

    public ArrayList<MedicalRecord> caseList;


}
