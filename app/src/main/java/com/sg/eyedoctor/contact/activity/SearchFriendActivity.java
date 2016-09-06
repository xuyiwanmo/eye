package com.sg.eyedoctor.contact.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.contact.adapter.FriendListAdapter;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_search_text)
public class SearchFriendActivity extends BaseActivity implements SearchLayout.SearchCallback {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;

    private ArrayList<FriendList> mFriends=new ArrayList<>();
    private FriendListAdapter mFriendAdapter;

    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<FriendList>>() {
                }.getType();
                BaseArrayResp<FriendList> res = new Gson().fromJson(result, objectType);
                mFriends=res.value;
                mFriendAdapter.setData(mFriends);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mFriends = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
        mFriendAdapter = new FriendListAdapter(mContext, mFriends, 0);
        mFriendAdapter.setShowLetter(false);

        mSearchLv.setAdapter(mFriendAdapter);
        mEmptyTv=UiUtils.setEmptyText(mContext,mSearchLv,"无记录");
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
                FriendList friend = mFriends.get(position);
                friend.loginid = friend.loginId;
                P2PMessageActivity.start(mContext, "d" + friend.loginId, SessionHelper.getDoctorP2pCustomization(friend), friend);
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.main_tab_contact);
    }

    @Override
    public void fillData(String s) {
        showdialog();
        BaseManager.circleFriendListFind(s, mCallback);
    }

    @Override
    public void cancel() {
        mFriends.clear();
        mFriendAdapter.setData(mFriends);
    }
}
