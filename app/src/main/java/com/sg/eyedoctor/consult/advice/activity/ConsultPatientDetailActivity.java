package com.sg.eyedoctor.consult.advice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.activity.MedicineDetailActivity;
import com.sg.eyedoctor.chartFile.activity.NewCaseShowActivity;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.consult.advice.adapter.ConsultPatientDetailAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultationList;
import com.sg.eyedoctor.consult.textConsult.bean.CounsultPatient;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 患者详情
 */
@ContentView(R.layout.activity_consult_patient_detail)
public class ConsultPatientDetailActivity extends BaseActivity {

    @ViewInject(R.id.img_patient_center_head)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.tv_patient_center_name)
    private TextView mNameTv;
    @ViewInject(R.id.tv_patient_center_sex)
    private TextView mSexTv;
    @ViewInject(R.id.tv_patient_center_age)
    private TextView mAgeTv;
    @ViewInject(R.id.tv_patient_idcard)
    private TextView mIdcardTv;
    @ViewInject(R.id.rl_patient_detail)
    private RelativeLayout mDetailRl;
    @ViewInject(R.id.lv_patient_center_record)
    private NoScrollListView mRecordLv;
    @ViewInject(R.id.rl)
    private RelativeLayout mRl;
    @ViewInject(R.id.tv_patient_center_empty)
    private TextView mEmptyTv;

    private Patient mPatient;

    private CounsultPatient mCounsultPatient;
    private ArrayList<ConsultationList> mConsultationLists=new ArrayList<>();
    private ConsultPatientDetailAdapter mDetailAdapter;

    private NetCallback mCallback = new NetCallback(this) {
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

        mPatient = getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);
        mDetailAdapter=new ConsultPatientDetailAdapter(this,mConsultationLists,0);
        mRecordLv.setAdapter(mDetailAdapter);

        showdialog();
        BaseManager.getVasConsultation(mPatient.patientId, mCallback);

    }

    @Override
    protected void initListener() {
        mRecordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultationList record = mConsultationLists.get(position);

                if (record.type == 2) {
                    Intent intent = new Intent(mContext, MedicineDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_RECORD, record);
                    intent.putExtra(ConstantValues.KEY_DATA, mPatient.patientName);
                    startActivity(intent);
                }
                else if (record.type==3){
                    Intent intent = new Intent(mContext, NewCaseShowActivity.class);
                    intent.putExtra(ConstantValues.KEY_PATIENT, record);
                    startActivity(intent);
                }else if(record.type==1){
                    Intent intent2 = new Intent(mContext, DiagnosticShowActivity.class);
                    intent2.putExtra(ConstantValues.KEY_ORDER_ID, record.orderId);
                    intent2.putExtra(ConstantValues.KEY_PATIENT, mPatient);
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

    }

    private void updateView() {
        setTextView(mSexTv,mPatient.sex.equals("1")?"男":"女");
        setTextView(mAgeTv,mPatient.age+"岁");
        setTextView(mIdcardTv,mCounsultPatient.idCard);
        setTextView(mNameTv,mCounsultPatient.name);

        mConsultationLists=mCounsultPatient.consultationList;
        mDetailAdapter.setData(mConsultationLists);
    }

}
