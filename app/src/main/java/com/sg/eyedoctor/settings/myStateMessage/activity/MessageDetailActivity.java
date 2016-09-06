package com.sg.eyedoctor.settings.myStateMessage.activity;

import android.text.TextUtils;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.settings.myStateMessage.bean.StateMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 站内消息详情
 */
@ContentView(R.layout.activity_message_detail)
public class MessageDetailActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.message_title_tv)
    private TextView mMessageTitleTv;
    @ViewInject(R.id.time_tv)
    private TextView mTimeTv;
    @ViewInject(R.id.from_tv)
    private TextView mFromTv;
    @ViewInject(R.id.content_tv)
    private TextView mContentTv;
    private StateMessage mStateMessage;
    private int mType;

    @Override
    protected void initView() {
        mStateMessage = (StateMessage) getIntent().getSerializableExtra(ConstantValues.KEY_DATA);
        mType=getIntent().getIntExtra(ConstantValues.KEY_TYPE,ConstantValues.STATE_RECEIVER);
        setTextView(mContentTv, mStateMessage.messageDetail);
        setTextView(mTimeTv, mStateMessage.createDate);
        String result="";
        if(mType==ConstantValues.STATE_ALREADY_SEND){

            for (int i = 0; i < mStateMessage.receiverList.size(); i++) {
                result = result + mStateMessage.receiverList.get(i).receiverName + "，";
            }
            if (!TextUtils.isEmpty(result)) {
                result ="收件人: "+ result.substring(0, result.length() - 1);
            }
        }else if(mType==ConstantValues.STATE_RECEIVER){

            switch (mStateMessage.type){
                case 1:
                    result="来自: 系统消息";
                    break;
                case 2:
                    if(mStateMessage.publisherName!=null){
                        result="来自: "+mStateMessage.publisherName+"的群发通知";
                    }else{
                        result="来自: 群发通知";
                    }

                    break;
                case 3:
                    if(mStateMessage.publisherName!=null){
                        result="来自: "+mStateMessage.publisherName+"的停诊通知";
                    }else{
                        result="来自: 停诊通知";
                    }
                    break;
            }
        }
        setTextView(mFromTv, result);
        setTextView(mMessageTitleTv, mStateMessage.messageTitle);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }


}
