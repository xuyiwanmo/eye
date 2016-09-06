package com.sg.eyedoctor.contact.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class AddFriendParams extends RequestParams {
    public String applyId;
    public String receiveId;


    public AddFriendParams(String applyId, String receiveId) {

        super(ConstantValues.HOST+"/Circle/CircleFriendAdd");
        this.applyId = applyId;
        this.receiveId = receiveId;
    }
}
