package com.sg.eyedoctor.commUtils.patientReport.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.adapter.AlreadyReportAdapter;
import com.sg.eyedoctor.commUtils.patientReport.adapter.PatientReportAdapter;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.common.view.SearchLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 搜索界面
 */
@ContentView(R.layout.activity_search_report)
public class SearchPatientReportActivity extends BaseActivity implements SearchLayout.SearchCallback, PatientReportAdapter.AgreeCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;

    @ViewInject(R.id.already_lv)
    private NoScrollListView mAlreadyLv;
    private AlreadyReportAdapter mAlreadyAdapter;
    private ArrayList<PDValidate> mPDValidates=new ArrayList<>();

    @ViewInject(R.id.report_lv)
    private NoScrollListView mReportLv;
    private PatientReportAdapter mPatientAdapter;
    private ArrayList<PDValidate> mReports=new ArrayList<>();

    private String mTempString="";

    private NetCallback mAlreadyCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PDValidate>>() {
                }.getType();
                BaseArrayResp<PDValidate> res = new Gson().fromJson(result, objectType);
                mPDValidates.clear();
                mPDValidates = res.value;
                mAlreadyAdapter.setData(mPDValidates);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mPatientCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PDValidate>>() {
                }.getType();
                BaseArrayResp<PDValidate> res = new Gson().fromJson(result, objectType);
                mReports.clear();
                mReports = res.value;
                mPatientAdapter.setData(mReports);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mAgreeCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                BaseManager.getPDFriendList(mTempString, mAlreadyCallback);
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

        mAlreadyAdapter=new AlreadyReportAdapter(mContext, mPDValidates,0);
        mAlreadyLv.setAdapter(mAlreadyAdapter);

        mPatientAdapter=new PatientReportAdapter(mContext,mReports,0,this);
        mReportLv.setAdapter(mPatientAdapter);

        mEmptyTv=UiUtils.setEmptyText(mContext,mReportLv,"无记录");
        mSearchSl.setCallback(this,mEmptyTv);
    }

    @Override
    protected void initListener() {


        mReportLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                    return;
                }
                PDValidate report=mReports.get(position);

                if(report.validateState.equals("1")){
                    P2PMessageActivity.start(mContext, report.patientIM, SessionHelper.getReportCustomization(report), report);
                }else{
                    Intent intent=new Intent(mContext, ReportNoteActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA,report);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.patient_report);
    }

    @Override
    public void fillData(String s) {
        mTempString=s;
        showdialog();
        BaseManager.getPDValidateList("",mTempString, mPatientCallback);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void agree(int position) {
        PDValidate pdValidate = mReports.get(position);
        showdialog();
        BaseManager.pdFriendUpdate(pdValidate.patientId, pdValidate.validateId, mAgreeCallback);
    }

    @Override
    public void clickHead(int position) {

    }
}
