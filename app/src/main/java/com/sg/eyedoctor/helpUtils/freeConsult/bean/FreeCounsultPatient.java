package com.sg.eyedoctor.helpUtils.freeConsult.bean;

import java.util.ArrayList;

/**
 * 图文咨询-患者详情
 */

public class FreeCounsultPatient {

    /**
     * id : ed694098-618f-4e5e-98c2-8dafb826b6ab
     * name : 范铠铭
     * idCard : 350203201107080612
     * sex : 1
     * createDate : 2016-03-18 18:40:02
     * grouping : 1
     * modifyDate : 2016-03-24 17:35:16
     * consultationList : [{"createDate":"2016-03-21 11:29:12","description":"结膜炎","type":2,"outpatientCard":"809291"},{"createDate":"2016-03-21 10:55:06","description":"结膜炎","type":2,"outpatientCard":"809207"},{"createDate":"2016-03-08 10:51:24","description":"结膜干燥","type":2,"outpatientCard":"796118"},{"createDate":"2016-02-23 10:42:53","description":"结膜炎","type":2,"outpatientCard":"779974"},{"createDate":"2016-02-16 11:14:56","description":"结膜炎","type":2,"outpatientCard":"770963"},{"createDate":"2016-02-16 10:55:54","description":"变应性结膜炎","type":2,"outpatientCard":"770897"},{"createDate":"2016-02-04 11:55:27","description":"结膜炎","type":2,"outpatientCard":"763589"},{"createDate":"2016-02-04 11:43:59","type":2,"outpatientCard":"763572"},{"createDate":"2016-01-21 17:03:36","description":"结膜炎","type":2,"outpatientCard":"749057"},{"createDate":"2014-08-20 17:17:05","description":"结膜炎","type":2,"outpatientCard":"233025"},{"createDate":"2014-08-18 20:22:48","description":"结膜炎","type":2,"outpatientCard":"230154"},{"createDate":"2016-04-07 11:16:55","description":"屈光不正","type":2,"outpatientCard":"826346"}]
     */
    public String id;
    public String name;
    public String idCard;
    public int sex;
    public String createDate;
    public String grouping;
    public String modifyDate;
    /**
     * createDate : 2016-03-21 11:29:12
     * description : 结膜炎
     * type : 2
     * outpatientCard : 809291
     */

    public ArrayList<ConsultationList> consultationList;

}
