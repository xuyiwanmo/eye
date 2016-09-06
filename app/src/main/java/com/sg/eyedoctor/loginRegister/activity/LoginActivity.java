package com.sg.eyedoctor.loginRegister.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.common.ui.popupmenu.PopupMenuItem;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.Doctor;
import com.sg.eyedoctor.common.bean.Vas;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.manager.ImManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.VersionUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.main.activity.MainActivity;
import com.sg.eyedoctor.settings.mySetting.activity.VersionUpdateActivity;
import com.sg.eyedoctor.settings.mySetting.bean.UpdateVersion;
import com.umeng.analytics.MobclickAgent;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.et_login_account)
    private EditText mAccountEt;
    @ViewInject(R.id.et_login_password)
    private EditText mPasswordEt;
    @ViewInject(R.id.ensure_tv)
    private TextView mLoginTv;
    @ViewInject(R.id.version_tv)
    private TextView mVersionTv;
    @ViewInject(R.id.tv_register_now)
    private TextView mRegisterNowTv;
    @ViewInject(R.id.tv_forget_password)
    private TextView mForgetPasswordTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private ImManager mImManager;
    private NIMPopupMenu popupMenu;
    private List<PopupMenuItem> menuItemList;

    private UpdateVersion mVersion;
    private boolean mNeedUpdate = false;
    private boolean mPasswordClear = false;

    private AlertDialog mLogoutDialog;
    private View dlgView;

    private boolean mImLogin = true;
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<Doctor>>() {
                }.getType();
                BaseResp<Doctor> res = new Gson().fromJson(result, objectType);

                Doctor doctor = res.value;
                doctor.Id = 1;
                ArrayList<Vas> vasList = doctor.VasSet;
                for (Vas vas : vasList) {
                    switch (vas.type) {
                        case "1":
                            doctor.textId = vas.id;
                            doctor.textIsOpen = vas.isOpen;
                            doctor.textPrice = vas.price;
                            break;
                        case "2":
                            doctor.phoneId = vas.id;
                            doctor.phoneIsOpen = vas.isOpen;
                            doctor.phonePrice = vas.price;
                            break;
                        case "3":
                            doctor.videoId = vas.id;
                            doctor.videoIsOpen = vas.isOpen;
                            doctor.videoPrice = vas.price;
                            break;
                        case "4":
                            doctor.addId = vas.id;
                            doctor.addIsOpen = vas.isOpen;
                            doctor.addPrice = vas.price;
                            break;

                    }
                }
                try {
                    x.db().delete(Doctor.class);
                    x.db().saveBindingId(doctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                mImManager.login("d" + doctor.loginid, doctor.password, mLoginCallback);


            } else {
                closeDialog();
                showToast(CommonUtils.getMsg(result));
                mPasswordEt.setText("");
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    //查询网络更新
    private NetCallback mVersionCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            //友盟集成下载点击
            MobclickAgent.onEvent(mContext, getString(R.string.umeng_download));
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<UpdateVersion>>() {
                }.getType();
                BaseResp<UpdateVersion> res = new Gson().fromJson(result, objectType);

                mVersion = res.value;

                if (Integer.valueOf(mVersion.version) > VersionUtils.getVersionCode(mContext)) {
                    //   mVersionMtv.setOtherTv("新版本" + mVersion.version);
                    DialogManager.showConfimCancelDlg(mContext, R.string.update_version_now, new DlgClick() {
                        @Override
                        public boolean click() {
                            //友盟集成下载点击
                            MobclickAgent.onEvent(mContext, getString(R.string.umeng_download));
                            Intent intent = new Intent(mContext, VersionUpdateActivity.class);
                            intent.putExtra("version", mVersion);
                            intent.putExtra("update", mNeedUpdate);
                            intent.putExtra(ConstantValues.KEY_TYPE, 1);
                            startActivity(intent);
                            return false;
                        }
                    }, new DlgClick() {
                        @Override
                        public boolean click() {
                            return false;
                        }
                    });
                    mNeedUpdate = true;
                } else {
                    // mVersionMtv.setOtherTv("已是最新版本");
                }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private RequestCallback<LoginInfo> mLoginCallback = new RequestCallback<LoginInfo>() {
        @Override
        public void onSuccess(LoginInfo param) {

            mImManager.onLoginDone();
            mImManager.saveAccount();

            // 初始化消息提醒
            NIMClient.toggleNotification(true);
            closeDialog();
            startActivity(new Intent(mContext, MainActivity.class));
            AppManager.getAppManager().finishActivity();

        }

        @Override
        public void onFailed(int code) {
            closeDialog();
            mImManager.onLoginDone();
            if (code == 302 || code == 404) {
                showToast("帐号或密码错误");
            } else {
                showToast("登录失败: " + code);
            }
        }

        @Override
        public void onException(Throwable exception) {
            LogUtils.i("云信  login failed" + exception.toString());
            closeDialog();
            mImManager.onLoginDone();
        }

    };

    @Override
    protected void initView() {
        mPasswordClear = getIntent().getBooleanExtra(ConstantValues.KEY_PASSWORD, false);
        if (mDoctor != null) {
            mAccountEt.setText(mDoctor.loginid + "");
            if (!mPasswordClear) {
                mPasswordEt.setText(mDoctor.password + "");
            }

        }
        mImManager = new ImManager(mContext);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("版本:").append(VersionUtils.getVersionCode(mContext));
        mVersionTv.setText(stringBuffer.toString());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.user_login);
        mActionbar.setRightBtnImg(R.drawable.ic_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopuptWindow(mContext, v, "0", null);
            }
        });
        mActionbar.setRightBtnVisible(View.INVISIBLE);
    }

    @Event(R.id.ensure_tv)
    private void login(View view) {
        showdialog();
        String name = getText(mAccountEt);
        String password = getText(mPasswordEt);
        BaseManager.login(name, password, mCallback);
        KeyBoardUtils.hideKeyboard(mPasswordEt);
    }

    @Event(R.id.tv_forget_password)
    private void forgetPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));

//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogStyle);
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        //获得window窗口的属性
//        android.view.WindowManager.LayoutParams lp = window.getAttributes();
//        //设置dialog在屏幕底部
//        window.setGravity(Gravity.BOTTOM);
//        //设置窗口宽度为充满全屏
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        //设置窗口高度为包裹内容
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        //将设置好的属性set回去
//        window.setAttributes(lp);
//        final PasswordView view1 = new PasswordView(mContext, "&&&&");
//        view1.setOnFinishInput(new OnPasswordInputFinish() {
//            @Override
//            public void inputFinish() {
//
//                showToast("输入错误" + view1.getStrPassword());
//                view1.clear();
//            }
//        });
//        dialog.setContentView(view1);
    }

    @Event(R.id.tv_register_now)
    private void registerNow(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public static void start(Context context, boolean passwordClear) {
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(ConstantValues.KEY_PASSWORD, passwordClear);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    //弹出菜单
    private void initPopuptWindow(Context context, View view, String sessionId, SessionTypeEnum sessionTypeEnum) {
        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(context, menuItemList, listener);
        }
        menuItemList.clear();
        menuItemList.addAll(getMoreMenuItems(context, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private NIMPopupMenu.MenuItemClickListener listener = new NIMPopupMenu.MenuItemClickListener() {
        @Override
        public void onItemClick(final PopupMenuItem item) {
            switch (item.getTag()) {
                case ConstantValues.WIFI:
                    ConstantValues.HOST = ConstantValues.WIFI_HOST;
                    ConstantValues.IMG_HOST = ConstantValues.WIFI_IMG_HOST;
                    showToast("已切换到公司内网");
                    break;
                case ConstantValues.NET:
                    ConstantValues.HOST = ConstantValues.NET_HOST;
                    ConstantValues.IMG_HOST = ConstantValues.NET_IMG_HOST;
                    showToast("已切换到公司外网");
                    break;
                case ConstantValues.SERVER:
                    ConstantValues.HOST = ConstantValues.SERVER_HOST;
                    ConstantValues.IMG_HOST = ConstantValues.SERVER_IMG_HOST;
                    showToast("已切换到医院服务器");
                    break;
            }
        }
    };

    private List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        moreMenuItems.add(new PopupMenuItem(context, ConstantValues.WIFI, sessionId,
                sessionTypeEnum, "内网"));
        moreMenuItems.add(new PopupMenuItem(context, ConstantValues.NET, sessionId,
                sessionTypeEnum, "外网"));
        moreMenuItems.add(new PopupMenuItem(context, ConstantValues.SERVER, sessionId,
                sessionTypeEnum, "服务器"));

        return moreMenuItems;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //查询新版本
        showdialog();
        BaseManager.getLastestVersion(mVersionCallback);
    }
}
