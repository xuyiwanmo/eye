package com.sg.eyedoctor.settings.personalInfo.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ApproveResponse {

        /**
         * hosCode : 10001
         * hosName : 厦门大学附属厦门眼科中心
         * deptList : [{"deptCode":"101","deptName":"视光科"},{"deptCode":"102","deptName":"白内障"},{"deptCode":"103","deptName":"青光眼"},{"deptCode":"104","deptName":"眼底病"},{"deptCode":"105","deptName":"小儿眼科"}]
         */

        public ArrayList<Hospital> hosList;
        /**
         * code : 1
         * name : 助理医师
         */

        public ArrayList<Title> titleList;
        /**
         * code : 1
         * name : 初级
         */
        public ArrayList<Level> levelList;



}
