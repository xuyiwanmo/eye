package com.sg.eyedoctor.consult.phoneConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.consult.advice.activity.ConsultDetailActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticEditActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticShowActivity;
import com.sg.eyedoctor.consult.phoneConsult.adapter.PhoneConsultFragmentAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 电话咨询搜索界面
 */
@ContentView(R.layout.activity_search_text)
public class SearchPhoneActivity extends BaseActivity implements SearchLayout.SearchCallback, PhoneConsultFragmentAdapter.DailCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;

    private ArrayList<Patient> mPatients = new ArrayList<>();
    private PhoneConsultFragmentAdapter mConsultAdapter;
    private Patient mPatient;
    private boolean mCanDail=true;

    private String mTempString="";
    private NetCallback mDataCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<Patient>>(){}.getType();
                BaseArrayResp<Patient> res=new Gson().fromJson(result, objectType);
                mPatients = res.value;
                if (mPatients != null) {
                    setData(mPatients);
                } else {
                    showToast(R.string.query_empty);
                }
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.already_post_system);
            } else {
                showToast(R.string.error_contact_account);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {

        mConsultAdapter = new PhoneConsultFragmentAdapter(mContext, mPatients, 0, this);
        mSearchLv.setAdapter(mConsultAdapter);
        mEmptyTv=UiUtils.setEmptyText(mContext,mSearchLv,"无记录");
        mSearchSl.setCallback(this,mEmptyTv);
    }

    @Override
    protected void initListener() {
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //**************点击对应的联系人   跳转到  聊天界面

                mPatient = mPatients.get(position);

                Intent intent2 = new Intent(mContext, ConsultDetailActivity.class);
                intent2.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                intent2.putExtra(ConstantValues.KEY_TYPE, 1);
                startActivity(intent2);
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.phone_consult);
    }

    @Override
    public void fillData(String s) {
        mTempString=s;
        showdialog();
        BaseManager.queryConsult("", "2", mTempString, mDataCallback);
    }

    @Override
    public void cancel() {
        mPatients.clear();
        mConsultAdapter.setData(mPatients);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTempString.equals("")) {
            return;
        }
        showdialog();
        BaseManager.queryConsult("", "2", mTempString, mDataCallback);
    }

    public void setData(ArrayList<Patient> patients) {
        for (Patient patient : patients) {
            patient.clickable = true;
            patient.authTime = 60;
        }
        this.mPatients = patients;
        mConsultAdapter.setData(patients);
    }

    @Override
    public void dail(int position) {
        if(!mCanDail){
            showToast(R.string.can_not_dail_togerther);
            return;
        }
        mCanDail=false;
        mPatient = mPatients.get(position);
        mPatient.clickable = false;
        BaseManager.callPhone("d" + mPatient.doctorId, mPatient.id, mPatient.doctorTel, mPatient.patientTel, mCallback);

        mSearchLv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPatient.authTime == 0) {
                    mPatient.authTime = 60;
                    mPatient.clickable = true;
                    mCanDail=true;

                } else if (!mPatient.clickable) {
                    mPatient.authTime--;
                }
                mConsultAdapter.setData(mPatients);
                mSearchLv.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void editDiagnotice(int position) {

        Patient patient=mPatients.get(position);
        if(patient.state==6){
            return;
        }
        if(patient.writeState.equals("0")){//未填写
            Intent intent = new Intent(mContext, DiagnosticEditActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);

        }else if(patient.writeState.equals("1")){
            Intent intent = new Intent(mContext, DiagnosticShowActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_END, true);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);
        }else {

        }
    }
}
