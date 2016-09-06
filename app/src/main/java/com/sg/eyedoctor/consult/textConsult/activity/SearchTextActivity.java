package com.sg.eyedoctor.consult.textConsult.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.consult.textConsult.adapter.SearchConsultAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 图文咨询搜索界面
 */
@ContentView(R.layout.activity_search_text)
public class SearchTextActivity extends BaseActivity implements SearchLayout.SearchCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;
    /**
     * (1:未回复|5:已回复|4：已完成)
     */
    private int mType = 1;

    private SearchConsultAdapter mAdapter;
    private ArrayList<Patient> mPatients = new ArrayList<>();
    private Patient mPatient;
    private String mTempString = "";

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);

                mPatients = res.value;
                if (mPatients != null) {
                    mAdapter.setData(mPatients);
                } else {
                    showToast(R.string.query_empty);
                }
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mAdapter = new SearchConsultAdapter(mContext, mPatients, 0);

        mSearchLv.setAdapter(mAdapter);
        mEmptyTv=UiUtils.setEmptyText(mContext,mSearchLv,"无记录");
        mSearchSl.setCallback(this,mEmptyTv);
    }

    @Override
    protected void initListener() {
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //**************点击对应的联系人   跳转到  聊天界面
                if (!CommonUtils.isLogin()) {
                    showToast(R.string.operation_not_open);
                    return;
                }
                mPatient = mPatients.get(position);

                P2PMessageActivity.start(mContext, mPatient.patientIM, SessionHelper.getTextP2pCustomization(mPatient, mPatient.state));

            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.text_consult);
    }

    @Override
    public void fillData(String s) {
        mTempString = s;
        showdialog();
        BaseManager.queryConsult("", "1", mTempString, mCallback);
    }

    @Override
    public void cancel() {
        mPatients.clear();
        mAdapter.setData(mPatients);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTempString.equals("")) {
            return;
        }
        showdialog();
        BaseManager.queryConsult("", "1", mTempString, mCallback);

    }
}
