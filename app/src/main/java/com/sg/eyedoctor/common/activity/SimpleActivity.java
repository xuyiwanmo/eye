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
        x.view().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.actionbar_color);

        //umPush
        PushAgent.getInstance(this).onAppStart();
    }

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

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initActionbar();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
