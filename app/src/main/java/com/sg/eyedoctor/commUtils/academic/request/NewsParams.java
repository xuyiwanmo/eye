package com.sg.eyedoctor.commUtils.academic.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by 德医互联 on 2016/9/14.
 */
public class NewsParams extends RequestParams {

    public String page;
    public String rows;
    public String overType;
    public String type;
    public String Name;
    public String EditorName;

    public NewsParams(String page, String rows, String overType, String type, String name, String editorName) {
        super(ConstantValues.HOST + "/Hospital/GetNews");
        this.page = page;
        this.rows = rows;
        this.overType = overType;
        this.type = type;
        Name = name;
        EditorName = editorName;
    }
}
