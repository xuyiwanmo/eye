package com.sg.eyedoctor.eyeCircle.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.eyeCircle.activity.CaseDetailActivity;
import com.sg.eyedoctor.eyeCircle.activity.PublishCaseOrTopicActivity;
import com.sg.eyedoctor.eyeCircle.adapter.TopicCaseAdapter;
import com.sg.eyedoctor.eyeCircle.bean.TopicCase;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 眼科圈病例,话题
 */
@ContentView(R.layout.fragment_circle_topic_case)
public class CircleTopicCaseFragment extends BaseFragment {
    public static final String TYPE = "TYPE";
    public static final int TYPE_TOPIC = 2;//话题
    public static final int TYPE_CASE = 1;//病例
    public static final int ROWS = 3;
    private int mPage = 1;
    private int mRows = ROWS;
    public int mType = TYPE_TOPIC;

    @ViewInject(R.id.case_lv)
    private PullToRefreshListView mPullToRefreshListView;
    @ViewInject(R.id.publish_ll)
    private LinearLayout mPublishLl;
    @ViewInject(R.id.publish_title_tv)
    private TextView mTitleTv;
    @ViewInject(R.id.head_img)
    private ImageView mHeadImg;

    private ListView mCaseLv;

    private TopicCaseAdapter mTopicCaseAdapter;
    private ArrayList<TopicCase> mTopicCases;


    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉

    public CircleTopicCaseFragment(int fragmentType) {
        mType = fragmentType;
    }

    public CircleTopicCaseFragment() {
    }

    public static final CircleTopicCaseFragment newInstance(int fragmentType) {
        CircleTopicCaseFragment f = new CircleTopicCaseFragment(fragmentType);
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
        BaseManager.topicListFind(mPage, mRows, mType, mCallback);
        viewInit();
    }



    @Override
    protected void initView() {
        if (mType == TYPE_CASE) {
            mTitleTv.setText(R.string.publish_case);
        } else {
            mTitleTv.setText(R.string.publish_topic);
        }

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mCaseLv = mPullToRefreshListView.getRefreshableView();

        mTopicCases = new ArrayList<>();
        mTopicCaseAdapter = new TopicCaseAdapter(mTopicCases, getActivity());
        mCaseLv.setAdapter(mTopicCaseAdapter);

        UiUtils.setEmptyText(getActivity(), mCaseLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mPullState = PullState.TOP_REFRESH;
                BaseManager.topicListFind(mPage, mRows, mType, mCallback);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage++;
                mPullState = PullState.END_REFRESH;
                BaseManager.topicListFind(mPage, mRows, mType, mCallback);
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA, mTopicCases.get(position - 1));
                intent.putExtra(ConstantValues.KEY_TYPE, mType);
                startActivity(intent);
            }
        });
    }

    @Event(R.id.publish_ll)
    private void publish(View view) {
        if (!mIsAuth) {
            startAuthActivity(R.string.eye_circle);
            return;
        }
        Intent intent = new Intent(getActivity(), PublishCaseOrTopicActivity.class);
        intent.putExtra("type", mType);
        startActivity(intent);
    }

    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<TopicCase>>() {
                }.getType();
                BaseArrayResp<TopicCase> res = new Gson().fromJson(result, objectType);
                ArrayList<TopicCase> topicCases = res.value;

                switch (mPullState) {
                    case NORMAL:
                        closeDialog();
                        mTopicCases = topicCases;
                        mTopicCaseAdapter.setData(mTopicCases);
                        break;
                    case TOP_REFRESH:
                        mTopicCases = topicCases;
                        mTopicCaseAdapter.setData(mTopicCases);
                        refreshComplete(mPullToRefreshListView);
                        break;
                    case END_REFRESH:
                        mTopicCases.addAll(topicCases);
                        mTopicCaseAdapter.setData(mTopicCases);
                        refreshComplete(mPullToRefreshListView);
                        if (topicCases.size() < ROWS) {
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


    public void viewInit() {

        CommonUtils.loadImg(mDoctor.picFileName, mHeadImg);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            mPage=1;
            mPullState = PullState.NORMAL;
            queryData();
        }

        mIsAuth = (mDoctor.state == 2?true:false);
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
}
