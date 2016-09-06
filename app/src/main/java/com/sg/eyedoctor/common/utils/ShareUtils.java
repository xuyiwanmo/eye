package com.sg.eyedoctor.common.utils;

import android.app.Activity;
import android.graphics.BitmapFactory;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.view.ShareDialog;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ShareUtils {

    public static void youmengShare(Activity context,UMShareListener listener,String content,String title,String web) {

        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };

        //自定义dialog
        ShareDialog.Builder builder = new ShareDialog.Builder(context);
        Config.dialog = builder.create();

        //图片
        UMImage image = new UMImage(context,
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        new ShareAction(context).setDisplayList(displaylist)
                .withText(content)//"这是内容这是内容这是内容"
                .withTitle(title)//"这是标题这是标题这是标题"
                .withTargetUrl(web)//"http://www.baidu.com"
                .withMedia(image)
                .setListenerList(listener)
                .open();
    }
}
