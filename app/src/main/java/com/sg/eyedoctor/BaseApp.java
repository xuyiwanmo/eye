package com.sg.eyedoctor;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.netease.nim.DemoCache;
import com.netease.nim.ImageLoaderKit;
import com.netease.nim.NimUIKit;
import com.netease.nim.avchat.AVChatProfile;
import com.netease.nim.avchat.activity.AVChatActivity;
import com.netease.nim.cache.NimUserInfoCache;
import com.netease.nim.cache.TeamDataCache;
import com.netease.nim.common.util.sys.SystemUtil;
import com.netease.nim.config.preference.Preferences;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.extension.CustomAttachParser;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatRingerConfig;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sg.eyedoctor.common.utils.CrashHander;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.main.activity.MainActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;

import org.xutils.DbManager;
import org.xutils.x;


/**
 * 初始化云信,ImageLoader,讯飞语音,友盟,xutils
 */
public class BaseApp extends Application {
    private static BaseApp sInstance = null;
    private DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("doctor.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbVersion(4)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            });

    public static BaseApp getApp() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //xutils初始化
        x.Ext.init(this, daoConfig);
        x.Ext.setDebug(true); // 是否输出debug日志

        //ImageLoader初始化
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);

        //讯飞语音初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56c2d805");

        //网易云信SDK初始化
        DemoCache.setContext(this);
        NIMClient.init(this, getLoginInfo(), options());

        if (inMainProcess()) {
            initUIKit();
            NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
                @Override
                public void onEvent(CustomNotification message) {
                    LogUtils.i(message.getContent().toString());
                }
            }, true);
        }

        //友盟分享初始化
        PlatformConfig.setQQZone("1105421058", "49Xh8mwY7z7EKATc");
        PlatformConfig.setWeixin("wx516bad16f20270ba", "dbd2aff592856d43623b95624a5f7cc5");
        MobclickAgent.setDebugMode(true);

        //友盟推送初始化
        umPushInit();

        //crash处理
        CrashHander crashHandler = CrashHander.getInstance();
        //crashHandler.init(getApplicationContext());

    }

    // *********************************************云信参数配置 start***********************************************
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        options.statusBarNotificationConfig = config;

        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;
        options.preloadAttach = true;
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.ic_case_detail_add;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        NimUIKit.init(this, infoProvider);
        SessionHelper.init();
        enableAVChat();
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }
            return user;
        }

        @Override
        public int getDefaultIconResId() {
            return R.drawable.ic_patient_head;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            Drawable drawable = getResources().getDrawable(R.drawable.nim_avatar_group);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            UserInfo user = getUserInfo(account);
            return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = NimUserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = NimUserInfoCache.getInstance().getAlias(account);
                }
            }
            if (TextUtils.isEmpty(nick)) {
                return null;
            }
            return nick;
        }
    };

    private void setupAVChat() {
        AVChatRingerConfig config = new AVChatRingerConfig();
        config.res_connecting = R.raw.avchat_connecting;
        config.res_no_response = R.raw.avchat_no_response;
        config.res_peer_busy = R.raw.avchat_peer_busy;
        config.res_peer_reject = R.raw.avchat_peer_reject;
        config.res_ring = R.raw.avchat_ring;
        AVChatManager.getInstance().setRingerConfig(config); // 设置铃声配置
    }

    private void enableAVChat() {
        setupAVChat();
        registerAVChatIncomingCallObserver(true);
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatActivity.launch(DemoCache.getContext(), data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }
    // *********************************************云信参数配置 end***********************************************


    /**
     * 友盟推送初始化
     */
    private void umPushInit() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

}



