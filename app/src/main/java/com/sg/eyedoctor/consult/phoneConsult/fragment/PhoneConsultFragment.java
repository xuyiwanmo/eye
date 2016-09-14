package com.sg.eyedoctor.consult.phoneConsult.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.consult.advice.activity.ConsultDetailActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticEditActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticShowActivity;
import com.sg.eyedoctor.consult.phoneConsult.adapter.PhoneConsultFragmentAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**

 */
@ContentView(R.layout.fragment_phone_consult)
public class PhoneConsultFragment extends BaseFragment implements PhoneConsultFragmentAdapter.DailCallback {
    @ViewInject(R.id.lv_couslt)
    private ListView mCousltLv;

    private ArrayList<Patient> mPatients = new ArrayList<>();
    private PhoneConsultFragmentAdapter mConsultAdapter;
    private Patient mPatient;

    private int mType = 1;

    private boolean mCanDail=true;

    private NetCallback mDataCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<Patient>>(){}.getType();
                BaseArrayResp<Patient> res=new Gson().fromJson(result, objectType);
                ArrayList<Patient> mPatients = res.value;
                Collections.sort(mPatients, new PhoneConsultSort());
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


    private NetCallback mCallback = new NetCallback(getActivity()) {
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

    public PhoneConsultFragment(int i) {
        mType = i;
    }

    public PhoneConsultFragment() {
    }


    @Override
    protected void initView() {

        mPatients = new ArrayList<>();
        mConsultAdapter = new PhoneConsultFragmentAdapter(getActivity(), mPatients, 0, this);
        mCousltLv.setAdapter(mConsultAdapter);

        UiUtils.setEmptyText(getActivity(), mCousltLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {
        mCousltLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //**************点击对应的联系人   跳转到  聊天界面

                mPatient = mPatients.get(position);

                Intent intent2 = new Intent(getActivity(), ConsultDetailActivity.class);
                intent2.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                intent2.putExtra(ConstantValues.KEY_TYPE, 1);
                startActivity(intent2);
            }
        });
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
        BaseManager.callPhone("d"+mPatient.doctorId,mPatient.id, mPatient.doctorTel, mPatient.patientTel, mCallback);

        mCousltLv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPatient.authTime == 0) {
                    mPatient.authTime = 60;
                    mPatient.clickable = true;
                    mCanDail = true;

                } else if (!mPatient.clickable) {
                    mPatient.authTime--;
                }
                mConsultAdapter.setData(mPatients);
                mCousltLv.postDelayed(this, 1000);
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
            Intent intent = new Intent(getActivity(), DiagnosticEditActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);

        }else if(patient.writeState.equals("1")){
            Intent intent = new Intent(getActivity(), DiagnosticShowActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_END, true);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);
        }else {

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断该Fragment是否已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() ) {
            queryData(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint() ) {
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }

    private void queryData() {
        showLoginDlg();
        BaseManager.queryConsult(mType + "", "2", "", mDataCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            queryData();
        }
    }

    class PhoneConsultSort implements Comparator {
        private String getTime(Patient patient1){
            String t1="";
            if (patient1.state == 1) {//待通话
                t1=patient1.dealTime;
            } else {//已完成

                if (patient1.state == 6) {//未拨通  退款状态
                    t1=CommonUtils.add2Hours(patient1.dealTime);
                }else{
                    t1=patient1.modifyDate;
                }
            }
            return t1;
        }
        @Override
        public int compare(Object time1, Object time2) {
            boolean flag = false;
            Patient patient1=(Patient) time1;
            Patient patient2=(Patient) time2;
            String t1=getTime(patient1);
            String t2=getTime(patient2);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            if(t1==null||t2==null){
                return -1;
            }
            try {
                Date d1 = format.parse(t1);
                Date d2 = format.parse(t2);
                if (d1.getTime() < d2.getTime()) {
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (flag) {
                return 1;
            } else {
                return -1;
            }
        }
    }




}
