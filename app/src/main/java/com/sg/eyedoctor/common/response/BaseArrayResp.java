package com.sg.eyedoctor.common.response;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/10.
 */
public class BaseArrayResp<T> {
    public int code;
    public ArrayList<T> value=new ArrayList<>();
}
