package com.netease.nim.session.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.netease.nim.session.SessionCustomization;
import com.netease.nim.session.module.Container;
import com.netease.nim.session.module.ModuleProxy;
import com.netease.nim.session.module.list.MessageListPanel;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;


/**
 * 消息历史查询界面
 * <p/>
 * Created by huangjun on 2015/4/17.
 */
public class MessageHistoryActivity extends BaseActivity implements ModuleProxy {

    private static final String EXTRA_DATA_ACCOUNT = "EXTRA_DATA_ACCOUNT";
    private static final String EXTRA_DATA_SESSION_TYPE = "EXTRA_DATA_SESSION_TYPE";
    private static final String EXTRA_DATA_SESSION_CUSTOM = "EXTRA_DATA_SESSION_CUSTOM";

    // context
    private SessionTypeEnum sessionType;
    private String account; // 对方帐号

    private MessageListPanel messageListPanel;
    private SessionCustomization customization;

    public static void start(Context context, String account, SessionTypeEnum sessionType, SessionCustomization customization) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATA_ACCOUNT, account);
        intent.putExtra(EXTRA_DATA_SESSION_TYPE, sessionType);
        intent.putExtra(EXTRA_DATA_SESSION_CUSTOM, customization);
        intent.setClass(context, MessageHistoryActivity.class);
        context.startActivity(intent);
    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    protected void initView() {
        View rootView = LayoutInflater.from(this).inflate(R.layout.nim_activity_message_history, null);
        setContentView(rootView);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        onParseIntent();

        Container container = new Container(this, account, sessionType, this);

        //漫游消息
        messageListPanel = new MessageListPanel(container, rootView, true, true,customization);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        messageListPanel.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        messageListPanel.onDestroy();
    }

    protected void onParseIntent() {
        account = getIntent().getStringExtra(EXTRA_DATA_ACCOUNT);
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra(EXTRA_DATA_SESSION_TYPE);
        customization = (SessionCustomization) getIntent().getSerializableExtra(EXTRA_DATA_SESSION_CUSTOM);
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return true;
    }
}
