package com.sg.eyedoctor.settings.mySetting.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.ShareUtils;
import com.sg.eyedoctor.settings.mySetting.response.ShareResult;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

@ContentView(R.layout.activity_share_app)
public class ShareAppActivity extends BaseActivity {

    @ViewInject(R.id.share_url_img)
    private ImageView mShareImg;
    @ViewInject(R.id.share_number_tv)
    private TextView mShareNumTv;
    @ViewInject(R.id.share_url_tv)
    private TextView mShareUrlTv;

    private ShareResult mResult;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<ShareResult>>() {
                }.getType();
                BaseResp<ShareResult> res = new Gson().fromJson(result, objectType);
                mResult = res.value;
                CommonUtils.loadImgformat(mResult.url,mShareImg);
                mShareNumTv.setText("邀请码: "+mResult.id);

            } else {

            }
        }

        @Override
        protected void timeOut() {

        }
    };

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.shareApp(mCallback);
    }

    @Event(R.id.share_url_tv)
    private void shareUrl(View view) {

        if(mResult!=null){
            String url="http://www.yanketong.com/H5/InviteCode.aspx?id="+mDoctor.id+"&type=1";
            ShareUtils.youmengShare(ShareAppActivity.this, new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA media) {

                }

                @Override
                public void onError(SHARE_MEDIA media, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA media) {

                }
            },getString(R.string.eye_doctor_introduce),getString(R.string.eye_doctor_download),url);
        }
    }

}
