package com.sg.eyedoctor.commUtils.addPatient.activity;

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
import com.sg.eyedoctor.commUtils.outPatientRecord.bean.AppointPatient;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.manager.SpeechManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.IdCardUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;

/**
 * 新增患者
 */
@ContentView(R.layout.activity_add_patient)
public class AddPatientActivity extends BaseActivity {
    @ViewInject(R.id.img_illness_voice)
    private ImageView mIllVoiceImg;
    @ViewInject(R.id.img_remark_voice)
    private ImageView mRemarkImg;
    @ViewInject(R.id.et_add_patient_idcard)
    private EditText mIdcardEt;
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
    @ViewInject(R.id.age_et)
    private EditText mPhoneEt;
    @ViewInject(R.id.ll_add_patient_city)
    private LinearLayout mCityLl;
    @ViewInject(R.id.et_add_patient_card_num)
    private EditText mCaseNumEt;
    @ViewInject(R.id.et_add_patient_clinic_num)
    private EditText mClinicNumEt;  //门诊号码
    @ViewInject(R.id.et_add_patient_diagnose)
    private EditText mDiagnoseEt;
    @ViewInject(R.id.et_add_patient_remark)
    private EditText mRemarkEt;
    @ViewInject(R.id.et_add_patient_age)
    private EditText mAgeEt;
    @ViewInject(R.id.tv_add_patient_city)
    private TextView mCityTv;
    @ViewInject(R.id.tv_add_patient_birth)
    private TextView mBirthTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    //添加请求参数
    private PatientParams mParams;
    private AppointPatient mPatient;
    private DialogManager mDlgManager;
    private SpeechManager mSpeechManager;
    private TimePickerView mTimePickerView;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.save_patient_ok);
                AppManager.getAppManager().finishActivity();
            } else {
                String str=CommonUtils.getMsg(result);
                showToast(str);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {
        mDlgManager = new DialogManager(this);
        mSpeechManager = new SpeechManager(this);
        mContext = this;
        mParams = new PatientParams(ConstantValues.HOST + "/Doctor/AddPatient");
        mParams.sex = 1;//默认性别为男
        mMaleImg.setSelected(true);
        mPatient = (AppointPatient) getIntent().getSerializableExtra(ConstantValues.KEY_PATIENT);
        mIdcardEt.addTextChangedListener(new TextWatcher() {
            String temp="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   temp=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(IdCardUtils.isIdcard(temp)){

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
            mNameEt.setText(mPatient.patientName);
            setTextView(mIdcardEt, mPatient.idCard);
            if (mPatient.idCard != null && IdCardUtils.isIdcard(mPatient.idCard)) {
                mAgeEt.setText(IdCardUtils.getAge(mPatient.idCard) + "");
                mBirthTv.setText(IdCardUtils.getBirth(mPatient.idCard));

                if (IdCardUtils.getSex(mPatient.idCard) == 1) {
                    mMaleImg.setSelected(true);
                    mFemaleImg.setSelected(false);
                    mParams.sex=1;
                } else if (IdCardUtils.getSex(mPatient.idCard) == 0) {
                    mMaleImg.setSelected(false);
                    mFemaleImg.setSelected(true);
                    mParams.sex=0;
                }
            }
            setTextView(mCaseNumEt, mPatient.medicalCard);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpeechManager.destroy();
    }

    @Event(R.id.img_illness_voice)
    private void printIllness(View view) {
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

    @Event(R.id.img_remark_voice)
    private void printRemark(View view) {
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
        mParams.sex = 2;
    }

    @Event(R.id.tv_add_patient_birth)
    private void choseBirth(View view) {
        KeyBoardUtils.hideKeyboard(mNameEt);
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY, new Date(System.currentTimeMillis()), false, true);
        mTimePickerView.show();
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() >= System.currentTimeMillis()) {
                    showToast("请选择正确的出生日期!");
                    return;
                }
                mParams.birth = CommonUtils.getDateTime(date);
                mBirthTv.setText(CommonUtils.getDateTime(date));
            }
        });

    }

    @Event(R.id.ll_add_patient_city)
    private void choseCity(View view) {
        mDlgManager.showCityDlg(mContext, new DialogManager.CityCallback() {
            @Override
            public void chooseCity(String cityTxt) {
                mParams.city = cityTxt;
                mCityTv.setText(cityTxt);
            }
        });
    }

    private void savePatient() {
        if (TextUtils.isEmpty(getText(mNameEt))) {
            showToast(R.string.enter_name);
            return;
        }

        mParams.name = getText(mNameEt);
        if (getText(mAgeEt).equals("")) {
            mParams.age = 0;
        } else if (CommonUtils.isNumeric(getText(mAgeEt))) {
            mParams.age = Integer.parseInt(getText(mAgeEt));
        } else {
            showToast("年龄输入错误,请重新输入!");
            return;
        }

        boolean isIdcard = true;
        if (getText(mIdcardEt) != null && !getText(mIdcardEt).equals("")) {
            isIdcard = IdCardUtils.isIdcard(getText(mIdcardEt));
            if (isIdcard) {//错误信息为""
                mParams.idCard = getText(mIdcardEt);
            } else {
                showToast(R.string.edit_idcard_error);
                return;
            }
        }

        mParams.tel = getText(mPhoneEt);
        mParams.medicalCard = getText(mCaseNumEt);
        mParams.illness = getText(mDiagnoseEt);
        mParams.remark = getText(mRemarkEt);
        mParams.birth = getText(mBirthTv);
        mParams.clinicId = getText(mClinicNumEt);

        BaseManager.addPatient(mParams, mCallback);
    }

}
