package com.sg.eyedoctor.common.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.view.SystemBarTintManager;
import com.umeng.message.PushAgent;

import org.xutils.x;

public abstract class SimpleActivity extends FragmentActivity {
    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        mContext=this;
        //xutils初始化
        x.view().inject(this);

        setStatusColor();

        //友盟推送
        PushAgent.getInstance(this).onAppStart();
    }

    /**
     * 设置状态栏颜色
     */
    private void setStatusColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.actionbar_color);
    }

    /**
     * 设置状态栏一体化
     * @param on
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 初始化界面
     */
    protected abstract void initView();
    /**
     * 初始化监听事件
     */
    protected abstract void initListener();
    /**
     * 初始化自定义Actionbar
     */
    protected abstract void initActionbar();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
