package com.sg.eyedoctor.consult.advice.activity;

import android.content.Intent;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.consult.advice.bean.AdviceCheck;
import com.sg.eyedoctor.helpUtils.doctorAdvice.adapter.AdviceCheckAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 建议检查项目
 */
@ContentView(R.layout.activity_check_advice)
public class CheckAdviceActivity extends BaseActivity implements AdviceCheckAdapter.ClickCheck {

    @ViewInject(R.id.check_lv)
    private PullToRefreshListView mRefreshLv;
    private ListView mCheckLv;
    private AdviceCheckAdapter mAdapter;

    private ArrayList<AdviceCheck> mChecks = new ArrayList<>();
    private int mRows = 10;
    private int mPage = 1;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {

                closeDialog();
                Type objectType = new TypeToken<BaseRowsResp<AdviceCheck>>() {
                }.getType();
                BaseRowsResp<AdviceCheck> res = new Gson().fromJson(result, objectType);

                mChecks = res.value.rows;
                mAdapter.setData(mChecks);
                LogUtils.i(mChecks.size() + "");
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
        mCheckLv = mRefreshLv.getRefreshableView();
        mAdapter = new AdviceCheckAdapter(mContext, mChecks, 0, this);
        mCheckLv.setAdapter(mAdapter);

        showdialog();
        BaseManager.getShareCheck(mPage + "", mRows + "", "", mCallback);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    public void click(int position) {
        Intent intent = new Intent();
        AdviceCheck check = mChecks.get(position);
        intent.putExtra(ConstantValues.KEY_DATA, check);
        setResult(RESULT_OK, intent);
        AppManager.getAppManager().finishActivity();
    }
}
