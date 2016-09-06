package com.sg.eyedoctor.chartFile.bean;

import java.util.ArrayList;

/**
 * Created by zhanghua on 2016/1/14.
 */
public class MedicineDetail {



        /**
         * hospitalId : 7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8
         * brid : 4108
         * prescribingDate : 2014-01-02 16: 42: 23
         * medicinemedicine : 2014-01-02 17: 19: 54
         * drugName : 小牛血去蛋白提取物眼用凝胶*
         * drugPrice : 51.9
         * drugAmount : 1
         * drugTotal : 51.9
         * type : 药店药品
         */

        public ArrayList<Drug> pharmacyList;
        /**
         * hospitalId : 7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8
         * brid : 4108
         * prescribingDate : 2014-01-0216: 42: 23
         * medicinemedicine : 2014-01-0217: 19: 54
         * drugName : 妥布霉素滴眼液（托百士)
         * drugPrice : 18.4
         * drugAmount : 1
         * drugTotal : 18.4
         * type : 药房药品
    */

        public ArrayList<Drug> drugstoreList;
        /**
         * hospitalId : 7A585AB3-ED33-B40D-C3E3-A8BB0A1195D8
         * brid : 443959
         * name : 吴巧玲
         * medicalTime :  2016-01-2019: 06: 25
         * costName : 裂隙灯检查
         * costPrice : 6
         * costAmount : 1
         * costTotal : 6
         * healthType : 乙类无
         */

        public ArrayList<Diagnosis> diagnosisList;


}
