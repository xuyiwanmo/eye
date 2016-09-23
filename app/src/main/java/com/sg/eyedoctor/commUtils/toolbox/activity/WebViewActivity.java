package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_web_view)
public class WebViewActivity extends BaseActivity {

    @ViewInject(R.id.eye_detail_wv)
    private WebView mWebView;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private String mTitle;
    private String mUrl;

    @Override
    protected void initView() {
        mTitle=getIntent().getStringExtra(ConstantValues.KEY_TITLE);
        mUrl=getIntent().getStringExtra(ConstantValues.KEY_URL);
        //WebView加载web资源

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl(mUrl);


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(mTitle);
    }


    protected void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }
        super.onPause();
    }

}
