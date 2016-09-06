package com.sg.eyedoctor.contact.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class FriendValidateParams extends RequestParams {
    public String friendId;
    public String state;
    public String applyId;
    public String doctorId;

    public FriendValidateParams(String friendId, String state, String applyId, String doctorId) {
        super(ConstantValues.HOST+"/Circle/CircleFriendValidate");
        this.friendId = friendId;
        this.state = state;
        this.applyId = applyId;
        this.doctorId = doctorId;
    }

}
