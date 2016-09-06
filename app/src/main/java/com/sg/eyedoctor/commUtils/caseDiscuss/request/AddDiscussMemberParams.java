package com.sg.eyedoctor.commUtils.caseDiscuss.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by zhanghua on 2016/1/11.
 */

public class AddDiscussMemberParams extends RequestParams {
    public String disId;
    public String memberId;

    public AddDiscussMemberParams() {
        super(ConstantValues.HOST+"/Doctor/DiscussionMemberAdd");
    }


}
