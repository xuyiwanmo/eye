package com.sg.eyedoctor.commUtils.patientReport.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.activity.ReportNoteActivity;
import com.sg.eyedoctor.commUtils.patientReport.adapter.PatientReportAdapter;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * 申请报到
 */
@ContentView(R.layout.fragment_patient_report)
public class PatientReportFragment extends BaseFragment implements PatientReportAdapter.AgreeCallback {
    @ViewInject(R.id.patient_report_lv)
    private ListView mPatientLv;
    private PatientReportAdapter mPatientReportAdapter;
    private ArrayList<PDValidate> mReports;

    private NetCallback mNetCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PDValidate>>() {
                }.getType();
                BaseArrayResp<PDValidate> res = new Gson().fromJson(result, objectType);

                mReports = res.value;
                Collections.sort(mReports, new FreeConsultSort());
                mPatientReportAdapter.setData(mReports);

            }else{
                showToast("获取资料失败!");
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mAgreeCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                BaseManager.getPDValidateList(ConstantValues.REPORT_TYPE_ACCEPT + "", "", mNetCallback);
            } else {
                showToast(R.string.error_load);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mReports = new ArrayList<>();
        mPatientReportAdapter = new PatientReportAdapter(getActivity(), mReports, R.layout.item_patient_report, this);
        mPatientLv.setAdapter(mPatientReportAdapter);
        UiUtils.setEmptyText(getActivity(), mPatientLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {
        mPatientLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PDValidate report = mReports.get(position);
                Intent intent = new Intent(getActivity(), ReportNoteActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA, report);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isVisible()) {
            queryData();
        }
    }

    @Override
    public void agree(int position) {

        PDValidate pdValidate = mReports.get(position);
        showLoginDlg();
        BaseManager.pdFriendUpdate(pdValidate.patientId, pdValidate.validateId, mAgreeCallback);
    }

    @Override
    public void clickHead(int position) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint()) {
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }

    private void queryData() {
        showLoginDlg();
        BaseManager.getPDValidateList(ConstantValues.REPORT_TYPE_ACCEPT + "", "", mNetCallback);
    }

    class FreeConsultSort implements Comparator {

        @Override
        public int compare(Object time1, Object time2) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
            Date date1 = null;
            Date date2 = null;
            try {
                date1=format.parse(((PDValidate) time1).treatmentDate.replace("-","/"));
                date2=format.parse(((PDValidate) time2).treatmentDate.replace("-","/"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long flag =date1.compareTo(date2);
            if (flag > 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
