package com.sg.eyedoctor.settings.myStateMessage.bean;

import com.sg.eyedoctor.helpUtils.massNotice.bean.MassReceiver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 站内消息
 */
public class StateMessage implements Serializable{



    /**
     * messageId : 6203debc-8d4f-4efb-9954-82e4470997b2
     * type : 2
     * messageDetail : 哈哈哈哈哈
     * publisherId : 18f4600e-8b79-4a58-aeef-e0a408e50208
     * createDate : 2016-04-15 15:53:21
     * publisherType : 1
     */
    public String messageId;
    public int type;
    public String messageDetail;
    public String messageTitle;
    public String publisherId;
    public String publisherName;
    public String createDate;
    public String isRead;//0：未读|1：已读
    public int publisherType;

    public ArrayList<MassReceiver> receiverList;

}
