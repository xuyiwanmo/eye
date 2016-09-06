package com.netease.nim;

import android.content.Context;
import android.location.LocationProvider;
import android.text.TextUtils;

import com.netease.nim.cache.DataCacheManager;
import com.netease.nim.common.util.storage.StorageType;
import com.netease.nim.common.util.storage.StorageUtil;
import com.netease.nim.common.util.sys.ScreenUtil;

import com.netease.nim.session.SessionEventListener;
import com.netease.nim.session.emoji.StickerManager;
import com.netease.nim.session.viewholder.MsgViewHolderBase;
import com.netease.nim.session.viewholder.MsgViewHolderFactory;
import com.netease.nim.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.List;

//import com.netease.nim.uikit.cache.DataCacheManager;
//import com.netease.nim.uikit.cache.TeamDataCache;
//import com.netease.nim.uikit.common.util.log.LogUtil;
//import com.netease.nim.uikit.common.util.storage.StorageType;
//import com.netease.nim.uikit.common.util.storage.StorageUtil;
//import com.netease.nim.uikit.common.util.sys.ScreenUtil;
//import com.netease.nim.uikit.contact.ContactEventListener;
//import com.netease.nim.uikit.contact.ContactProvider;
//import com.netease.nim.uikit.contact_selector.activity.ContactSelectActivity;
//import com.netease.nim.uikit.session.SessionCustomization;
//import com.netease.nim.uikit.session.SessionEventListener;
//import com.netease.nim.uikit.session.activity.P2PMessageActivity;
//import com.netease.nim.uikit.session.activity.TeamMessageActivity;
//import com.netease.nim.uikit.session.emoji.StickerManager;
//import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
//import com.netease.nim.uikit.session.viewholder.MsgViewHolderFactory;
//import com.netease.nim.uikit.team.activity.AdvancedTeamInfoActivity;
//import com.netease.nim.uikit.team.activity.NormalTeamInfoActivity;
//import com.netease.nim.uikit.uinfo.UserInfoHelper;

/**
 * UIKit能力输出类。
 */
public final class NimUIKit {

    // context
    private static Context context;

    // 自己的用户帐号
    private static String account;

    // 用户信息提供者
    private static UserInfoProvider userInfoProvider;



    // 地理位置信息提供者
    private static LocationProvider locationProvider;

    // 图片加载、缓存与管理组件
    private static ImageLoaderKit imageLoaderKit;

    // 会话窗口消息列表一些点击事件的响应处理函数
    private static SessionEventListener sessionListener;


    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context          上下文
     * @param userInfoProvider 用户信息提供者
     */
    public static void init(Context context, UserInfoProvider userInfoProvider) {
        NimUIKit.context = context.getApplicationContext();
        NimUIKit.userInfoProvider = userInfoProvider;

        NimUIKit.imageLoaderKit = new ImageLoaderKit(context, null);

        // init data cache
        LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);  // 监听登录同步数据完成通知
        DataCacheManager.observeSDKDataChanged(true);
        if (!TextUtils.isEmpty(getAccount())) {
            DataCacheManager.buildDataCache(); // build data cache on auto login
        }

        // init tools
        StorageUtil.init(context, null);
        ScreenUtil.init(context);
        StickerManager.getInstance().init();

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);

    }

    /**
     * 释放缓存，一般在注销时调用
     */
    public static void clearCache() {
        DataCacheManager.clearDataCache();
    }






    public static Context getContext() {
        return context;
    }

    public static String getAccount() {
        return account;
    }


    public static ImageLoaderKit getImageLoaderKit() {
        return imageLoaderKit;
    }



    /**
     * 根据消息附件类型注册对应的消息项展示ViewHolder
     *
     * @param attach     附件类型
     * @param viewHolder 消息ViewHolder
     */
    public static void registerMsgItemViewHolder(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        MsgViewHolderFactory.register(attach, viewHolder);
    }

    /**
     * 注册Tip类型消息项展示ViewHolder
     * @param viewHolder Tip消息ViewHolder
     */
    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
        MsgViewHolderFactory.registerTipMsgViewHolder(viewHolder);
    }

    /**
     * 设置当前登录用户的帐号
     *
     * @param account 帐号
     */
    public static void setAccount(String account) {
        NimUIKit.account = account;
    }

    /**
     * 获取聊天界面事件监听器
     *
     * @return
     */
    public static SessionEventListener getSessionListener() {
        return sessionListener;
    }

    /**
     * 设置聊天界面的事件监听器
     *
     * @param sessionListener
     */
    public static void setSessionListener(SessionEventListener sessionListener) {
        NimUIKit.sessionListener = sessionListener;
    }





    /**
     * 当用户资料发生改动时，请调用此接口，通知更新UI
     *
     * @param accounts 有用户信息改动的帐号列表
     */
    public static void notifyUserInfoChanged(List<String> accounts) {
        UserInfoHelper.notifyChanged(accounts);
    }

    public static UserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }
}
