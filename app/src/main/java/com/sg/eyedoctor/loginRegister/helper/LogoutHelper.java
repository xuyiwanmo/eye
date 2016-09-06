package com.sg.eyedoctor.loginRegister.helper;


import com.netease.nim.DemoCache;
import com.netease.nim.LoginSyncDataStatusObserver;
import com.netease.nim.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;


/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NIMClient.getService(AuthService.class).logout();
        NimUIKit.clearCache();

        DemoCache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
    }
}
