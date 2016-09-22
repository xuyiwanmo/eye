package com.sg.eyedoctor.settings.personalInfo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CameraUtils;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.settings.personalInfo.bean.ApproveResponse;
import com.sg.eyedoctor.settings.personalInfo.bean.Dept;
import com.sg.eyedoctor.settings.personalInfo.bean.Hospital;
import com.sg.eyedoctor.settings.personalInfo.bean.Level;
import com.sg.eyedoctor.settings.personalInfo.bean.Title;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 医生认证
 */
@ContentView(R.layout.activity_doctor_approve)
public class DoctorApproveActivity extends BaseActivity {
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    @ViewInject(R.id.tv_post_approve)
    private TextView mPostTv;
    @ViewInject(R.id.img_my_update_head)
    private ImageView mApproveImg;
    @ViewInject(R.id.ll_my_update_operation_num)
    private LinearLayout mOperationNumLv;
    @ViewInject(R.id.ll_my_update_hospital)
    private LinearLayout mHospitalLv;
    @ViewInject(R.id.ll_my_update_administrative_office)
    private LinearLayout mOfficeLv;
    @ViewInject(R.id.ll_my_update_title)
    private LinearLayout mTitleLv;
    @ViewInject(R.id.ll_my_update_rank)
    private LinearLayout mRankLv;
    @ViewInject(R.id.tv_my_update_operation)
    private TextView mOperationNumTv;
    @ViewInject(R.id.tv_my_update_operation)
    private TextView mOperationTv;
    @ViewInject(R.id.tv_my_update_hospital)
    private TextView mHospitalTv;
    @ViewInject(R.id.tv_my_update_administrative_office)
    private TextView mOfficeTv;
    @ViewInject(R.id.tv_my_update_title)
    private TextView mTitleTv;
    @ViewInject(R.id.tv_my_update_rank)
    private TextView mRankTv;
    @ViewInject(R.id.rl_my_update_approve)
    private RelativeLayout mApproveRl;
    @ViewInject(R.id.ll_my_update_idcard)
    private LinearLayout mIdcardLl;
    @ViewInject(R.id.tv_my_update_idcard)
    private TextView mIdcardTv;
    @ViewInject(R.id.line)
    private View mLine;

    private int mSotrt = 0;
    private File mUploadFile;
    private ArrayList<Hospital> mHospitals;
    private ArrayList<Level> mLevels;
    private ArrayList<Title> mTitles;
    private ArrayList<Dept> mDepts;

    private NetCallback mImgCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PicBean>>() {
                }.getType();
                BaseArrayResp<PicBean> res = new Gson().fromJson(result, objectType);
                mDoctor.licenseImages = res.value.get(0).picUrl;
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                CommonUtils.loadImg(mDoctor.licenseImages, mApproveImg);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.approving_ok);
                mDoctor.Authenticated = "认证中";
                mDoctor.state = 1;
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            } else {
                showToast(R.string.post_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mQueryCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<ApproveResponse>>() {
                }.getType();
                BaseResp<ApproveResponse> res = new Gson().fromJson(result, objectType);
                ApproveResponse value = res.value;
                mHospitals = value.hosList;
                mLevels = value.levelList;
                mTitles = value.titleList;
            } else {
                showToast(R.string.net_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mHospitals = new ArrayList<>();
        mDepts = new ArrayList<>();
        mTitles = new ArrayList<>();
        mLevels = new ArrayList<>();
        CommonUtils.loadImg(mDoctor.licenseImages, mApproveImg);
        BaseManager.queryApprove(mQueryCallback);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDoctor.licenseHospital != null && !mDoctor.licenseHospital.trim().equals("")) {
            mHospitalTv.setText(mDoctor.licenseHospital);
            mOfficeLv.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
        } else {
            mOfficeLv.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
        }
        if (mDoctor.licenseTitle != null) {
            mTitleTv.setText(mDoctor.licenseTitle);
        }
        if (mDoctor.licenseDept != null) {
            mOfficeTv.setText(mDoctor.licenseDept);
        }
        if (mDoctor.licenseLevel != null) {
            mRankTv.setText(mDoctor.licenseLevel);
        }
        if (mDoctor.licenseCard != null) {
            mOperationTv.setText(mDoctor.licenseCard);
        }

        if (mDoctor.idCard != null) {
            mIdcardTv.setText(mDoctor.idCard);
        }
        mUploadFile = new File(DoctorInfoActivity.SDCARD, DoctorInfoActivity.PATH + mDoctor.loginid + "/");
        if (!mUploadFile.exists()) {
            mUploadFile.mkdirs();
        }
        if (mIsAuth) {
            mPostTv.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            showToast(R.string.cancel);
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                CameraUtils.cropRawPhoto(DoctorApproveActivity.this, intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (CommonUtils.hasSdcard()) {
                    CameraUtils.cropRawPhoto(DoctorApproveActivity.this, Uri.fromFile(new File(mUploadFile, DoctorInfoActivity.APPROVE_JPG)));
                } else {
                    showToast(R.string.no_sdcard);
                }
                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Event(R.id.tv_post_approve)
    private void postApprove(View view) {
        if (mDoctor.licenseImages==null||mDoctor.licenseImages.trim().equals("")) {
            showToast(R.string.picure_not_empty);
            return;
        }
        showdialog();
        BaseManager.postApprove(mCallback);
    }

    @Event({R.id.ll_my_update_idcard, R.id.ll_my_update_hospital, R.id.ll_my_update_title, R.id.ll_my_update_rank, R.id.ll_my_update_operation_num, R.id.ll_my_update_administrative_office})
    private void edit(View view) {
        //0执业  1医院  2.科室   3.职称   4.级别  5.身份证
        Intent intent = new Intent(DoctorApproveActivity.this, DoctorApproveEditActivity.class);
        String hint = "";
        switch (view.getId()) {
            case R.id.ll_my_update_administrative_office:
                String hospital = mDoctor.licenseHospital;
                if (hospital == null) {
                    break;
                }
                Intent intent2 = getIntent();
                int index = intent2.getIntExtra(ConstantValues.KEY_HOS_ID, 0);
                mDepts = mHospitals.get(index).deptList;
                mSotrt = ConstantValues.APPROVE_DEPT;
                hint = mOfficeTv.getText().toString();
                intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA, mDepts);
                break;
            case R.id.ll_my_update_operation_num:
                mSotrt = ConstantValues.APPROVE_OPERATION;
                hint = mOperationNumTv.getText().toString();
                break;
            case R.id.ll_my_update_rank:
                mSotrt = ConstantValues.APPROVE_RANK;
                hint = mRankTv.getText().toString();
                intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA, mLevels);
                break;
            case R.id.ll_my_update_hospital:
                mSotrt = ConstantValues.APPROVE_HOSPITAL;
                intent.setExtrasClassLoader(Hospital.class.getClassLoader());
                intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA, mHospitals);
                hint = mHospitalTv.getText().toString();
                break;
            case R.id.ll_my_update_title:
                mSotrt = ConstantValues.APPROVE_TITLE;
                intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA, mTitles);
                hint = mTitleTv.getText().toString();
                break;
            case R.id.ll_my_update_idcard:
                mSotrt = ConstantValues.APPROVE_IDCARD;
                hint = mIdcardTv.getText().toString();
                break;
        }
        intent.putExtra(ConstantValues.KEY_SORT, mSotrt);
        intent.putExtra(ConstantValues.KEY_HINT, hint);
        startActivity(intent);
    }

    @Event(R.id.rl_my_update_approve)
    private void showPhotoDialog(View view) {
        final String[] items = getResources().getStringArray(
                R.array.takephoto);
        new AlertDialog.Builder(DoctorApproveActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == 0) {
                            CameraUtils.choseHeadImageFromCameraCapture(DoctorApproveActivity.this, new File(mUploadFile, DoctorInfoActivity.APPROVE_JPG));
                        } else {
                            CameraUtils.choseHeadImageFromGallery(DoctorApproveActivity.this);
                        }

                    }
                }).show();
    }


    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable(ConstantValues.KEY_DATA);
            File file = new File(mUploadFile, DoctorInfoActivity.APPROVE_JPG);
            CameraUtils.saveFile(photo, file);
            showdialog();
            ArrayList<String> list = new ArrayList<>();
            list.add(file.getPath());
            BaseManager.pictureUpload(list, mImgCallback);

        }
    }


}
