package com.netease.nim.session.action;

import com.netease.nim.session.SessionCustomization;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sg.eyedoctor.R;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageAction extends PickImageAction {

    public ImageAction() {
        super(R.drawable.nim_message_plus_photo_selector, R.string.input_panel_photo, true);
    }
    public ImageAction(SessionCustomization customization) {
        super(R.drawable.nim_message_plus_photo_selector, R.string.input_panel_photo, true,customization);
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());



        sendMessage(message);
    }
}

