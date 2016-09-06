package com.sg.eyedoctor.chartFile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.adapter.ChartPatientAdapter;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 病历夹搜索界面
 */
@ContentView(R.layout.activity_search_chart_file)
public class SearchChartFileActivity extends BaseActivity implements SearchLayout.SearchCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;

    private ChartPatientAdapter mAdapter;
    private ArrayList<PatientByGroup> mPatients = new ArrayList<>();

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PatientByGroup>>() {
                }.getType();
                BaseArrayResp<PatientByGroup> res = new Gson().fromJson(result, objectType);
                mPatients = res.value;
                mAdapter.setData(mPatients);
                if (mPatients.size() == 0) {
                    showToast(R.string.search_result_zero);
                }
            } else {
                showToast(R.string.get_data_error);
            }

        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        mAdapter = new ChartPatientAdapter(mContext, mPatients, 0);
        mSearchLv.setAdapter(mAdapter);
        mEmptyTv = UiUtils.setEmptyText(mContext, mSearchLv, "无记录");
        mSearchSl.setCallback(this, mEmptyTv);
    }

    @Override
    protected void initListener() {
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientByGroup patient = mPatients.get(position);
                Intent intent = new Intent(mContext, PatientCenterActivity.class);
                intent.putExtra(ConstantValues.KEY_PATIENT, patient);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.chartfile);
    }

    @Override
    public void fillData(String s) {
        showdialog();
        BaseManager.queryAllPatient(s, mCallback);

    }

    @Override
    public void cancel() {
        mPatients.clear();
        mAdapter.setData(mPatients);

    }

}
