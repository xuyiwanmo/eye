package com.sg.eyedoctor.commUtils.caseDiscuss.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.TeamMessageActivity;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.CaseDiscussAdapter;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscuss;
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
 * 搜索病历讨论
 */
@ContentView(R.layout.activity_search_text)
public class SearchDiscussCaseActivity extends BaseActivity implements SearchLayout.SearchCallback{
    public static final int ROWS = 1000;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;

    private int mCurDeletePostiion = 0;
    private CaseDiscuss caseDiscuss;
    private int mCurrPage = 1;
    private ArrayList<CaseDiscuss> mCaseDiscusses = new ArrayList<>();
    private CaseDiscussAdapter mDiscussAdapter;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<CaseDiscuss>>() {
                }.getType();
                BaseArrayResp<CaseDiscuss> res = new Gson().fromJson(result, objectType);
                mCaseDiscusses = res.value;
                mDiscussAdapter.setData(mCaseDiscusses);
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mDeteleCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                mCaseDiscusses.remove(mCurDeletePostiion);
                mDiscussAdapter.setData(mCaseDiscusses);
            } else {
                showToast(R.string.delete_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        mDiscussAdapter = new CaseDiscussAdapter(this, mCaseDiscusses, 0);
        mSearchLv.setAdapter(mDiscussAdapter);
        mEmptyTv= UiUtils.setEmptyText(mContext,mSearchLv,"无记录");
        mSearchSl.setCallback(this,mEmptyTv);
    }

    @Override
    protected void initListener() {
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                    return;
                }
                CaseDiscuss discuss=mCaseDiscusses.get(position);
                TeamMessageActivity.start(mContext, discuss.teamId, discuss.patientName, SessionHelper.getTeamCustomization(discuss.disId), null);
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.case_discussing);
    }

    @Override
    public void fillData(String s) {
        showdialog();
        BaseManager.discussionListFind(mCurrPage + "", ROWS + "", s, mCallback);

    }

    @Override
    public void cancel() {
        mCaseDiscusses.clear();
        mDiscussAdapter.setData(mCaseDiscusses);
    }
}
