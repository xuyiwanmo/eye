package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 新增分组
 */
public class AddGroupParams extends RequestParams {
    public String groupName;


    public AddGroupParams(String groupName) {
        super(ConstantValues.HOST+"/Doctor/AddGroup");
        this.groupName=groupName;
    }
}
