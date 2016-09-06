package com.sg.eyedoctor.helpUtils.massNotice.activity;

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
import com.sg.eyedoctor.settings.myStateMessage.activity.MessageDetailActivity;
import com.sg.eyedoctor.settings.myStateMessage.adapter.StateMessageAdapter;
import com.sg.eyedoctor.settings.myStateMessage.bean.StateMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 已发送群发通知
 */
@ContentView(R.layout.activity_already_send_notice)
public class AlreadySendNoticeActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.send_lv)
    private ListView mSendLv;
    private StateMessageAdapter mMessageAdapter;

    private ArrayList<StateMessage> mStateMessages=new ArrayList<>();

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<StateMessage>>(){}.getType();
                BaseArrayResp<StateMessage> res=new Gson().fromJson(result, objectType);

                mStateMessages=res.value;
                mMessageAdapter.setData(mStateMessages);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {
        mMessageAdapter=new StateMessageAdapter(mContext,mStateMessages,0,ConstantValues.STATE_ALREADY_SEND);
        mSendLv.setAdapter(mMessageAdapter);
        UiUtils.setEmptyText(mContext, mSendLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {
        mSendLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StateMessage message=mStateMessages.get(position);
                Intent intent=new Intent(mContext,MessageDetailActivity.class);
                intent.putExtra(ConstantValues.KEY_DATA,message);
                intent.putExtra(ConstantValues.KEY_TYPE,ConstantValues.STATE_ALREADY_SEND);
                startActivity(intent);
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
        BaseManager.getMassNotice(mCallback);
    }
}
