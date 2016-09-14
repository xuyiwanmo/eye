package com.sg.eyedoctor.commUtils.academic.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.academic.bean.Academic;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.ShareUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 学术前沿webview
 */
@ContentView(R.layout.activity_academic_web)
public class AcademicWebActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.academic_wv)
    private WebView mAcademicWv;
    private Academic mAcademic;
    private boolean mIsCollect = false;
    private String title;
    private NetCallback mStoreCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.store_ok);
                mIsCollect = true;
                mActionbar.getSecondBtnImg().setSelected(mIsCollect);
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mCancelCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast("取消收藏成功");
                mIsCollect = false;
                mActionbar.getSecondBtnImg().setSelected(mIsCollect);
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    //判断是否收藏
    private NetCallback mJudgeStoreCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<Integer>>() {
                }.getType();
                BaseResp<Integer> res = new Gson().fromJson(result, objectType);

                if (res.value == 0) {//未收藏
                    mIsCollect = false;
                } else {
                    mIsCollect = true;
                }
                mActionbar.getSecondBtnImg().setSelected(mIsCollect);
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {
        mAcademic = (Academic) getIntent().getSerializableExtra(ConstantValues.KEY_URL);
        title=mAcademic.newsTitle;
        WebSettings webSettings = mAcademicWv.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptEnabled(true);
        mAcademicWv.loadUrl(mAcademic.url);
        mActionbar.setSecondbtnVisible(View.VISIBLE);
        mActionbar.setTitle(R.string.news_detail);

        mActionbar.setRightBtnImg(R.drawable.ic_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.youmengShare(AcademicWebActivity.this, new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        showToast(share_media + "分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        showToast(share_media + "分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        showToast(share_media + "分享取消");
                    }
                },title,"眼科通学术前沿",mAcademic.url);
            }
        });
        mActionbar.setSecondBtnImg(R.drawable.selector_chart_store, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String picture = "";
                showdialog();
                if (mIsCollect) {//收藏->取消
                    BaseManager.collectionCancel(mAcademic.newsId + "", mCancelCallback);
                } else {//未收藏->收藏
                    BaseManager.collectionAdd(mAcademic.newsId, mDoctor.id, "1", mAcademic.newsTitle, picture, mAcademic.url, mStoreCallback);
                }
            }
        });
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
        BaseManager.isCollection(mAcademic.newsId, mJudgeStoreCallback);
    }
}
