package com.sg.eyedoctor.commUtils.patientReport.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.adapter.AlreadyReportAdapter;
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
 * 已报到
 */
@ContentView(R.layout.fragment_patient_report)
public class AlreadyReportFragment extends BaseFragment {
    @ViewInject(R.id.patient_report_lv)
    private ListView mPatientLv;
    private AlreadyReportAdapter mPatientReportAdapter;
    private ArrayList<PDValidate> mReports;

    private NetCallback mNetCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                Type objectType = new TypeToken<BaseArrayResp<PDValidate>>() {
                }.getType();
                BaseArrayResp<PDValidate> res = new Gson().fromJson(result, objectType);

                mReports = res.value;
                Collections.sort(mReports, new FreeConsultSort());
                mPatientReportAdapter.setData(mReports);

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
        mPatientReportAdapter = new AlreadyReportAdapter(getActivity(), mReports, R.layout.item_patient_report);
        mPatientLv.setAdapter(mPatientReportAdapter);
        UiUtils.setEmptyText(getActivity(), mPatientLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {
        mPatientLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                    return;
                }
                PDValidate pdValidate = mReports.get(position);
//                if (pdValidate.newMessage!=0) {
//                    BaseManager.setQuestionMessageIsRead(mPatient.questionId, mPatient.patientIM, "d"+mPatient.doctorId, mReadCallback);
//                }
                P2PMessageActivity.start(getActivity(), pdValidate.patientIM, SessionHelper.getReportCustomization(pdValidate), pdValidate);
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isVisible() ) {
            queryData();
        }
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
        BaseManager.getPDFriendList("",mNetCallback);
    }

    class FreeConsultSort implements Comparator {

        @Override
        public int compare(Object time1, Object time2) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/M/dd HH:mm:ss");
            Date date1 = null;
            Date date2 = null;
            try {
                date1=format.parse(((PDValidate) time1).latestDate);
                date2=format.parse(((PDValidate) time2).latestDate);
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
