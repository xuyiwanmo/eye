package com.sg.eyedoctor.contact.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.contact.adapter.SearchContactAdapter;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 添加朋友
 */
@ContentView(R.layout.activity_search_text)
public class AddFriendActivity extends BaseActivity implements SearchLayout.SearchCallback{

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.search_lv)
    private ListView mSearchLv;

    private SearchContactAdapter mSearchContactAdapter;
    private ArrayList<FriendList> mSearchContacts;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<FriendList>>() {
                }.getType();
                BaseArrayResp<FriendList> res = new Gson().fromJson(result, objectType);

                mSearchContacts = res.value;
                mSearchContactAdapter.setData(mSearchContacts);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mSearchContacts = new ArrayList<>();
        mSearchContactAdapter = new SearchContactAdapter(this, mSearchContacts, R.layout.item_search_contact);
        mSearchLv.setAdapter(mSearchContactAdapter);
        mSearchSl.setCallback(this);
        UiUtils.setEmptyText(mContext,mSearchLv,"无记录");
    }

    @Override
    protected void initListener() {
        mSearchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddFriendActivity.this, DoctorDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA, mSearchContacts.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.contact_add_friend);
    }

    @Override
    public void fillData(String s) {
        showdialog();
        BaseManager.circleFriendFind(s, mCallback);
    }

    @Override
    public void cancel() {

    }
}
