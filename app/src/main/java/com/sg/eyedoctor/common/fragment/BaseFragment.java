package com.sg.eyedoctor.common.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.loginRegister.activity.LoginActivity;
import com.sg.eyedoctor.loginRegister.helper.LogoutHelper;
import com.sg.eyedoctor.main.activity.CertificationActivity;

/**
 * Created by wyouflf on 15/11/4.
 */
public abstract class BaseFragment extends CommFragment {
    private ProgressDialog mProgressDialog;
    public DisplayImageOptions mImageOptions;

    public boolean mIsAuth=false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //显示图片的配置
        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    /**
     * 等待登录对话框
     */
    public void showLoginDlg() {

        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在查询，请稍后······");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    public void closeDialog() {
        ((BaseActivity) getActivity()).closeDialog();
        if(mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

    }



    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int stringId) {
        Toast.makeText(getActivity(), getText(stringId), Toast.LENGTH_SHORT).show();
    }

    protected abstract void initView();

    protected abstract void initListener();

    public void onTimeOut(){
        closeDialog();
        showToast(R.string.verticode_time_out);
        LogoutHelper.logout();
        AppManager.getAppManager().finishAllActivity();
        LoginActivity.start(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        mDoctor=BaseManager.getDoctor();
        if(mDoctor!=null&&mDoctor.state==2){
            mIsAuth=true;
        }else{
            mIsAuth=false;
        }
    }

    public void startAuthActivity(String title) {
        Intent intent=new Intent(getActivity(), CertificationActivity.class);
        intent.putExtra(ConstantValues.KEY_TITLE, title);
        startActivity(intent);
    }
    public void startAuthActivity(int titleId) {
        startAuthActivity(getResources().getString(titleId));
    }


}
