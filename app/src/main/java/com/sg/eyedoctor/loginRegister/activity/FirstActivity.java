package com.sg.eyedoctor.loginRegister.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.utils.SPUtils;

public class FirstActivity extends Activity {

    private ImageView mFirstImg;
    private boolean mFirstLogin;
    private Context mContext;

    private DisplayImageOptions mConfig = new DisplayImageOptions.Builder()
            .cacheInMemory(true)// 在内存中会缓存该图片
            .cacheOnDisk(true)// 在硬盘中会缓存该图片
            .considerExifParams(true)// 会识别图片的方向信息
            .resetViewBeforeLoading(true)// 重设图片
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);
        mFirstImg = (ImageView) findViewById(R.id.first_img);
        mContext = this;
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.guide_1, mFirstImg);

        mFirstLogin = (boolean) SPUtils.get(mContext, ConstantValues.KEY_FIRST_LOGIN, true);
        mFirstImg.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mFirstLogin) {//第一次登陆进入引导页
                    mContext.startActivity(new Intent(mContext, GuideActivity.class));
                    SPUtils.put(mContext, ConstantValues.KEY_FIRST_LOGIN, false);
                } else {
                    LoginActivity.start(mContext);
                }
                finish();
            }
        }, 500);
    }

}
