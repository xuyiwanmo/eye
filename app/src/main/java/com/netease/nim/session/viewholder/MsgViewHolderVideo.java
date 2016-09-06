package com.netease.nim.session.viewholder;

//import com.netease.nim.uikit.R;
//import com.netease.nim.uikit.common.util.media.BitmapDecoder;
//import com.netease.nim.uikit.session.activity.WatchVideoActivity;

import com.netease.nim.common.ui.media.BitmapDecoder;
import com.netease.nim.session.activity.WatchVideoActivity;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.sg.eyedoctor.R;

/**
 * Created by zhoujianghua on 2015/8/5.
 */
public class MsgViewHolderVideo extends MsgViewHolderThumbBase {

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_video;
    }

    @Override
    protected void onItemClick() {
        WatchVideoActivity.start(context, message);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        VideoAttachment attachment = (VideoAttachment) message.getAttachment();
        String thumb = attachment.getThumbPathForSave();
        return BitmapDecoder.extractThumbnail(path, thumb) ? thumb : null;
    }
}
