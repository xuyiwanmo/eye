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
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.helpUtils.doctorAdvice.adapter.AdviceCheckAdapter;
import com.sg.eyedoctor.helpUtils.doctorAdvice.adapter.LongAdviceCheckAdapter;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.LongAdviceCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 长期医嘱选择
 */
@ContentView(R.layout.activity_long_advice_check)
public class LongAdviceCheckActivity extends BaseActivity implements AdviceCheckAdapter.ClickCheck, LongAdviceCheckAdapter.ClickCheck, SearchLayout.SearchCallback {

    @ViewInject(R.id.check_lv)
    private PullToRefreshListView mRefreshLv;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    private ListView mCheckLv;
    private LongAdviceCheckAdapter mAdapter;
    private int mRows = 10;
    private int mPage = 1;
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉
    private boolean mIsLast = false;
    private ArrayList<LongAdviceCheck> mAdvices = new ArrayList<>();
    private ArrayList<LongAdviceCheck> mSearchAdvices = new ArrayList<>();
    private String mSearchStr = "";

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();

                if (result.contains("value")) {  //value不为空
                    LogUtils.i("not null");
                    Type objectType = new TypeToken<BaseRowsResp<LongAdviceCheck>>() {
                    }.getType();

                    BaseRowsResp<LongAdviceCheck> res = new Gson().fromJson(result, objectType);
                    ArrayList<LongAdviceCheck> checks = res.value.rows;
                    if (mSearchStr.equals("")) {
                        switch (mPullState) {
                            case NORMAL:
                                closeDialog();
                                mAdvices = checks;
                                mAdapter.setData(mAdvices);
                                break;
                            case TOP_REFRESH:
                                mAdvices = checks;
                                mAdapter.setData(mAdvices);
                                refreshComplete(mRefreshLv);
                                break;
                            case END_REFRESH:
                                mAdvices.addAll(checks);
                                mAdapter.setData(mAdvices);
                                refreshComplete(mRefreshLv);
                                if (mAdvices.size() >= res.value.total) {
                                    showToast(R.string.end_yet);
                                    mIsLast = true;
                                }
                                break;
                        }
                    } else {//模糊查询
                        switch (mPullState) {
                            case NORMAL:
                                closeDialog();
                                mSearchAdvices = checks;
                                mAdapter.setData(mSearchAdvices);
                                break;
                            case TOP_REFRESH:
                                mSearchAdvices = checks;
                                mAdapter.setData(mSearchAdvices);
                                refreshComplete(mRefreshLv);
                                break;
                            case END_REFRESH:

                                mSearchAdvices.addAll(checks);
                                mAdapter.setData(mSearchAdvices);

                                refreshComplete(mRefreshLv);
                                if (mSearchAdvices.size() >= res.value.total) {
                                    showToast(R.string.end_yet);
                                    mIsLast = true;
                                }
                                break;
                        }
                    }

                } else {
                    LogUtils.i("not null");
                    switch (mPullState) {
                        case TOP_REFRESH:
                        case END_REFRESH:
                            refreshComplete(mRefreshLv);
                            break;
                    }
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
        mCheckLv = mRefreshLv.getRefreshableView();
        mAdapter = new LongAdviceCheckAdapter(mContext, mAdvices, 0, this);
        mSearchSl.setCallback(this);

        mCheckLv.setAdapter(mAdapter);
        UiUtils.setEmptyText(mContext, mCheckLv, getString(R.string.empty));

        showdialog();
        BaseManager.getAdviceCheck(mPage + "", mRows + "", mSearchStr, mCallback);
    }

    @Override
    protected void initListener() {
        mCheckLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        mRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mPullState = PullState.TOP_REFRESH;
                BaseManager.getAdviceCheck(mPage + "", mRows + "", mSearchStr, mCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mIsLast) {
                    showToast(R.string.end_yet);
                    return;
                }
                mPage++;
                mPullState = PullState.END_REFRESH;
                BaseManager.getAdviceCheck(mPage + "", mRows + "", mSearchStr, mCallback);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    public void click(int position) {
        Intent intent = new Intent();
        LongAdviceCheck check = mAdvices.get(position);
        intent.putExtra("data", check);
        setResult(RESULT_OK, intent);
        AppManager.getAppManager().finishActivity();
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

    @Override
    public void fillData(String s) {
        mSearchStr = s;
        showdialog();
        mPage = 1;
        mPullState = PullState.NORMAL;
        BaseManager.getAdviceCheck(mPage + "", mRows + "", mSearchStr, mCallback);

    }

    @Override
    public void cancel() {
        mSearchStr = "";
        mAdapter.setData(mAdvices);
    }
}
