package com.sg.eyedoctor.contact.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.contact.adapter.ContactAdapter;
import com.sg.eyedoctor.contact.bean.AddFriend;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 新的朋友
 */
@ContentView(R.layout.activity_new_friend)
public class NewFriendActivity extends BaseActivity implements ContactAdapter.ContactCallback {
    public static final int TYPE_AGREE = 1;
    public static final int TYPE_REFUSE = 2;
    private int mType = TYPE_AGREE;
    private int mPosition = 0;
    private boolean mClearData=true;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.phone_contact_ll)
    private LinearLayout mPhoneLl;
    @ViewInject(R.id.search_ll)
    private LinearLayout mSearchLl;
    @ViewInject(R.id.friend_lv)
    private PullToRefreshListView mFriendPtrlv;
//    @ViewInject(R.id.friend_lv)
    private ListView mFriendLv;
    @ViewInject(R.id.recommend_friend_lv)
    private NoScrollListView mRecommendLv;

    private ContactAdapter mContactAdapter;
    private ArrayList<AddFriend> mFriends;

    private int mRows=20;
    private int mCurrentPage=1;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<AddFriend>>() {
                }.getType();
                BaseArrayResp<AddFriend> res = new Gson().fromJson(result, objectType);
                if(mClearData){
                    mFriends=res.value;
                    refreshComplete(mFriendPtrlv);
                }else{
                    mFriends.addAll(res.value);
                }

                if(res.value.size()<20){
                    initFooter(false);
                }

                mContactAdapter.setData(mFriends);

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    private NetCallback mMyCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<AddFriend>>() {
                }.getType();
                BaseArrayResp<AddFriend> res = new Gson().fromJson(result, objectType);
                mFriends = res.value;
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    private NetCallback mAgreeCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                sendAgreeOrRefuse(mType, mPosition);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    private View mFootView;
    private TextView mFootTv;

    @Override
    protected void initView() {
        mFriends = new ArrayList<>();
        mFriendLv=mFriendPtrlv.getRefreshableView();
        mFriendPtrlv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFriendPtrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mClearData=true;
                mCurrentPage=0;
                showdialog();
                BaseManager.circleFriendInfo(mRows,mCurrentPage,mCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        mContactAdapter = new ContactAdapter(mContext, mFriends, R.layout.item_contact);
        mContactAdapter.setContactCallback(this);
        mFriendLv.setAdapter(mContactAdapter);

        initFooter(true);

    }

    private void initFooter(final boolean canload) {
        if(mFootView!=null){
           mFriendLv.removeFooterView(mFootView);
        }else{
            mFootView =View.inflate(mContext,R.layout.footer_contact_new_friend,null);
            mFootTv =(TextView)mFootView.findViewById(R.id.foot_tv);
        }

        mFootTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClearData=false;
                if(canload){
                    showdialog();
                    BaseManager.circleFriendInfo(mRows,++mCurrentPage,mCallback);
                }else{
                    showToast(R.string.load_complete);
                }

            }
        });
        mFriendLv.addFooterView(mFootView);
        if(canload){
            mFootTv.setText(R.string.load_more);
        }else{
            mFootTv.setText(R.string.load_complete);
        }



    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv("添加朋友", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddFriendActivity.class));
            }
        });
    }

    @Override
    public void agree(int type, int postition) {
        mType = type;
        mPosition = postition;
        AddFriend addFriend = mFriends.get(postition);
        showdialog();
        BaseManager.circleFriendValidate(addFriend.id, type + "", addFriend.applyId, mAgreeCallback);
    }

    private void sendAgreeOrRefuse(int type, int postition) {
        View view = mFriendLv.getChildAt(postition);
        view.findViewById(R.id.agree_ll).setVisibility(View.GONE);
        TextView tv = (TextView) view.findViewById(R.id.accept_tv);
        tv.setVisibility(View.VISIBLE);
        if (type == TYPE_AGREE) {
            showToast(R.string.agree);
            tv.setText(R.string.agree);
        } else {
            showToast(R.string.refuse_already);
            tv.setText(R.string.refuse_already);
        }
    }

    @Event(R.id.phone_contact_ll)
    private void phoneContact(View view) {
      //  startActivity(new Intent(this, PhoneContactActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.circleFriendInfo(mRows,mCurrentPage,mCallback);
    }
    public void refreshComplete(
            final PullToRefreshBase<ListView> refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
            }
        }, 1000);
    }

}
