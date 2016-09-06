package com.netease.nim.session.activity;

import android.content.Intent;

import com.netease.nim.common.activity.TActionBarActivity;
import com.netease.nim.session.SessionCustomization;
import com.netease.nim.session.constant.Extras;
import com.netease.nim.session.fragment.MessageFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.sg.eyedoctor.common.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhoujianghua on 2015/9/10.
 */
public abstract class BaseMessageActivity extends TActionBarActivity {

    protected String sessionId;

    public SessionCustomization customization;

    //聊天界面
    private MessageFragment messageFragment;

    protected abstract MessageFragment fragment();


    @Override
    protected void initView() {

        parseIntent();
        messageFragment = (MessageFragment) switchContent(fragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置头像  用户名
        if (mDoctor!=null) {
            Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
            fields.put(UserInfoFieldEnum.Name, mDoctor.userName);
            fields.put(UserInfoFieldEnum.AVATAR, CommonUtils.formatUrl(mDoctor.picFileName));
            NIMClient.getService(UserService.class).updateUserInfo(fields);
        }
    }

    @Override
    public void onBackPressed() {
        if (messageFragment == null || !messageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageFragment != null) {
            messageFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (customization != null) {
            customization.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void parseIntent() {
        sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        customization = (SessionCustomization) getIntent().getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);
    }


}
