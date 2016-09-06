package com.sg.eyedoctor.consult.phoneConsult.bean;

/**
 * Created by Administrator on 2016/5/3.
 */
public class PhoneConsultTime {

    /**
     * value : [{"id":"9a2c7a24-00b4-4c34-b12c-80a6af461b14","doctorId":"10002","dutyTime":"01:00","week":1,"state":0,"createDate":"2016-05-04 17:54:51"},{"id":"2144f1aa-a737-4fd9-8504-4ef0d1cec6f7","doctorId":"10002","dutyTime":"00:00","week":1,"state":0,"createDate":"2016-05-04 17:54:51"},{"id":"93840f0a-1834-4365-a643-81cd6b0646ad","doctorId":"10002","dutyTime":"02:00","week":1,"state":0,"createDate":"2016-05-04 17:54:51"},{"id":"b56a36aa-9162-4deb-83cc-87f59c0509f0","doctorId":"10002","dutyTime":"01:30","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"7754e8d6-8309-4567-a65c-68a4f5dd057c","doctorId":"10002","dutyTime":"02:00","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"167ac31a-d7a9-4b65-9a74-8ef5f51ebdd9","doctorId":"10002","dutyTime":"02:30","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"dfba9d81-2b03-4a85-8ae8-2f13592be254","doctorId":"10002","dutyTime":"03:30","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"714d1174-41fb-425d-84c6-65b3bb5074fc","doctorId":"10002","dutyTime":"04:00","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"0394b859-bfdb-4545-a1f1-5b6ea0cc1ebb","doctorId":"10002","dutyTime":"04:30","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"72ba9933-eba7-4c38-8ae7-5f44876b508c","doctorId":"10002","dutyTime":"16:00","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"2dc6b9f7-53b6-4ccf-a85f-a25ee4a308ee","doctorId":"10002","dutyTime":"01:00","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"39630e22-7d84-4979-b7db-3a7590ddfa6b","doctorId":"10002","dutyTime":"00:30","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"efea3514-3a44-43ce-b55f-809909f8ea06","doctorId":"10002","dutyTime":"00:00","week":2,"state":0,"createDate":"2016-05-04 17:55:42"},{"id":"f3268308-04b3-424f-960b-a3fa36bcb0c4","doctorId":"10002","dutyTime":"03:00","week":2,"state":0,"createDate":"2016-05-04 17:55:42"}]
     * code : 10000
     */

    public String id;
    public String doctorId;
    public String dutyTime;
    public int week;
    public int state;
    public String createDate;

    public int type=0;  //空位普通  1加号  2空白

    public int count;
    public boolean isFirst;



    public PhoneConsultTime(int type) {
        this.type = type;
    }
}
