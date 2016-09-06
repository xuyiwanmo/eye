package com.sg.eyedoctor.settings.myStateMessage.activity;

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
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.settings.myStateMessage.adapter.StateMessageAdapter;
import com.sg.eyedoctor.settings.myStateMessage.bean.StateMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 站内消息
 */
@ContentView(R.layout.activity_platform_message)
public class StateMessageActivity extends BaseActivity {

    @ViewInject(R.id.refresh_ptrlv)
    private PullToRefreshListView mRefreshLv;

    private ListView mMessageLv;
    private ArrayList<StateMessage> mStateMessages = new ArrayList<>();
    private StateMessageAdapter mMessageAdapter;
    private int mCurrPosition;

    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<StateMessage>>() {
                }.getType();
                BaseArrayResp<StateMessage> res = new Gson().fromJson(result, objectType);
                mStateMessages=res.value;
                mMessageAdapter.setData(mStateMessages);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mReadCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Intent intent=new Intent(mContext,MessageDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA, mStateMessage);
                intent.putExtra(ConstantValues.KEY_TYPE,ConstantValues.STATE_RECEIVER);
                startActivity(intent);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private StateMessage mStateMessage;


    @Override
    protected void initView() {
        mMessageLv = mRefreshLv.getRefreshableView();
        mMessageAdapter=new StateMessageAdapter(mContext,mStateMessages,0,ConstantValues.STATE_RECEIVER);
        mMessageLv.setAdapter(mMessageAdapter);
       // mRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
        UiUtils.setEmptyText(mContext, mMessageLv, getString(R.string.empty));
    }

    @Override
    protected void initListener() {
        mRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        mRefreshLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrPosition=position-1;
                mStateMessage =mStateMessages.get(mCurrPosition);
                showdialog();
                BaseManager.messageRead(mStateMessage.messageId,mReadCallback);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getStateMessageByDoc(mCallback);
    }
}
