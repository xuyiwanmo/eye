package com.sg.eyedoctor.helpUtils.doctorAdvice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.HospitalPatient;
import com.sg.eyedoctor.helpUtils.electronicRecords.adapter.HospitalPatientAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_hospital_patient_list)
public class HospitalPatientListActivity extends BaseActivity {
    public static final int ROWS = 10;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.patient_lv)
    private PullToRefreshListView mRefreshLv;
    private ListView mPatientLv;

    private int mPage = 1;
    private int mRows = ROWS;

    private ArrayList<HospitalPatient> mPatients = new ArrayList<>();

    private HospitalPatientAdapter mPatientAdapter;
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉

    private boolean mIsLast = false;
    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseRowsResp<HospitalPatient>>() {
                }.getType();
                BaseRowsResp<HospitalPatient> res = new Gson().fromJson(result, objectType);
                ArrayList<HospitalPatient> topicCases = res.value.rows;

                switch (mPullState) {
                    case NORMAL:
                        closeDialog();
                        mPatients = topicCases;
                        mPatientAdapter.setData(mPatients);
                        break;
                    case TOP_REFRESH:
                        mPatients = topicCases;
                        mPatientAdapter.setData(mPatients);
                        refreshComplete(mRefreshLv);
                        break;
                    case END_REFRESH:
                        mPatients.addAll(topicCases);
                        mPatientAdapter.setData(mPatients);
                        refreshComplete(mRefreshLv);
                        if (mPatients.size() >= res.value.total) {
                            showToast(R.string.end_yet);
                            mIsLast = true;
                        }

                        break;
                }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
        mPatientLv = mRefreshLv.getRefreshableView();
        mPatientAdapter = new HospitalPatientAdapter(this, mPatients, 0);
        mPatientLv.setAdapter(mPatientAdapter);

        UiUtils.setEmptyText(mContext, mPatientLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {

        mRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mPullState = PullState.TOP_REFRESH;
                BaseManager.getAdvicePatient(mPage + "", mRows + "", mCallback);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mIsLast) {
                    showToast(R.string.end_yet);
                    refreshComplete(mRefreshLv);
                    return;
                }
                mPage++;
                mPullState = PullState.END_REFRESH;
                BaseManager.getAdvicePatient(mPage + "", mRows + "", mCallback);
            }
        });

        mRefreshLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HospitalPatient patient = mPatients.get(position - 1);
                Intent intent = new Intent(mContext, HospitalPatientDetailActivity.class);

                intent.putExtra("data", patient);


                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getAdvicePatient(mPage + "", mRows + "", mCallback);

    }


    public void refreshComplete(
            final PullToRefreshBase<ListView> refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
                mPullState = PullState.NORMAL;
            }
        }, 1000);
    }
}
