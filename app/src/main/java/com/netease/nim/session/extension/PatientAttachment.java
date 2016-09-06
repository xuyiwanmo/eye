package com.netease.nim.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class PatientAttachment extends CustomAttachment {

    public static final String KEY_SEX = "sex";
    public static final String KEY_ILL = "symptom";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";

    public String sex;
    public String name;
    public String ill;
    public String age;

    public PatientAttachment(JSONObject data) {
        super(CustomAttachmentType.Patient);
        load(data);
    }


    @Override
    protected void parseData(JSONObject value) {
        JSONObject data=value.getJSONObject("value");
        name = data.getString(KEY_NAME);
        ill = data.getString(KEY_ILL);
        sex = data.getString(KEY_SEX);
        age = data.getString(KEY_AGE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_NAME, name);
        data.put(KEY_ILL, ill);
        data.put(KEY_SEX, sex);
        data.put(KEY_AGE, age);
        JSONObject value=new JSONObject();
        value.put("value",data);
        return value;
    }

    private void load(JSONObject value) {
        JSONObject data=value.getJSONObject("value");
        name = data.getString(KEY_NAME);
        ill = data.getString(KEY_ILL);
        sex = data.getString(KEY_SEX);
        age = data.getString(KEY_AGE);

    }
}
