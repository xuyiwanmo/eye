package com.netease.nim.session.action;

import android.widget.Toast;

import com.netease.nim.avchat.activity.AVChatActivity;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.utils.NetworkUtil;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class AVChatAction extends BaseAction {
    private AVChatType avChatType;
    private String patientId;

    public AVChatAction(AVChatType avChatType,String patientId) {
        super(R.drawable.message_plus_video_chat_normal, R.string.input_panel_video_call);
        this.avChatType = avChatType;
        this.patientId=patientId;
    }

    @Override
    public void onClick() {
        if (NetworkUtil.isNetAvailable(getActivity())) {
            startAudioVideoCall(avChatType);
        } else {
            Toast.makeText(getActivity(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    /************************ 音视频通话 ***********************/

    public void startAudioVideoCall(AVChatType avChatType) {
        AVChatActivity.start(getActivity(), getAccount(), avChatType.getValue(), AVChatActivity.FROM_INTERNAL,patientId);
    }
}
