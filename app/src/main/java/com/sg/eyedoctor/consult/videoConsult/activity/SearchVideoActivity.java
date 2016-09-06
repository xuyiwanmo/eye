package com.sg.eyedoctor.consult.videoConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.avchat.activity.AVChatActivity;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.NetworkUtil;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.consult.advice.activity.ConsultDetailActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticEditActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticShowActivity;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;
import com.sg.eyedoctor.consult.videoConsult.adapter.VideoAddAdapter;
import com.sg.eyedoctor.consult.videoConsult.adapter.VideoConsultAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 视频咨询搜索界面
 */
@ContentView(R.layout.activity_search_video)
public class SearchVideoActivity extends BaseActivity implements SearchLayout.SearchCallback, VideoAddAdapter.AddCallback, VideoConsultAdapter.DailCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;
    @ViewInject(R.id.search_add_lv)
    private ListView mSearchAddLv;
    @ViewInject(R.id.empty_tv)
    private TextView mEmptyTv;

    private VideoAddAdapter mAddAdapter;
    private VideoConsultAdapter mConsultAdapter;
    private ArrayList<Patient> mPatients=new ArrayList<>();
    private ArrayList<Patient> mConsultPatients=new ArrayList<>();

    private String mTempString="";
    private String mXtrId;
    private Patient mPatient;
    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);

                mConsultPatients = res.value;
                if (mPatients != null) {
                    mConsultAdapter.setData(mConsultPatients);
                } else {
                    showToast(R.string.query_empty);
                }
                BaseManager.getServiceXtrRecordList(mXtrId, mTempString,mDataCallback);
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mDataCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);
                mPatients = res.value;
                mAddAdapter.setData(mPatients);
                if(mPatients.size()==0&&mConsultPatients.size()==0){
                    mSearchLv.setVisibility(View.GONE);
                    mSearchLv.setVisibility(View.GONE);
                    mEmptyTv.setVisibility(View.VISIBLE);
                }else{
                    mSearchLv.setVisibility(View.VISIBLE);
                    mSearchLv.setVisibility(View.VISIBLE);
                    mEmptyTv.setVisibility(View.GONE);
                }
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
                BaseManager.getServiceXtrRecordList(mXtrId, mTempString,mDataCallback);
            }else {
                closeDialog();
                showToast(R.string.post_fail);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mConsultCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);
                mConsultPatients = res.value;
                if (mPatients != null) {
                    mConsultAdapter.setData(mConsultPatients);
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


    @Override
    protected void initView() {
        mXtrId = getIntent().getStringExtra(ConstantValues.KEY_XTR_ID);
        mSearchSl.setCallback(this);
        mAddAdapter=new VideoAddAdapter(mContext,mPatients,0,this);
        mConsultAdapter=new VideoConsultAdapter(mContext,mConsultPatients,0,this);
        mSearchAddLv.setAdapter(mAddAdapter);
        mSearchLv.setAdapter(mConsultAdapter);

    }

    @Override
    protected void initListener() {
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPatient = mConsultPatients.get(position);
                Intent intent2 = new Intent(mContext, ConsultDetailActivity.class);
                intent2.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                intent2.putExtra(ConstantValues.KEY_TYPE, 1);
                startActivity(intent2);
            }
        });

        mSearchAddLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast("加号查询的结果无法操作!");
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.video_consult);
    }

    @Override
    public void fillData(String s) {
        mTempString =s;
        showdialog();
        BaseManager.queryConsult("", "3", mTempString, mCallback);
    }

    @Override
    public void cancel() {
        mPatients.clear();
        mConsultPatients.clear();
        mAddAdapter.setData(mPatients);
        mConsultAdapter.setData(mConsultPatients);
        mEmptyTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void agreeOrRefuse(int position, int type) {
        Patient record=mPatients.get(position);
        showdialog();
        BaseManager.serviceXtrRecordModify(record.id, type, mAgreeCallback);
    }
    @Override
    public void dail(int position) {
        Patient patient = mConsultPatients.get(position);

        if (NetworkUtil.isNetAvailable(mContext)) {
            AVChatActivity.start(mContext, patient.patientIM, AVChatType.VIDEO.getValue(), AVChatActivity.FROM_INTERNAL, patient.id);
        } else {
            showToast(R.string.network_is_not_available);
        }

    }

    @Override
    public void editDiagnotice(int position) {
        Patient patient = mConsultPatients.get(position);
        if (patient.state == 6) {
            return;
        }
        if (patient.writeState.equals("0")) {//未填写
            Intent intent = new Intent(mContext, DiagnosticEditActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);

        } else if (patient.writeState.equals("1")) {
            Intent intent = new Intent(mContext, DiagnosticShowActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_END, true);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mTempString.equals("")){
            return;
        }
        showdialog();
        BaseManager.queryConsult("", "3", mTempString, mCallback);
    }
}
