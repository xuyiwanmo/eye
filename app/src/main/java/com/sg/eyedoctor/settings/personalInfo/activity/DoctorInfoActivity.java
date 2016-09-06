package com.sg.eyedoctor.settings.personalInfo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CameraUtils;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.settings.personalInfo.bean.UploadResponse;
import com.sg.eyedoctor.settings.personalInfo.request.DoctorUpdateParams;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * 个人信息
 */
@ContentView(R.layout.activity_doctor_info)
public class DoctorInfoActivity extends BaseActivity { /* 请求识别码 */

    /*图片路径*/
    public static final String SDCARD = "sdcard";
    public static final String PATH = "/upload/";
    public static final String HEAD_JPG = "head.jpg";
    public static final String APPROVE_JPG = "approve.jpg";

    @ViewInject(R.id.rl_my_update_head)
    private RelativeLayout mHeadRl;
    @ViewInject(R.id.ll_my_update_sex)
    private LinearLayout mSexLl;
    @ViewInject(R.id.ll_my_update_name)
    private LinearLayout mNameLl;
    @ViewInject(R.id.ll_my_update_idcard)
    private LinearLayout mIdcardLl;
    @ViewInject(R.id.ll_my_update_birth)
    private LinearLayout mBirthLl;
    @ViewInject(R.id.ll_my_update_tel)
    private LinearLayout mTelLl;
    @ViewInject(R.id.ll_my_update_teach_title)
    private LinearLayout mTeachTitleLl;
    @ViewInject(R.id.ll_my_update_speciality)
    private LinearLayout mSpecialityLl;
    @ViewInject(R.id.rl_my_update_approve)
    private RelativeLayout mApproveRl;
    @ViewInject(R.id.ll_my_update_introduce)
    private LinearLayout mIntroduceLl;
    @ViewInject(R.id.img_my_update_head)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.tv_my_update_sex)
    private TextView mSexTv;
    @ViewInject(R.id.tv_my_update_birth)
    private TextView mBirthTv;
    @ViewInject(R.id.tv_my_update_name)
    private TextView mNameTv;
    @ViewInject(R.id.tv_my_update_idcard)
    private TextView mIdcardTv;
    @ViewInject(R.id.tv_my_update_teach_title)
    private TextView mTeachTitleTv;
    @ViewInject(R.id.tv_my_update_speciality)
    private TextView mSpecialityTv;
    @ViewInject(R.id.tv_approve)
    private TextView mApproveTv;
    @ViewInject(R.id.tv_my_update_introduce)
    private TextView mIntroduceTv;
    @ViewInject(R.id.tv_my_update_tel)
    private TextView mTelTv;

    private int mSort = ConstantValues.DOCTOR_SEX;
    private DoctorUpdateParams mParams;
    private Intent mIntent;
    private String mHint;
    private File mUploadFile;
    private DialogManager mDialogManager;
    private TimePickerView mTimePickerView;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<UploadResponse>>() {
                }.getType();
          //      BaseResp<UploadResponse> res = new Gson().fromJson(result, objectType);

                showToast(R.string.post_ok);
//                if (res.value.picFileName != null && !res.value.picFileName.equals("")) {
//                    mDoctor.picFileName = res.value.picFileName;
//                    try {
//                        LogUtils.i("save doctor");
//                        x.db().saveOrUpdate(mDoctor);
//                        initHead();
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//
//                }
            } else {
                showToast(R.string.post_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mHeadCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<UploadResponse>>() {
                }.getType();
                BaseResp<UploadResponse> res = new Gson().fromJson(result, objectType);

                mDoctor.picFileName = res.value.picFileName;
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                showToast(R.string.approve_ok);
                initHead();
            } else {
                showToast(R.string.post_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        mParams = new DoctorUpdateParams();
        mApproveTv.setText(mDoctor.Authenticated);
        mUploadFile = new File(SDCARD, PATH + mDoctor.loginid + "/");
        if (!mUploadFile.exists()) mUploadFile.mkdirs();
        initView();
        initHead();
        initBirth();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            showToast(R.string.cancel);
            return;
        }
        switch (requestCode) {
            case ConstantValues.CODE_GALLERY_REQUEST:
                CameraUtils.cropRawPhoto(DoctorInfoActivity.this, intent.getData());
                break;
            case ConstantValues.CODE_CAMERA_REQUEST:
                if (CommonUtils.hasSdcard()) {
                    CameraUtils.cropRawPhoto(DoctorInfoActivity.this, Uri.fromFile(new File(mUploadFile, HEAD_JPG)));
                } else {
                    showToast("没有SDCard!");
                }
                break;
            case ConstantValues.CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent, new File(mUploadFile, HEAD_JPG));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Event({R.id.ll_my_update_name, R.id.ll_my_update_idcard, R.id.ll_my_update_sex, R.id.ll_my_update_introduce, R.id.ll_my_update_teach_title, R.id.ll_my_update_speciality, R.id.ll_my_update_tel})
    private void updateTitle(View view) {
        mIntent = new Intent(DoctorInfoActivity.this, DoctorInfoEditActivity.class);
        switch (view.getId()) {
            case R.id.ll_my_update_sex:
                mSort = ConstantValues.DOCTOR_SEX;
                mHint = mDoctor.sex + "";
                break;
            case R.id.ll_my_update_idcard:
                mSort = ConstantValues.DOCTOR_IDCARD;
                mHint = mIdcardTv.getText().toString();
                break;
            case R.id.ll_my_update_name:
                mSort = ConstantValues.DOCTOR_NAME;
                mHint = mNameTv.getText().toString();
                break;
            case R.id.ll_my_update_teach_title:
                mSort = ConstantValues.DOCTOR_TITLE;
                mHint = mTeachTitleTv.getText().toString();
                break;
            case R.id.ll_my_update_speciality:
                mSort = ConstantValues.DOCTOR_SPECIALITY;
                mHint = mSpecialityTv.getText().toString();
                break;
            case R.id.ll_my_update_introduce:
                mSort = ConstantValues.DOCTOR_INTRODUCE;
                mHint = mIntroduceTv.getText().toString();
                break;
            case R.id.ll_my_update_tel:
                mSort = ConstantValues.DOCTOR_TEL;
                mHint = mTelTv.getText().toString();
                break;
        }
        mIntent.putExtra(ConstantValues.KEY_SORT, mSort);
        mIntent.putExtra(ConstantValues.KEY_HINT, mHint);
        startActivity(mIntent);

    }

    @Event(R.id.rl_my_update_head)
    private void showPhotoDialog(View view) {

        mSort = ConstantValues.DOCTOR_HEAD;

        final String[] items = getResources().getStringArray(
                R.array.takephoto);
        new AlertDialog.Builder(DoctorInfoActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == 0) {
                            CameraUtils.choseHeadImageFromCameraCapture(DoctorInfoActivity.this, new File(mUploadFile, HEAD_JPG));
                        } else {
                            CameraUtils.choseHeadImageFromGallery(DoctorInfoActivity.this);
                        }

                    }
                }).show();
    }

    @Event(R.id.rl_my_update_approve)
    private void doctorApprove(View view) {
        startActivity(new Intent(DoctorInfoActivity.this, DoctorApproveActivity.class));
    }

    @Event(R.id.ll_my_update_birth)
    private void choseBirth(View view) {

        if (mBirthTv.getText() != null) {
            Date date = CommonUtils.strToTime(mBirthTv.getText().toString());
            mTimePickerView.setTime(date);
        }

        mTimePickerView.show();
    }


    @Override
    protected void initView() {
        mDialogManager = new DialogManager(this);

        if (mDoctor.sex == 1) {
            mSexTv.setText("男");
        } else if (mDoctor.sex == 2) {
            mSexTv.setText("女");
        }
        setUserInfo(mNameTv, mDoctor.userName);
        setUserInfo(mBirthTv, mDoctor.birthDay);
        setUserInfo(mSpecialityTv, mDoctor.speciality);
        setUserInfo(mIntroduceTv, mDoctor.introduce);
        setUserInfo(mTelTv, mDoctor.tel);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initActionbar() {

    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent, File file) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            CameraUtils.saveFile(photo, file);
            mHeadImg.setImageBitmap(photo);
            BaseManager.postHead(mHeadCallback);
        }
    }

    private void setUserInfo(TextView textview, String str) {
        if (str == null) {
            return;
        }
        textview.setText(str);
    }

    private void initHead() {
        LogUtils.i(mDoctor.picFileName);
        CommonUtils.loadImg(mDoctor.picFileName, mHeadImg);
    }

    private void initBirth() {
        Date date=new Date(System.currentTimeMillis());
        int year= date.getYear();
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY, new Date(System.currentTimeMillis()), false, true);
        mTimePickerView.setRange(1900, 2200);
        mTimePickerView.setOnResultSelectListener(new TimePickerView.OnResultSelectListener() {
            @Override
            public boolean onTimeSelect(Date date) {
                String time = CommonUtils.getDateTime(date);
                if(date.getTime()>System.currentTimeMillis()){
                    showToast(R.string.date_error);
                    return false;
                }
                mBirthTv.setText(time);
                mParams.birthDay = time;
                mDoctor.birthDay = time;
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                BaseManager.updateInfo(mParams, mCallback);
                return true;
            }
        });
    }

}
