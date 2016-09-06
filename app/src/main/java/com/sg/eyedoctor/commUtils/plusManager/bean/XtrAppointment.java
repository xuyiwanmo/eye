package com.sg.eyedoctor.commUtils.plusManager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/11.
 */
public class XtrAppointment implements Serializable {

    /**
     * id : 29d59bd9-d2a4-41fb-a636-f610b431863e
     * doctorId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * week : 1
     * timeType : AM
     * endSpan : 13:00
     * beginSpan : 12:00
     * xtrAccount : 4
     * xtrHospital : 厦门眼科中心
     * xtrType : 1
     * xtrPrice : 10
     * remark : 挂号时间段内有效到场即有效
     * createDate : 2016-03-10 16:17:07
     * orderby : 0
     * remainingAccount : 3
     */

    public String id;
    public String doctorId;
    public int week;
    public String timeType;
    public String endSpan;
    public String beginSpan;
    public String xtrAccount;
    public String xtrHospital;
    public int xtrType;
    public String xtrPrice;
    public String remark;
    public String createDate;
    public int orderby;
    public String remainingAccount;
    public String hospitalId;

}
