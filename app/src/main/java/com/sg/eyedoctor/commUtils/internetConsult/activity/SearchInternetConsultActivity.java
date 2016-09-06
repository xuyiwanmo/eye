package com.sg.eyedoctor.commUtils.internetConsult.activity;

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
import com.sg.eyedoctor.commUtils.internetConsult.adapter.InternetConsultAdapter;
import com.sg.eyedoctor.commUtils.internetConsult.bean.InternetConsult;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 互联网会诊搜索
 */
@ContentView(R.layout.activity_search_internet_consult)
public class SearchInternetConsultActivity extends BaseActivity implements SearchLayout.SearchCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;

    @ViewInject(R.id.case_lv)
    private PullToRefreshListView mPullToRefreshListView;

    private ListView mCaseLv;
    private InternetConsultAdapter mTopicCaseAdapter;
    private ArrayList<InternetConsult> mTopicCases;

    private int mPage = 1;
    private int mRows = 5;
    private String mTempString="";
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉
    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<InternetConsult>>() {
                }.getType();
                BaseArrayResp<InternetConsult> res = new Gson().fromJson(result, objectType);

                switch (mPullState) {
                    case NORMAL:
                        closeDialog();
                        mTopicCases = res.value;
                        mTopicCaseAdapter.setData(mTopicCases);
                        break;
                    case TOP_REFRESH:
                        mTopicCases = res.value;
                        mTopicCaseAdapter.setData(mTopicCases);
                        refreshComplete(mPullToRefreshListView);
                        break;
                    case END_REFRESH:
                        mTopicCases.addAll(res.value);
                        mTopicCaseAdapter.setData(mTopicCases);
                        refreshComplete(mPullToRefreshListView);
                        if (res.value.size() < 5) {
                            showToast(R.string.end_yet);
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

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mCaseLv = mPullToRefreshListView.getRefreshableView();

        mTopicCases = new ArrayList<>();
        mTopicCaseAdapter = new InternetConsultAdapter(mContext,mTopicCases, 0);
        mCaseLv.setAdapter(mTopicCaseAdapter);

        mEmptyTv=UiUtils.setEmptyText(mContext,mCaseLv,"无记录");
        mSearchSl.setCallback(this,mEmptyTv);
    }

    @Override
    protected void initListener() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mPullState = PullState.TOP_REFRESH;
                BaseManager.netConsultationListFind(mPage, mRows, "", mTempString, mCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                mPullState = PullState.END_REFRESH;
                BaseManager.netConsultationListFind(mPage, mRows, "", mTempString, mCallback);
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, InternetConsultDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA, mTopicCases.get(position - 1));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle("互联网会诊");
    }

    @Override
    public void fillData(String s) {
        mTempString=s;
        mPage = 0;
        mPullState = PullState.NORMAL;
        showdialog();
        BaseManager.netConsultationListFind(mPage, mRows, "", mTempString, mCallback);
    }

    @Override
    public void cancel() {
        mTopicCases.clear();
        mTopicCaseAdapter.setData(mTopicCases);
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
