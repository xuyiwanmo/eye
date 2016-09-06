package com.sg.eyedoctor.common.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

/**
 * Created by Administrator on 2016/2/24.
 */
public abstract class NetCallback implements Callback.CommonCallback<String> {
    private Context mContext;

    public NetCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onSuccess(String result) {
        LogUtils.i(result);
        //如果是-10004  则直接到登录界面
        try {
            JSONObject object = new JSONObject(result.toString());
            if (object.getInt("code") == -10004) {
                timeOut();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestOK(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

        // requestError(ex, isOnCallback);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    protected abstract void requestOK(String result);
    protected abstract void timeOut();

    //protected abstract void requestError(Throwable ex, boolean isOnCallback);
    protected void requestError(Throwable ex, boolean isOnCallback) {

    }
}
