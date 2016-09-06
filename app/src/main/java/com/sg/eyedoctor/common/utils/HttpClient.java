package com.sg.eyedoctor.common.utils;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public  class HttpClient {
    public static void post(RequestParams params,NetCallback callback){
        log(params);
        x.http().post(params, callback);
    }

    private static void log(RequestParams params) {
        LogUtils.i(params.getUri());
        List<RequestParams.Header> headParams=params.getHeaders();
        for (KeyValue bodyParam : headParams) {
            LogUtils.i(bodyParam.key + "  " + bodyParam.value);
        }

        List<KeyValue> bodyParams=params.getBodyParams();
        for (KeyValue bodyParam : bodyParams) {
            LogUtils.i(bodyParam.key + "  " + bodyParam.value);
        }

        List<KeyValue> fileParams=params.getFileParams();
        for (KeyValue bodyParam : fileParams) {
            LogUtils.i(bodyParam.key+"   "+bodyParam.value);
        }
        List<KeyValue> queryParams=params.getQueryStringParams();
        for (KeyValue bodyParam : queryParams) {
            LogUtils.i(bodyParam.key+"   "+bodyParam.value);
        }
    }

    public static void get(RequestParams params,NetCallback callback){
        log(params);
        x.http().get(params, callback);
    }
}
