package com.sg.eyedoctor.settings.mySetting.bean;

import java.io.Serializable;

/**
 * 版本更新信息
 */
public class UpdateVersion implements Serializable {

    /**
     * id : 1
     * version : doctor_5.13
     * downLoadUrl : X
     * title : 眼科通医生版v5.13更新
     * remark : 1.修改了 病历详情中的检查名字
     * <p/>
     * type : 1
     * createDate : 2016-05-13 20:29:18
     * createUser : 系统
     */

    public String id;
    public String version;
    public String downLoadUrl;
    public String title;
    public String remark;
    public int type;
    public String createDate;
    public String createUser;

}
