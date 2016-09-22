package com.sg.eyedoctor.commUtils.internetConsult.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.internetConsult.activity.InternetConsultDetailActivity;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.InternetConsultAdapter;
import com.sg.eyedoctor.commUtils.internetConsult.bean.InternetConsult;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 互联网会诊
 */
@ContentView(R.layout.fragment_internet_consult)
public class InternetConsultationFragment extends BaseFragment {
    public static final String TYPE = "TYPE";
    public static final int TYPE_JOIN = 2;//我参与的
    public static final int TYPE_NEW = 1;//最新
    public static final int TYPE_MY = 3;//我的会诊
    public static final int ROWS = 3;
    public int mType = TYPE_NEW;

    @ViewInject(R.id.case_lv)
    private PullToRefreshListView mPullToRefreshListView;

    private ListView mCaseLv;
    private InternetConsultAdapter mTopicCaseAdapter;
    private ArrayList<InternetConsult> mTopicCases;

    private int mPage = 1;
    private int mRows = ROWS;
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉

    public InternetConsultationFragment(int fragmentType) {
        mType = fragmentType;
    }

    public InternetConsultationFragment() {
    }

    public static final InternetConsultationFragment newInstance(int fragmentType) {
        InternetConsultationFragment f = new InternetConsultationFragment(fragmentType);
        Bundle bundle = new Bundle(2);
        bundle.putInt(TYPE, fragmentType);

        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(TYPE);
    }

    public void queryData() {

        showLoginDlg();
        switch (mType) {
            case TYPE_JOIN:
                BaseManager.netConsultationJoinListFind(mPage, mRows, mDoctor.id,"", mCallback);
                break;
            case TYPE_MY:
                BaseManager.netConsultationListFind(mPage, mRows, mDoctor.id, "",mCallback);
                break;
            case TYPE_NEW:
                BaseManager.netConsultationListFind(mPage, mRows, "","", mCallback);
                break;
        }
    }

    @Override
    protected void initView() {

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mCaseLv = mPullToRefreshListView.getRefreshableView();

        mTopicCases = new ArrayList<>();
        mTopicCaseAdapter = new InternetConsultAdapter( getActivity(),mTopicCases,0);
        mCaseLv.setAdapter(mTopicCaseAdapter);

        UiUtils.setEmptyText(getActivity(), mPullToRefreshListView, getString(R.string.empty));
    }

    @Override
    protected void initListener() {

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mPullState = PullState.TOP_REFRESH;
                switch (mType) {
                    case TYPE_JOIN:
                        BaseManager.netConsultationJoinListFind(mPage, mRows, mDoctor.id, "", mCallback);
                        break;
                    case TYPE_MY:
                        BaseManager.netConsultationListFind(mPage, mRows, mDoctor.id, "", mCallback);
                        break;
                    case TYPE_NEW:
                        BaseManager.netConsultationListFind(mPage, mRows, "", "", mCallback);
                        break;
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                mPullState = PullState.END_REFRESH;
                switch (mType) {
                    case TYPE_JOIN:
                        BaseManager.netConsultationJoinListFind(mPage, mRows, mDoctor.id, "", mCallback);
                        break;
                    case TYPE_MY:
                        BaseManager.netConsultationListFind(mPage, mRows, mDoctor.id, "", mCallback);
                        break;
                    case TYPE_NEW:
                        BaseManager.netConsultationListFind(mPage, mRows, "", "", mCallback);
                        break;
                }
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InternetConsultDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA, mTopicCases.get(position - 1));
                startActivity(intent);
            }
        });

//        mCaseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(getActivity(), CaseDetailActivity.class);
//                intent.putExtra("data",mTopicCases.get(position));
//                startActivity(intent);
//            }
//        });
    }

    private NetCallback mCallback = new NetCallback(getActivity()) {
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
                        if (res.value.size() < ROWS) {
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断该Fragment是否已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() ) {
            mPage=1;
            mPullState = PullState.NORMAL;
            queryData(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint() ) {
            mPage=1;
            mPullState = PullState.NORMAL;
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            mPage=1;
            mPullState = PullState.NORMAL;
            queryData();
        }
    }

}
