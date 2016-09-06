package com.sg.eyedoctor.settings.mySetting.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.VersionUtils;
import com.sg.eyedoctor.common.view.MenuTextView;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.loginRegister.activity.LoginActivity;
import com.sg.eyedoctor.settings.mySetting.bean.UpdateVersion;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 设置
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.password_mtv)
    private MenuTextView mPasswordMtv;
    @ViewInject(R.id.version_mtv)
    private MenuTextView mVersionMtv;
    @ViewInject(R.id.about_mtv)
    private MenuTextView mAboutMtv;
    @ViewInject(R.id.ensure_tv)
    private TextView mEnsureTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private UpdateVersion mVersion;
    private boolean mNeedUpdate = false;

    //查询网络更新
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<UpdateVersion>>() {
                }.getType();
                BaseResp<UpdateVersion> res = new Gson().fromJson(result, objectType);

                mVersion = res.value;
                mVersion.version = "2";
                mVersion.downLoadUrl = "http://yqall02.baidupcs.com/file/1f32158bb8e646d6eadf07efe611e03f?bkt=p3-14001f32158bb8e646d6eadf07efe611e03fa18bbfc80000000084bf&fid=286939992-250528-175493339424990&openDate=1463993252&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-ealOmM17zZbD239tQ50X0fSVZKI%3D&to=qyac&fm=Yan,B,T,t&sta_dx=0&sta_cs=0&sta_ft=apk&sta_ct=0&fm2=Yangquan,B,T,t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=14001f32158bb8e646d6eadf07efe611e03fa18bbfc80000000084bf&sl=68288590&expires=8h&rt=sh&r=797578354&mlogid=3332801317324786671&vuk=286939992&vbdid=1513760770&fin=doctor_3.0.apk&fn=doctor_3.0.apk&slt=pm&uta=0&rtype=1&iv=0&isw=0&dp-logid=3332801317324786671&dp-callid=0.1.1";
                if (Integer.valueOf(mVersion.version) > VersionUtils.getVersionCode(mContext)) {
                    mVersionMtv.setOtherTv("新版本" + mVersion.version);
                    mNeedUpdate = true;
                } else {
                    mVersionMtv.setOtherTv("已是最新版本");
                }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

    }


    @Override
    protected void initListener() {
        mPasswordMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        mVersionMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VersionUpdateActivity.class);
                intent.putExtra("version", mVersion);
                intent.putExtra("update", mNeedUpdate);
                startActivity(intent);
            }
        });
        mAboutMtv.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.ensure_tv)
    private void exit(View view) {
        showExitDialog();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //查询新版本
        showdialog();
        BaseManager.getLastestVersion(mCallback);
    }

    private void showExitDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(SettingActivity.this);
        ad.setTitle(R.string.exit_app);
        ad.setMessage(R.string.whether_exit_app);
        ad.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        logout();
                        AppManager.getAppManager().finishAllActivity();
                        LoginActivity.start(mContext);
                    }
                }
        );
        ad.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.create().show();
    }




}
