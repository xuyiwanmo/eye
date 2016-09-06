package com.sg.eyedoctor.helpUtils.electronicRecords.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.activity.WebViewActivity;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.helpUtils.electronicRecords.adapter.CaseDownloadAdapter;
import com.sg.eyedoctor.helpUtils.electronicRecords.bean.ElectricHospital;
import com.sg.eyedoctor.helpUtils.electronicRecords.bean.PubliceList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_elec_patient_detail)
public class ElecPatientDetailActivity extends BaseActivity {

    @ViewInject(R.id.name_tv)
    private TextView mNameTv;

    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.bed_tv)
    private TextView mBedTv;
    @ViewInject(R.id.office_tv)
    private TextView mOfficeTv;
    @ViewInject(R.id.area_tv)
    private TextView mAreaTv;
    @ViewInject(R.id.num_tv)
    private TextView mNumTv; //患者编号
    @ViewInject(R.id.case_no_tv)
    private TextView mCaseNoTv;//就诊号
    @ViewInject(R.id.enter_hospital_tv)
    private TextView mEnterHospitalTv;;

    @ViewInject(R.id.case_lv)
    private NoScrollListView mCaseLv;
    @ViewInject(R.id.refresh_ptrsv)
    private PullToRefreshScrollView mRefreshPtrsv;

    private ElectricHospital mElectricHospital;
    private int mPageNo = 1;
    private int mPageSize = 10;
    private String eventNo;

    private CaseDownloadAdapter mDownloadAdapter;
    private ArrayList<PubliceList> mPubliceLists = new ArrayList<>();
    private ArrayList<PubliceList> mTempPublics = new ArrayList<>();
    private PubliceList publiceList;

    private PullState mPullState = PullState.NORMAL;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseRowsResp<PubliceList>>() {
                }.getType();
                BaseRowsResp<PubliceList> res = new Gson().fromJson(result, objectType);
                mTempPublics = res.value.rows;

                if (mPullState == PullState.TOP_REFRESH) {
                    mPubliceLists=mTempPublics;
                    refreshComplete(mRefreshPtrsv);
                } else if (mPullState == PullState.END_REFRESH) {
                    mPubliceLists.addAll(mTempPublics);
                    refreshComplete(mRefreshPtrsv);
                } else {
                    mPubliceLists=mTempPublics;
                }
                mDownloadAdapter.setData(mPubliceLists);
            //    UiUtils.setHeight(mContext,mDownloadAdapter,mCaseLv);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mFileCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<String>>() {
                }.getType();
                BaseResp<String> res = new Gson().fromJson(result, objectType);
                publiceList.downLoadPath = res.value;
                startWeb();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };



    private void startWeb() {

        String url = ConstantValues.SERVER_IMG_HOST + publiceList.downLoadPath;
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(ConstantValues.KEY_TITLE, publiceList.title);
        intent.putExtra(ConstantValues.KEY_URL, url);
        startActivity(intent);
    }

    @Override
    protected void initView() {
        mElectricHospital = (ElectricHospital) getIntent().getSerializableExtra(ConstantValues.KEY_PATIENT);
        mDownloadAdapter = new CaseDownloadAdapter(this, mPubliceLists, 0);
        mCaseLv.setAdapter(mDownloadAdapter);
        eventNo=mElectricHospital.eventNo;

        if (mElectricHospital != null) {
            setTextView(mNameTv, mElectricHospital.patientName);
            setTextView(mNumTv, mElectricHospital.patientId);
            setTextView(mCaseNoTv, mElectricHospital.eventNo);
            setTextView(mAgeTv, mElectricHospital.age + "岁");
            setTextView(mAreaTv, mElectricHospital.wardName + "");
            setTextView(mOfficeTv, mElectricHospital.deptName);
            setTextView(mBedTv, mElectricHospital.bedNo);
            setTextView(mEnterHospitalTv, mElectricHospital.admissionTime.replace("/","-") + "");
        }

    }


    @Override
    protected void initListener() {
        mRefreshPtrsv.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshPtrsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullState = PullState.TOP_REFRESH;
                mPageNo = 1;
                BaseManager.getPubliceList(mPageNo + "", mPageSize + "",eventNo, mCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullState = PullState.END_REFRESH;
                mPageNo++;
                BaseManager.getPubliceList(mPageNo + "", mPageSize + "",eventNo, mCallback);
            }
        });

        mCaseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                publiceList = mPubliceLists.get(position);

                showdialog();
                BaseManager.caseLoad(publiceList.eventNo, publiceList.emrId, publiceList.patientId, publiceList.title, mFileCallback);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }

    private void queryData() {
        showdialog();
        BaseManager.getPubliceList(mPageNo + "", mPageSize + "",eventNo, mCallback);
    }

    private void refreshComplete(
            final PullToRefreshScrollView refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
                mPullState = PullState.NORMAL;
            }
        }, 200);
    }

}
