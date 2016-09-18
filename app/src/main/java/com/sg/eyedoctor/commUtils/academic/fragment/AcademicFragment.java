package com.sg.eyedoctor.commUtils.academic.fragment;


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
import com.sg.eyedoctor.commUtils.academic.activity.AcademicWebActivity;
import com.sg.eyedoctor.commUtils.academic.adapter.AcademicAdapter;
import com.sg.eyedoctor.commUtils.academic.bean.Academic;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.fragment_academic)
public class AcademicFragment extends BaseFragment {
    public static final String TYPE = "TYPE";
    public static final int ROWS = 5;

    @ViewInject(R.id.academic_ptrlv)
    private PullToRefreshListView mRefreshListView;

    private ListView mAcademicLv;
    private AcademicAdapter mAcademicAdapter;
    private ArrayList<Academic> mAcademics = new ArrayList<>();
    private int mCurrPage = 1;
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉
    private int mType;

    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseRowsResp<Academic>>() {
                }.getType();
                BaseRowsResp<Academic> res = new Gson().fromJson(result, objectType);

                switch (mPullState) {
                    case NORMAL:
                        closeDialog();
                        mAcademics = res.value.rows;
                        mAcademicAdapter.setData(mAcademics);
                        break;
                    case TOP_REFRESH:
                        mAcademics = res.value.rows;
                        mAcademicAdapter.setData(mAcademics);
                        refreshComplete(mRefreshListView);
                        break;
                    case END_REFRESH:
                        mAcademics.addAll(res.value.rows);
                        mAcademicAdapter.setData(mAcademics);
                        refreshComplete(mRefreshListView);
                        if (res.value.rows.size()<ROWS){
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

    public AcademicFragment(int type) {
        mType = type;
    }
    public AcademicFragment() {

    }

    public static final AcademicFragment newInstance(int fragmentType) {
        AcademicFragment f = new AcademicFragment(fragmentType);
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

    @Override
    protected void initView() {
        mAcademicLv = mRefreshListView.getRefreshableView();
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mAcademicAdapter = new AcademicAdapter(getActivity(), mAcademics, 0);
        mAcademicLv.setAdapter(mAcademicAdapter);
    }

    @Override
    protected void initListener() {
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrPage = 1;
                mPullState = PullState.TOP_REFRESH;
                BaseManager.getEyeFrontiersList(mCurrPage + "", ROWS + "", mType + "", mCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrPage++;
                mPullState = PullState.END_REFRESH;
                BaseManager.getEyeFrontiersList(mCurrPage + "", ROWS + "", mType + "", mCallback);
            }
        });
        mAcademicLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AcademicWebActivity.class);

                Academic academic=mAcademics.get(position - 1);
                intent.putExtra("url",academic.url);
                intent.putExtra("id",academic.newsId);
                intent.putExtra("title",academic.newsTitle);
                intent.putExtra("type",academic.newsType);
                intent.putExtra("push",false);

                startActivity(intent);
            }
        });
        UiUtils.setEmptyText(getActivity(),mAcademicLv,getString(R.string.empty));

    }

    private void refreshComplete(
            final PullToRefreshBase<ListView> refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
                mPullState = PullState.NORMAL;
            }
        }, 1000);
    }

    public  void queryData(){
        showLoginDlg();
        BaseManager.getEyeFrontiersList(mCurrPage + "", ROWS + "", mType+ "", mCallback);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断该Fragment是否已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() ) {
            queryData(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint() ) {
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }



}
