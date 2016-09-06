package com.sg.eyedoctor.chartFile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.textConsult.bean.CounsultPatient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 病人详情
 */
@ContentView(R.layout.activity_patient_detail)
public class PatientDetailActivity extends BaseActivity {
    @ViewInject(R.id.tv_add_patient_name)
    private TextView mNameTv;
    @ViewInject(R.id.tv_add_patient_idcard)
    private TextView mIdcardTv;
    @ViewInject(R.id.img_add_patient_sex_male)
    private ImageView mMaleImg;
    @ViewInject(R.id.img_add_patient_sex_female)
    private ImageView mFemaleImg;
    @ViewInject(R.id.tv_add_patient_birth)
    private TextView mBirthTv;

    @ViewInject(R.id.tv_add_patient_age)
    private TextView mAgeTv;
    @ViewInject(R.id.tv_add_patient_phone)
    private TextView mPhoneTv;
    @ViewInject(R.id.tv_add_patient_city)
    private TextView mCityTv;

    @ViewInject(R.id.tv_add_patient_card_num)
    private TextView mCardNum;
    @ViewInject(R.id.tv_add_patient_clinic_num)
    private TextView mClinicNum;
    @ViewInject(R.id.tv_add_patient_diagnose)
    private TextView mDiagnoseTv;
    @ViewInject(R.id.tv_add_patient_remark)
    private TextView mRemarkTv;

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private CounsultPatient mPatient;

    @Override
    protected void initView() {
        mPatient = getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);

        if (mPatient != null) {
            LogUtils.i(mPatient.toString());
            setInfo(mNameTv, mPatient.name);

            setInfo(mAgeTv, CommonUtils.dateToAge(mPatient.birth) + "");
            if (mPatient.birth != null) {
                setInfo(mBirthTv, mPatient.birth.split(" ")[0]);
            }
            setInfo(mCityTv, mPatient.city);
            setInfo(mIdcardTv, mPatient.idCard);
            setInfo(mPhoneTv, mPatient.tel);
            setInfo(mRemarkTv, mPatient.remark);
            setInfo(mCardNum, mPatient.medicalCard);
            setInfo(mDiagnoseTv, mPatient.illness);
            setInfo(mClinicNum, mPatient.clinicId);

            mNameTv.setText(mPatient.name);
            if (mPatient.sex == 1) {
                mMaleImg.setSelected(true);
                mFemaleImg.setSelected(false);
            } else if (mPatient.sex == 0) {
                mMaleImg.setSelected(false);
                mFemaleImg.setSelected(true);
            }

        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDetailActivity.this, PatientDetailEditActivity.class);
                intent.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
            }
        });
    }

    private void setInfo(TextView mNameTv, String name) {
        if (name == null || name.trim().equals("") || name.equals("null")) {
            mNameTv.setText(R.string.edit_empty);
        } else {
            mNameTv.setText(name);
        }
    }


}
