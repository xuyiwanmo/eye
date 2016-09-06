package com.sg.eyedoctor.settings.mySetting.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.VersionUtils;
import com.sg.eyedoctor.settings.mySetting.bean.UpdateVersion;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.concurrent.Executor;

@ContentView(R.layout.activity_version_update)
public class VersionUpdateActivity extends BaseActivity {

    @ViewInject(R.id.new_version_tv)
    private TextView mNewVersionTv;
    @ViewInject(R.id.curren_version_tv)
    private TextView mCurrenVersionTv;
    @ViewInject(R.id.ensure_tv)
    private TextView mEnsureTv;
    @ViewInject(R.id.update_pb)
    private ProgressBar mUpdatePb;
    @ViewInject(R.id.update_rl)
    private RelativeLayout mUpdateRl;
    private UpdateVersion mVersion;
    private boolean mNeedUpdate;
    private final static int MAX_DOWNLOAD_THREAD = 2; // 有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
    private final Executor executor = new PriorityExecutor(MAX_DOWNLOAD_THREAD, true);
    private int index = 0;

    @Override
    protected void initView() {
        mVersion = (UpdateVersion) getIntent().getSerializableExtra(ConstantValues.KEY_VERSION);
        mNeedUpdate = getIntent().getBooleanExtra(ConstantValues.KEY_UPDATE, false);
        int type = getIntent().getIntExtra(ConstantValues.KEY_TYPE, 0);
        if (mNeedUpdate) {
            mNewVersionTv.setText("发现新版本:" + mVersion.version);
            mCurrenVersionTv.setText("当前版本" + VersionUtils.getVersionCode(mContext));
            mUpdateRl.setVisibility(View.VISIBLE);
            mEnsureTv.setVisibility(View.VISIBLE);
            mUpdatePb.setVisibility(View.INVISIBLE);
        } else {
            mNewVersionTv.setText("当前已是最新版本");
            mUpdateRl.setVisibility(View.INVISIBLE);
        }
        if (type == 1) {
            update(mEnsureTv);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.ensure_tv)
    private void update(View view) {
        mUpdatePb.setVisibility(View.VISIBLE);
        mEnsureTv.setVisibility(View.GONE);
        mUpdatePb.setMax(100);
        startDownload();
    }

    private void startDownload() {
        //友盟集成下载点击
        MobclickAgent.onEvent(mContext, getString(R.string.umeng_download));

        DownLoadCallback downLoadCallback = new DownLoadCallback();

        RequestParams params = new RequestParams(mVersion.downLoadUrl);

        params.setAutoResume(true);
        params.setAutoRename(true);
        File file = Environment.getExternalStorageDirectory();
        //如果此文件已存在先删除
        file.delete();// 删除文件
        params.setSaveFilePath(file.getPath() + "/download");
        params.setExecutor(executor);
        params.setCancelFast(true);

        Callback.Cancelable cancelable = x.http().get(params, downLoadCallback);

    }


    class DownLoadCallback implements Callback.CommonCallback<File>,
            Callback.ProgressCallback<File>,
            Callback.Cancelable {

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            int value = (int) (current * 100 / total);
            LogUtils.i("值" + value);
            mUpdatePb.setProgress(value);
        }

        @Override
        public void onSuccess(File result) {
            showToast("开始安装");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(result),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }
}
