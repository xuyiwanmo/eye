package com.sg.eyedoctor.chartFile.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.request.PatientParams;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.manager.SpeechManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.IdCardUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.textConsult.bean.CounsultPatient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;

/**
 * 编辑患者资料
 */
@ContentView(R.layout.activity_patient_detail_edit)
public class PatientDetailEditActivity extends BaseActivity {

    @ViewInject(R.id.et_add_patient_idcard)
    private EditText mIdcardEt;
    @ViewInject(R.id.et_add_patient_clinic_num)
    private EditText mClinicEt;  //门诊号码
    @ViewInject(R.id.et_add_patient_name)
    private EditText mNameEt;
    @ViewInject(R.id.img_add_patient_sex_male)
    private ImageView mMaleImg;
    @ViewInject(R.id.both_eye_tv)
    private TextView mMaleTv;
    @ViewInject(R.id.img_add_patient_sex_female)
    private ImageView mFemaleImg;
    @ViewInject(R.id.tv_add_patient_female)
    private TextView mFemaleTv;
    @ViewInject(R.id.et_add_patient_age)
    private EditText mAgeEt;
    @ViewInject(R.id.age_et)
    private EditText mPhoneEt;
    @ViewInject(R.id.ll_add_patient_city)
    private LinearLayout mCityLl;

    @ViewInject(R.id.et_add_patient_card_num)
    private EditText mCardNumEt;
    @ViewInject(R.id.et_add_patient_diagnose)
    private EditText mDiagnoseEt;
    @ViewInject(R.id.et_add_patient_remark)
    private EditText mRemarkEt;

    @ViewInject(R.id.tv_add_patient_city)
    private TextView mCityTv;
    @ViewInject(R.id.tv_add_patient_birth)
    private TextView mBirthTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.img_patient_illness)
    private ImageView mIllnessImg;
    @ViewInject(R.id.img_patient_remark)
    private ImageView mRemarkImg;
    private TimePickerView mTimePickerView;

    //添加请求参数
    private PatientParams mParams;
    private CounsultPatient mPatient;

    private SpeechManager mSpeechManager;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.save_patient_ok);
                finish();
            } else {
                showToast(R.string.save_patient_fail);
            }
        }

        @Override
        protected void requestError(Throwable ex, boolean isOnCallback) {
            closeDialog();
            showToast(R.string.save_patient_fail);
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mSpeechManager = new SpeechManager(mContext);
        mParams = new PatientParams(ConstantValues.HOST + "/Doctor/UpPatient");

        mPatient = getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);

        if (mPatient.sex == 1) {
            mMaleImg.setSelected(true);
        } else if (mPatient.sex == 0) {
            mFemaleImg.setSelected(true);
        }

        mIdcardEt.addTextChangedListener(new TextWatcher() {
            String temp = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (IdCardUtils.isIdcard(temp)) {

                    mAgeEt.setText(IdCardUtils.getAge(temp) + "");

                    mBirthTv.setText(IdCardUtils.getBirth(temp));

                    if (IdCardUtils.getSex(temp) == 1) {
                        mMaleImg.setSelected(true);
                        mFemaleImg.setSelected(false);
                        mParams.sex=1;
                    } else if (IdCardUtils.getSex(temp) == 0) {
                        mMaleImg.setSelected(false);
                        mFemaleImg.setSelected(true);
                        mParams.sex=0;
                    }
                }
            }
        });

        if (mPatient != null) {
            setInfo(mNameEt, mPatient.name);
            setInfo(mAgeEt, mPatient.age+"");
            setInfo(mBirthTv, mPatient.birth);
            setInfo(mCityTv, mPatient.city);
            setInfo(mIdcardEt, mPatient.idCard);
            setInfo(mPhoneEt, mPatient.tel);
            setInfo(mRemarkEt, mPatient.remark);
            setInfo(mCardNumEt, mPatient.medicalCard);
            setInfo(mDiagnoseEt, mPatient.illness);
            setInfo(mClinicEt, mPatient.clinicId);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePatient();
            }
        });
    }

    @Event(R.id.img_patient_illness)
    private void showIllSpeachDlg(View view) {
        mSpeechManager.showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mDiagnoseEt.setText(result.toString());
                    mDiagnoseEt.setSelection(mDiagnoseEt.length());
                }
            }
        });

    }

    @Event(R.id.img_patient_remark)
    private void showRemarkSpeachDlg(View view) {
        mSpeechManager.showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mRemarkEt.setText(result.toString());
                    mRemarkEt.setSelection(mRemarkEt.length());
                }
            }
        });
    }

    private void savePatient() {
        if (TextUtils.isEmpty(getText(mNameEt))) {
            showToast(R.string.enter_name);
            return;
        }
        mParams.id=mPatient.id;
        mParams.name = getText(mNameEt);
        String age = getText(mAgeEt);

        if (age == null || age.equals("")) {
            mParams.age = 0;
        } else if (CommonUtils.isAge(age)) {
            mParams.age = Integer.parseInt(age);
        }
        mParams.tel = getText(mPhoneEt);
        mParams.medicalCard = getText(mCardNumEt);
        mParams.illness = getText(mDiagnoseEt);
        mParams.remark = getText(mRemarkEt);
        mParams.clinicId = getText(mClinicEt);

        mParams.birth=getText(mBirthTv);

        if (getText(mIdcardEt) != null && !getText(mIdcardEt).equals("")) {
            boolean isIdcard  = IdCardUtils.isIdcard(getText(mIdcardEt));
            if (isIdcard) {//错误信息为""
                mParams.idCard = getText(mIdcardEt);
            } else {
                showToast(R.string.edit_idcard_error);
                return;
            }
        }
        mParams.idCard = getText(mIdcardEt);
        BaseManager.updatePatient(mParams, mCallback);
    }

    @Event({R.id.img_add_patient_sex_male, R.id.both_eye_tv})
    private void checkMale(View view) {
        mMaleImg.setSelected(true);
        mFemaleImg.setSelected(false);
        mParams.sex = 1;
    }

    @Event({R.id.img_add_patient_sex_female, R.id.tv_add_patient_female})
    private void checkFemale(View view) {
        mFemaleImg.setSelected(true);
        mMaleImg.setSelected(false);
        mParams.sex = 0;
    }

    @Event(R.id.tv_add_patient_birth)
    private void choseBirth(View view) {
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY, new Date(System.currentTimeMillis()), false, true);
        mTimePickerView.show();
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() >= System.currentTimeMillis()) {
                    showToast("请选择正确的出生日期!");
                    return;
                }

                mBirthTv.setText(CommonUtils.getDateTime(date));
            }
        });
    }

    @Event(R.id.ll_add_patient_city)
    private void choseCity(View view) {
        DialogManager.showCityDlg(mContext, new DialogManager.CityCallback() {
            @Override
            public void chooseCity(String cityTxt) {
                mParams.city = cityTxt;
                mCityTv.setText(cityTxt);
            }
        });
    }

    private void setInfo(TextView mNameTv, String name) {
        if (name == null || name.trim().equals("")) {
            mNameTv.setHint("未填写");
        } else {
            mNameTv.setText(name);
        }
    }


}
