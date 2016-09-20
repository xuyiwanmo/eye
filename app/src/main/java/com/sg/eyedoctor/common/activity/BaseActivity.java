package com.sg.eyedoctor.common.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.internetConsult.activity.InternetConsultDetailActivity;
import com.sg.eyedoctor.commUtils.internetConsult.bean.InternetConsult;
import com.sg.eyedoctor.commUtils.plusManager.activity.PlusManagerActivity;
import com.sg.eyedoctor.common.bean.Doctor;
import com.sg.eyedoctor.common.bean.PushBean;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetworkUtil;
import com.sg.eyedoctor.consult.phoneConsult.activity.PhoneConsultActivity;
import com.sg.eyedoctor.consult.textConsult.activity.TextConsultActivity;
import com.sg.eyedoctor.consult.videoConsult.activity.VideoConsultActivity;
import com.sg.eyedoctor.eyeCircle.activity.CaseDetailActivity;
import com.sg.eyedoctor.eyeCircle.bean.TopicCase;
import com.sg.eyedoctor.loginRegister.activity.LoginActivity;
import com.sg.eyedoctor.loginRegister.helper.LogoutHelper;
import com.sg.eyedoctor.main.activity.CertificationActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类
 */
public abstract class BaseActivity extends SimpleActivity {

    /**
     *弹出对话框
     */
    private ProgressDialog mProgressDialog;
    /**
     * notification的id
     */
    private int mNotifyId = 1;
    /**
     * 当列表为空,显示的Text
     */
    protected TextView mEmptyTv;
    /**
     * 账号被踢dialog
     */
    public AlertDialog mLogoutDialog;
    /**
     * imageLoader参数配置
     */
    public DisplayImageOptions mImageOptions;
    /**
     * 数据库获取的医生
     */
    public static Doctor mDoctor;
    /**
     * 是否认证
     */
    public boolean mIsAuth = false;
    public View dlgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDoctor = BaseManager.getDoctor();
        //显示图片的配置
        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        initView();
        initActionbar();
        initListener();
    }

    /**
     * 等待对话框
     */
    public void showdialog() {
        if (!NetworkUtil.isNetAvailable(this)) {
            showToast("网络未连接,请连接网络后再查询!");
            return;
        }
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在操作,请稍后······");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭对话框
     */
    public void closeDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected String getText(TextView editText) {
        return editText.getText().toString().trim();
    }

    protected String getText(String text, String emptyStr) {
        if (text == null) {
            return emptyStr;
        }
        return text;
    }

    protected String getText(TextView text, String emptyStr) {
        if (text.getText().toString() == null) {
            return emptyStr;
        }
        return emptyStr;
    }


    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int stringId) {
        Toast.makeText(this, getText(stringId), Toast.LENGTH_SHORT).show();
    }

    public void setTextView(TextView tv, String str) {
        if (str == null || str.equals("null")) {
            tv.setText("");
        } else {
            tv.setText(str);
        }
    }

    public void setTextView(TextView tv, String str, String emptyStr) {
        if (str == null || str.equals("null") || str.trim().equals("")) {
            tv.setText(emptyStr);
        } else {
            tv.setText(str);
        }
    }
    public int getcolor(int id) {
        return getResources().getColor(id);
    }
    /**
     * 网络连接超时统一处理
     */
    public void onTimeOut() {
        closeDialog();
        showToast(R.string.verticode_time_out);
        logout();
        AppManager.getAppManager().finishAllActivity();
        LoginActivity.start(this);
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        mDoctor = BaseManager.getDoctor();
        if (mDoctor != null && mDoctor.state == 2) {
            mIsAuth = true;
        } else {
            mIsAuth = false;
        }
        registerObserver(true);

    }

    public void startAuthActivity(String title) {
        Intent intent = new Intent(mContext, CertificationActivity.class);
        intent.putExtra(ConstantValues.KEY_TITLE, title);
        startActivity(intent);
    }

    public void startAuthActivity(int titleId) {
        startAuthActivity(getResources().getString(titleId));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
        registerObserver(false);
    }

    public void logout() {
        LogoutHelper.logout();
    }

    // public abstract void showKilloutDlg();

    public void registerObserver(boolean isRegister) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                mStatusCodeObserver, isRegister);

        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(pushObserver, isRegister);
    }


    public void showKickOutDlg(Context context) {
        String content = "您的账号于s在另一台手机登录。如非本人操作，则密码可能已经泄露，建议修改密码。";
        String time = CommonUtils.longToHM(System.currentTimeMillis());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (dlgView == null || mLogoutDialog == null) {

            mLogoutDialog = builder.create();
            dlgView = View.inflate(mContext
                    , R.layout.dialog_confirm_cancel, null);

            ((TextView) dlgView.findViewById(R.id.content_tv)).setText(content.replace("s", time));

            ((TextView) dlgView.findViewById(R.id.dlg_title_tv)).setText(R.string.logout_notice);

            ((TextView) dlgView.findViewById(R.id.confirm_tv)).setText(R.string.logout);
            ((TextView) dlgView.findViewById(R.id.cancel_tv)).setText(R.string.login_again);

            dlgView.findViewById(R.id.confirm_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mLogoutDialog.dismiss();
                    logout();
                    AppManager.getAppManager().finishAllActivity();
                }
            });
            dlgView.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mLogoutDialog.dismiss();
                    logout();
                    LoginActivity.start(mContext, false);
                }
            });
        }

        if (!mLogoutDialog.isShowing()) {
            mLogoutDialog.show();
            mLogoutDialog.setContentView(dlgView);
        }
    }

    private Observer<StatusCode> mStatusCodeObserver = new Observer<StatusCode>() {
        public void onEvent(StatusCode status) {
            if (status.wontAutoLogin()) {
                showKickOutDlg(mContext);
            }
        }
    };

    private Observer<CustomNotification> pushObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            String str = message.getContent();
            if (!str.contains("type")) {
                agreePush(str);
                return;
            }
            LogUtils.i("push", str);

            PushBean pushBean = new Gson().fromJson(str, PushBean.class);

            switch (Integer.valueOf(pushBean.type)) {
                case ConstantValues.PUSH_TEXT_CONSULT:
                    sendNotification(TextConsultActivity.class, R.string.text_consult, "有新患者向您购买了图文问诊服务。", null);
                    break;

                case ConstantValues.PUSH_VIDEO_CONSULT:
                    sendNotification(VideoConsultActivity.class, R.string.video_consult, "有新患者向您购买了视频问诊服务。", null);
                    break;

                case ConstantValues.PUSH_PHONE_CONSULT:
                    sendNotification(PhoneConsultActivity.class, R.string.phone_consult, "有新患者向您购买了电话问诊服务。", null);
                    break;

                case ConstantValues.PUSH_PLUS:
                    sendNotification(PlusManagerActivity.class, R.string.add_manager, "有新患者向您购买了加号管理服务。", null);
                    break;

                case ConstantValues.PUSH_INTERNET_CONSULT:

                    Bundle internet_bundle = new Bundle();
                    InternetConsult consult = new InternetConsult();
                    consult.id = pushBean.id;
                    internet_bundle.putParcelable(ConstantValues.KEY_DATA, consult);

                    sendNotification(InternetConsultDetailActivity.class, R.string.internet_consult, "您发布的消息有新的评论。", internet_bundle);
                    break;

                case ConstantValues.PUSH_EYE_CIRCLE:
                    Bundle circle_bundle = new Bundle();
                    TopicCase topicCase = new TopicCase();
                    topicCase.topicId = pushBean.id;
                    circle_bundle.putParcelable(ConstantValues.KEY_DATA, topicCase);

                    sendNotification(CaseDetailActivity.class, R.string.eye_circle, "您发布的消息有新的评论。", circle_bundle);
                    break;

            }
        }
    };

    private void agreePush(String str) {
        String id = str.split(":")[0];
        String orderNo = str.split(":")[1];

        switch (id) {
            case "1":
                patientPushAgree(orderNo);
                break;

            case "2":
                patientPushRefuse(orderNo);
                break;

            case "0":
                DialogManager.showConfimDlg(this, R.string.patient_end, new DlgClick() {
                    @Override
                    public boolean click() {
                       AppManager.getAppManager().finishActivity();
                        return false;
                    }
                });
                break;
        }
    }

    public void patientPushRefuse(String orderNo) {

    }


    private void sendNotification(Class activity, int title, String content, Bundle bundle) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyId++;
        Intent intent = new Intent(mContext, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        //定义notification
        Notification n = new Notification(R.drawable.ic_launcher, content, System.currentTimeMillis());
        n.flags = Notification.FLAG_AUTO_CANCEL;
        n.number = mNotifyId;

        PendingIntent pi = PendingIntent.getActivity(mContext, mNotifyId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //具体的通知内容
        n.setLatestEventInfo(mContext, getString(title), content, pi);
        //执行通知
        mNotificationManager.notify(mNotifyId, n);

    }

    public void patientPushAgree(String orderNo){

    }


}
