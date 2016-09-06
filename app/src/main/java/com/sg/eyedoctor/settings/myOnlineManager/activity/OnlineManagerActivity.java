package com.sg.eyedoctor.settings.myOnlineManager.activity;

import android.content.Intent;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.OnlineManagerLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 在线服务管理
 */
@ContentView(R.layout.activity_online_manager)
public class OnlineManagerActivity extends BaseActivity {

    @ViewInject(R.id.text_oml)
    private OnlineManagerLayout mTextOml;
    @ViewInject(R.id.phone_oml)
    private OnlineManagerLayout mPhoneOml;
    @ViewInject(R.id.video_oml)
    private OnlineManagerLayout mVideoOml;
    @ViewInject(R.id.add_oml)
    private OnlineManagerLayout mAddOml;

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.text_oml)
    private void setTextOml(View view) {
        startActivity(new Intent(mContext,SetTextConsultActivity.class));
    }

    @Event(R.id.phone_oml)
    private void setPhoneOml(View view) {
        startActivity(new Intent(mContext, SetPhoneConsultActivity.class));
    }

    @Event(R.id.video_oml)
    private void setVideoOml(View view) {
        startActivity(new Intent(mContext, SetVideoConsultActivity.class));
    }

    @Event(R.id.add_oml)
    private void setAddOml(View view) {
        startActivity(new Intent(mContext, SetAddConsultActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPriceView(mDoctor.textIsOpen, mDoctor.textPrice, mTextOml);
        setPriceView(mDoctor.phoneIsOpen,mDoctor.phonePrice, mPhoneOml);
        setPriceView(mDoctor.videoIsOpen,mDoctor.videoPrice, mVideoOml);
        setPriceView(mDoctor.addIsOpen, mDoctor.addPrice, mAddOml);
    }


    private void setPriceView(String isOpen,String price, OnlineManagerLayout oml) {
        if(isOpen.equals("True")){//开通
            oml.setPriceTv(price+"元/次");
        }else {
            oml.setPriceTv(getString(R.string.alredy_close));
        }
    }
}
