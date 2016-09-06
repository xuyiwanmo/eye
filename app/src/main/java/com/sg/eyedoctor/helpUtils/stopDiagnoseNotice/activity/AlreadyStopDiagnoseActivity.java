package com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.activity;

import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.adapter.StopDiagnoseAdapter;
import com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.bean.StopDiagnose;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 已停诊
 */
@ContentView(R.layout.activity_already_stop_diagnose)
public class AlreadyStopDiagnoseActivity extends BaseActivity {

    @ViewInject(R.id.stop_lv)
    private ListView mStopLv;

    private ArrayList<StopDiagnose> mStopDiagnoses=new ArrayList<>();
    private StopDiagnoseAdapter mDiagnoseAdapter;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<StopDiagnose>>(){}.getType();
                BaseArrayResp<StopDiagnose> res=new Gson().fromJson(result, objectType);
                mStopDiagnoses=res.value;
                mDiagnoseAdapter.setData(mStopDiagnoses);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mDiagnoseAdapter=new StopDiagnoseAdapter(mContext,mStopDiagnoses,0);
        mStopLv.setAdapter(mDiagnoseAdapter);
        UiUtils.setEmptyText(mContext, mStopLv, getString(R.string.empty));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getStopNotice(mCallback);
    }
}
