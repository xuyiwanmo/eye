package com.sg.eyedoctor.helpUtils.electronicRecords.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.helpUtils.electronicRecords.adapter.ElectricPatientAdapter;
import com.sg.eyedoctor.helpUtils.electronicRecords.bean.ElectricHospital;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_electric_record)
public class ElectricRecordActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.patient_lv)
    private PullToRefreshListView mRefreshLv;
    private ListView mPatientLv;

    public static final int ROWS = 10;
    private int mPage = 1;
    private int mRows = ROWS;

    private ArrayList<ElectricHospital> mPatients = new ArrayList<>();

    private ElectricPatientAdapter mPatientAdapter;
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉

    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<ElectricHospital>>() {
                }.getType();
                BaseArrayResp<ElectricHospital> res = new Gson().fromJson(result, objectType);
                ArrayList<ElectricHospital> topicCases = res.value;

                LogUtils.i(topicCases.size()+"");

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
                        if (topicCases.size() < ROWS) {
                            showToast(R.string.end_yet);
                        }

                        break;
                }
            }
        }

        @Override
        protected void requestError(Throwable ex, boolean isOnCallback) {

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
        mPatientAdapter = new ElectricPatientAdapter(this, mPatients, 0);
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
                BaseManager.getNowPubliceEvent(mPage + "", mRows + "", mCallback);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                mPullState = PullState.END_REFRESH;

                BaseManager.getNowPubliceEvent(mPage + "", mRows + "", mCallback);
            }
        });

        mRefreshLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
//                intent.putExtra("data", mTopicCases.get(position - 1));
//                startActivity(intent);
                ElectricHospital patient = mPatients.get(position - 1);
                Intent intent = new Intent(mContext, ElecPatientDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_PATIENT, patient);
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
        BaseManager.getNowPubliceEvent(mPage + "", mRows + "", mCallback);
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
