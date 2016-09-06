package com.sg.eyedoctor.common.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.netease.nim.DemoCache;
import com.netease.nim.common.util.string.MD5;
import com.netease.nim.config.preference.Preferences;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;


/**
 * 网易云信Manager
 */
public class ImManager extends BaseManager {

    private AbortableFuture<LoginInfo> mLoginRequest;

    public ImManager(Context context) {
        super(context);
    }

    private String mToken;
    private String mAccount;


    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 mToken
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(mContext);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void login(String account, String password, RequestCallback<LoginInfo> callback) {
        //设置的云信密码统一为123456
        password = "123456";
        this.mAccount = account;
        this.mToken = tokenFromPassword(password);
        mLoginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, mToken));
        mLoginRequest.setCallback(callback);
    }

    public void onLoginDone() {
        mLoginRequest = null;
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    public void saveAccount() {
        DemoCache.setAccount(mAccount);
        //saveLoginInfo(mAccount, mToken);
    }
}
