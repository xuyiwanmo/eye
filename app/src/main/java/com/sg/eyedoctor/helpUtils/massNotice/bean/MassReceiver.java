package com.sg.eyedoctor.helpUtils.massNotice.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/15.
 */
public class MassReceiver implements Serializable {

    public String receiverId;
    public String receiverName;
    public int receiverType;

    public MassReceiver(String receiverId,String  receiverName, int receiverType) {
        this.receiverId = receiverId;
        this.receiverType = receiverType;
        this.receiverName = receiverName;
    }

}
