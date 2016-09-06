package com.sg.eyedoctor.settings.personalInfo.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.IdCardUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.settings.personalInfo.request.DoctorUpdateParams;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 个人信息-修改姓名,性别等
 */
@ContentView(R.layout.activity_doctor_info_edit)
public class DoctorInfoEditActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.ll_my_update_title)
    private LinearLayout mTitleLl; //身份证  教学职称  姓名  擅长
    @ViewInject(R.id.et_my_update_title)
    private EditText mTitleEt; //身份证  教学职称  姓名  擅长的输入框   电话号码
    @ViewInject(R.id.ll_my_update_introduce)
    private LinearLayout mIntroduceLv; //介绍
    @ViewInject(R.id.et_my_update_introduce)
    private EditText mIntroduceEt; //介绍输入框
    @ViewInject(R.id.ll_my_update_male)
    private LinearLayout mMaleLl; //男
    @ViewInject(R.id.ll_my_update_female)
    private LinearLayout mFemaleLl; //女
    @ViewInject(R.id.img_my_update_title_delete)
    private ImageView mTitleDeleteImg;
    @ViewInject(R.id.img_my_update_introduce_delete)
    private ImageView mIntroduceDeleteImg;
    @ViewInject(R.id.img_my_update_female_check)
    private ImageView mFemaleImg;
    @ViewInject(R.id.img_my_update_male_check)
    private ImageView mMaleImg;

    private String mHint;
    private String mTitle;

    private DoctorUpdateParams mParams;
    /**
     * 0性别    1身份证  2教学职称  3.擅长  4.介绍  7.姓名   8.手机号码
     */
    private int mSort = ConstantValues.DOCTOR_SEX;
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.update_ok);
                AppManager.getAppManager().finishActivity();
            } else {
                showToast(R.string.update_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        mSort = getIntent().getIntExtra(ConstantValues.KEY_SORT, 0);
        mHint = getIntent().getStringExtra(ConstantValues.KEY_HINT);
        switch (mSort) {
            case ConstantValues.DOCTOR_SEX: //男女
                mTitle = getResources().getText(R.string.sex).toString();
                mActionbar.setRightTvVisible(View.INVISIBLE);
                mMaleLl.setVisibility(View.VISIBLE);
                mFemaleLl.setVisibility(View.VISIBLE);
                if (mHint.equals("1")) {
                    mMaleImg.setVisibility(View.VISIBLE);
                    mFemaleImg.setVisibility(View.INVISIBLE);
                } else {
                    mFemaleImg.setVisibility(View.VISIBLE);
                    mMaleImg.setVisibility(View.INVISIBLE);
                }
                break;
            case ConstantValues.DOCTOR_IDCARD: //身份证
                mTitle = getResources().getText(R.string.idcard).toString();
                mActionbar.setRightTvVisible(View.VISIBLE);
                mTitleLl.setVisibility(View.VISIBLE);
                mTitleEt.setText(mHint);
                KeyBoardUtils.showKeyboard(mTitleEt);
                break;

            case ConstantValues.DOCTOR_NAME://姓名
                mTitle = getResources().getText(R.string.real_name).toString();
                mActionbar.setRightTvVisible(View.VISIBLE);
                mTitleLl.setVisibility(View.VISIBLE);
                mTitleEt.setText(mHint);
                KeyBoardUtils.showKeyboard(mTitleEt);
                break;
            case ConstantValues.DOCTOR_SPECIALITY://擅长
                mTitle = getResources().getText(R.string.speciality).toString();
                mActionbar.setRightTvVisible(View.VISIBLE);
                mTitleLl.setVisibility(View.VISIBLE);
                mTitleEt.setText(mHint);
                KeyBoardUtils.showKeyboard(mTitleEt);
                break;
            case ConstantValues.DOCTOR_INTRODUCE://介绍
                mTitle = getResources().getText(R.string.introduce).toString();
                mActionbar.setRightTvVisible(View.VISIBLE);
                mIntroduceLv.setVisibility(View.VISIBLE);
                KeyBoardUtils.showKeyboard(mIntroduceEt);
                mIntroduceEt.setText(mHint);
                break;
            case ConstantValues.DOCTOR_TEL://手机
                mTitle = getResources().getText(R.string.phone_number).toString();
                mActionbar.setRightTvVisible(View.VISIBLE);
                mTitleLl.setVisibility(View.VISIBLE);
                mTitleEt.setText(mHint);
                KeyBoardUtils.showKeyboard(mTitleEt);

                break;
        }

        mActionbar.setTitle(mTitle);
        mParams = new DoctorUpdateParams();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.complete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
            }
        });
    }

    @Event(R.id.img_my_update_title_delete)
    private void deleteTitle(View view) {
        mTitleEt.setText("");
    }

    @Event(R.id.img_my_update_introduce_delete)
    private void deleteIntroduce(View view) {
        mIntroduceEt.setText("");
    }

    @Event(R.id.ll_my_update_male)
    private void selectMale(View view) {
        mParams.sex = "1";
        mDoctor.sex = 1;
        try {
            x.db().saveOrUpdate(mDoctor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        mMaleImg.setVisibility(View.VISIBLE);
        mFemaleImg.setVisibility(View.INVISIBLE);

        updateDoctorInfo();
    }

    @Event(R.id.ll_my_update_female)
    private void selectFemale(View view) {
        mParams.sex = "2";
        mDoctor.sex = 2;
        try {
            x.db().saveOrUpdate(mDoctor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        mFemaleImg.setVisibility(View.VISIBLE);
        mMaleImg.setVisibility(View.INVISIBLE);
        updateDoctorInfo();
    }

    private void complete() {
        switch (mSort) {

            case ConstantValues.DOCTOR_IDCARD:
                boolean isIdcard = IdCardUtils.isIdcard(getText(mTitleEt));
                if (isIdcard) {//错误信息为""
                    mParams.idcard = mTitleEt.getText().toString();
                    mDoctor.idCard = mTitleEt.getText().toString();
                } else {
                    showToast(R.string.edit_error);
                    return;
                }

                break;
            case ConstantValues.DOCTOR_NAME:
                mParams.userName = mTitleEt.getText().toString();
                mDoctor.userName = mTitleEt.getText().toString();
                break;
            case ConstantValues.DOCTOR_TITLE:
                mParams.teachTitle = mTitleEt.getText().toString();
                break;
            case ConstantValues.DOCTOR_SPECIALITY:
                mParams.speciality = mTitleEt.getText().toString();
                mDoctor.speciality = mTitleEt.getText().toString();
                break;
            case ConstantValues.DOCTOR_INTRODUCE:
                mParams.introduce = mIntroduceEt.getText().toString();
                mDoctor.introduce = mIntroduceEt.getText().toString();
                break;
            case ConstantValues.DOCTOR_TEL:
                mParams.tel = mTitleEt.getText().toString();
                mDoctor.tel = mTitleEt.getText().toString();

                if (!CommonUtils.isMobile(mDoctor.tel)) {
                    showToast(R.string.tel_error);
                    return;
                }
                break;
        }
        try {
            x.db().saveOrUpdate(mDoctor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        updateDoctorInfo();
    }

    private void updateDoctorInfo() {
        BaseManager.updateInfo(mParams, mCallback);
    }

}
