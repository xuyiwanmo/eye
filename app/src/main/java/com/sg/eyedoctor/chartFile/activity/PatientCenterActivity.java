package com.sg.eyedoctor.chartFile.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.MedicalRecord;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticShowActivity;
import com.sg.eyedoctor.consult.advice.adapter.ConsultPatientDetailAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultationList;
import com.sg.eyedoctor.consult.textConsult.bean.CounsultPatient;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 患者中心
 */
@ContentView(R.layout.activity_patient_center)
public class PatientCenterActivity extends BaseActivity {
    @ViewInject(R.id.tv_patient_center_name)
    private TextView mNameTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.tv_patient_center_sex)
    private TextView mSexTv;
    @ViewInject(R.id.tv_patient_center_age)
    private TextView mAgeTv;
    @ViewInject(R.id.tv_patient_center_phone)
    private TextView mPhoneTv;
    @ViewInject(R.id.tv_patient_center_city)
    private TextView mCityTv;
    @ViewInject(R.id.tv_patient_center_idcrd)
    private TextView mIdcardTv;
    @ViewInject(R.id.tv_patient_center_empty)
    private TextView mEmptyTv;
    @ViewInject(R.id.rl_patient_detail)
    private RelativeLayout mDetailTv;
    @ViewInject(R.id.bottom_rl)
    private RelativeLayout mBottomRl;
    @ViewInject(R.id.add_case_tv)
    private TextView mAddCaseTv;
    @ViewInject(R.id.lv_patient_center_record)
    private NoScrollListView mPatientCenterLv;

    private PatientByGroup mPatient;

    private int isFocus = 0; //1:关注|0:取消关注
    private int mType = 0;  //0无操作   1:点击跳转

    private CounsultPatient mCounsultPatient;
    private ArrayList<ConsultationList> mConsultationLists = new ArrayList<>();
    private ConsultPatientDetailAdapter mDetailAdapter;

    private NetCallback mStoreCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                if (isFocus == 1) {
                    isFocus = 0;
                    mActionbar.getRightBtnImg().setSelected(false);
                } else {
                    isFocus = 1;
                    mActionbar.getRightBtnImg().setSelected(true);
                }
            }else{
                showToast(R.string.get_data_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    private NetCallback mCenterCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<CounsultPatient>>() {
                }.getType();
                BaseResp<CounsultPatient> res = new Gson().fromJson(result, objectType);
                mCounsultPatient = res.value;
                updateView();

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {

        mPatient = (PatientByGroup) getIntent().getSerializableExtra(ConstantValues.KEY_PATIENT);
        mType = getIntent().getIntExtra(ConstantValues.KEY_TYPE, 0);

        mDetailAdapter = new ConsultPatientDetailAdapter(this, mConsultationLists, 0);
        mPatientCenterLv.setAdapter(mDetailAdapter);

        setEmptyText(mContext,mPatientCenterLv,getString(R.string.query_detail_empty));
        if (mType == ConstantValues.PATIENT_CENTER_CHOOSE) {
            mAddCaseTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getVasConsultation(mPatient.id, mCenterCallback);
    }

    @Override
    protected void initListener() {
        mPatientCenterLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultationList record = mConsultationLists.get(position);
                if (mType == ConstantValues.PATIENT_CENTER_CHOOSE) {
                    MedicalRecord medicine = new MedicalRecord();
                    Intent intent = new Intent();
                    medicine.age = CommonUtils.dateToAge(mPatient.birth);
                    medicine.name = mCounsultPatient.name;
                    medicine.diagnosis = record.description;
                    medicine.sex = mPatient.sex;
                    medicine.illness = record.illDetail;
                    medicine.picList = record.picList;

                    intent.putExtra(ConstantValues.KEY_RECORD, medicine);
                    setResult(RESULT_OK, intent);
                    AppManager.getAppManager().finishActivity();
                    return;
                }
                if (record.type == ConstantValues.PATIENT_CENTER_HOSPITAL) {
                    Intent intent = new Intent(PatientCenterActivity.this, MedicineDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_RECORD, record);
                    intent.putExtra(ConstantValues.KEY_DATA, mPatient.name);
                    startActivity(intent);
                } else if (record.type == ConstantValues.PATIENT_CENTER_NEW_ADD) {
                    Intent intent = new Intent(PatientCenterActivity.this, NewCaseShowActivity.class);
                    intent.putExtra(ConstantValues.KEY_PATIENT, record);
                    startActivity(intent);
                } else if (record.type == ConstantValues.PATIENT_CENTER_PLATFORM) {
                    Patient patient=new Patient();
                    patient.patientName=mCounsultPatient.name;
                    patient.sex=mCounsultPatient.sex+"";
                    patient.age=mCounsultPatient.age+"";
                    Intent intent2 = new Intent(mContext, DiagnosticShowActivity.class);
                    intent2.putExtra(ConstantValues.KEY_ORDER_ID, record.orderId);
                    intent2.putExtra(ConstantValues.KEY_PATIENT, patient);
                    intent2.putExtra(ConstantValues.KEY_IS_RECORD, true);
                    intent2.putExtra(ConstantValues.KEY_END, true);
                    intent2.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
                    startActivity(intent2);
                }
            }
        });

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightBtnImg(R.drawable.selector_chart_store, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = 0;
                if (isFocus == 1) {
                    temp = 0;
                } else {
                    temp = 1;
                }

                showdialog();
                BaseManager.patientSetFocus(mCounsultPatient.id, temp, mStoreCallback);
            }
        });
    }

    @Event(R.id.rl_patient_detail)
    private void patientDetail(View view) {
        Intent intent = new Intent(PatientCenterActivity.this, PatientDetailActivity.class);
        intent.putExtra(ConstantValues.KEY_PATIENT, mCounsultPatient);
        startActivity(intent);
    }

    @Event(R.id.add_case_tv)
    private void addNewCase(View view) {

        Intent intent = new Intent(PatientCenterActivity.this, AddNewCaseActivity.class);
        intent.putExtra(ConstantValues.KEY_PATIENT, mPatient);
        startActivity(intent);
    }

    private void updateView() {

        mNameTv.setText(mCounsultPatient.name);
        setTextView(mPhoneTv, mCounsultPatient.tel, "未填写");

        if (mCounsultPatient.sex == 1) {
            mSexTv.setText("男");
        } else if (mCounsultPatient.sex == 0) {
            mSexTv.setText("女");
        }
        setTextView(mCityTv, mCounsultPatient.city, "未填写");
        if (mCounsultPatient.age != 0) {
            setTextView(mAgeTv, mCounsultPatient.age + "岁", "");
        }

        if (!TextUtils.isEmpty(mCounsultPatient.idCard)) {
            mIdcardTv.setText(mCounsultPatient.idCard);
        } else {
            mIdcardTv.setText("未填写");
        }

        if (mCounsultPatient.isFocus.equals("1")) {
            isFocus = 1;
            mActionbar.getRightBtnImg().setSelected(true);
        } else {
            isFocus = 0;
            mActionbar.getRightBtnImg().setSelected(false);
        }

        //如果未认证   则屏蔽医院信息
        mConsultationLists = mCounsultPatient.consultationList;
        LogUtils.i("isAuth; " + mDoctor.state);
        if(!mIsAuth){
            for (ConsultationList consultationList : mConsultationLists) {
                if(consultationList.type==ConstantValues.PATIENT_CENTER_HOSPITAL||consultationList.type==ConstantValues.PATIENT_CENTER_PLATFORM){
                    mConsultationLists.remove(consultationList);
                }
            }
        }

        if (mConsultationLists!=null&&mConsultationLists.size() != 0) {
            mBottomRl.setVisibility(View.VISIBLE);
        }
        mDetailAdapter.setData(mConsultationLists);
    }

    //为listview设置空文本
    public static void setEmptyText(Context context,ListView list,String emptyText){
        LinearLayout.LayoutParams lytp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lytp.gravity = Gravity.CENTER;

        TextView emptyView = new TextView(context);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setLayoutParams(lytp);
        emptyView.setHeight(400);
        emptyView.setText(emptyText);
        emptyView.setTextSize(18);
        emptyView.setTextColor(context.getResources().getColor(R.color.text_body_weak));
        emptyView.setVisibility(View.GONE);

        ((ViewGroup)list.getParent()).addView(emptyView);
        list.setEmptyView(emptyView);
    }
}
