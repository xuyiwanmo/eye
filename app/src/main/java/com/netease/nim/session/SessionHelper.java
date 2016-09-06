package com.netease.nim.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.DemoCache;
import com.netease.nim.NimUIKit;
import com.netease.nim.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.session.action.AVChatAction;
import com.netease.nim.session.action.BaseAction;
import com.netease.nim.session.activity.MessageHistoryActivity;
import com.netease.nim.session.extension.CustomAttachParser;
import com.netease.nim.session.extension.PatientAttachment;
import com.netease.nim.session.extension.StickerAttachment;
import com.netease.nim.session.viewholder.MsgViewHolderAVChat;
import com.netease.nim.session.viewholder.MsgViewHolderPatient;
import com.netease.nim.team.model.TeamExtras;
import com.netease.nim.team.model.TeamRequestCode;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.bean.Doctor;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.consult.advice.activity.ConsultDetailActivity;
import com.sg.eyedoctor.consult.advice.activity.ConsultPatientDetailActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticShowActivity;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.helpUtils.freeConsult.bean.FreePatient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {

    private static final int ACTION_PATIENT_DETAIL = 0;
    private static final int ACTION_DIAGNOSIS_ADVICE = 1;
    private static final int ACTION_END = 2;
    private static final int ACTION_HISTORY = 3;

    private static SessionCustomization reportCustomization;
    private static SessionCustomization p2pCustomization;
    private static SessionCustomization freeP2pCustomization;
    private static SessionCustomization teamCustomization;
    private static SessionCustomization doctorP2pCustomization;

    private static NIMPopupMenu popupMenu;
    private static List<PopupMenuItem> menuItemList;

    public static Patient mPatient;
    public static FreePatient mFreePatient;
    public static PDValidate mPDValidate;
    public static FriendList mDoctor;

    public static int mType;

    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    public static SessionCustomization getTextP2pCustomization(final Patient patient, final int type) {
        mPatient = patient;
        mType = type;

        p2pCustomization = new SessionCustomization() {
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);
            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item) {
                return new StickerAttachment(category, item);
            }
        };

        p2pCustomization.withSticker = true;

        // 定制ActionBar右边的按钮，可以加多个
        ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
        SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId) {
                initPopuptWindow(context, view, sessionId, SessionTypeEnum.P2P);
            }
        };
        cloudMsgButton.iconId = R.drawable.ic_more_show;
        buttons.add(cloudMsgButton);

        p2pCustomization.buttons = buttons;

        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面
                //   UserProfileActivity.start(context, message.getFromAccount());
                Doctor doctor = BaseManager.getDoctor();
                if (!message.getFromAccount().equals("d" + doctor.loginid)) {
                    Intent intent = new Intent(context, ConsultDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_PATIENT, patient);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };

        NimUIKit.setSessionListener(listener);

        if (type == ConstantValues.P2P_TEXT_COMPLETE) {
            p2pCustomization.showComplete = true;
        } else {
            p2pCustomization.showComplete = false;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(ConstantValues.KEY_ORDER, patient.id);
        map.put(ConstantValues.KEY_SOURCE, ConstantValues.CHAT_TEXT + "");

        p2pCustomization.map = map;
        p2pCustomization.showName = patient.patientName;
        p2pCustomization.type = type;
        return p2pCustomization;
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    public static SessionCustomization getFreeP2pCustomization(final FreePatient patient, final int type) {
        mFreePatient = patient;
        mType = type;

        freeP2pCustomization = new SessionCustomization() {
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);
            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item) {
                return new StickerAttachment(category, item);
            }
        };

        // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
        ArrayList<BaseAction> actions = new ArrayList<>();
        actions.add(new AVChatAction(AVChatType.VIDEO,patient.patientId));
        freeP2pCustomization.actions = actions;

        freeP2pCustomization.withSticker = true;

        if (type == ConstantValues.FREE_COMPLETE) {
            freeP2pCustomization.showComplete = true;
        } else {
            freeP2pCustomization.showComplete = false;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(ConstantValues.KEY_ORDER, patient.questionId);
        map.put(ConstantValues.KEY_SOURCE, ConstantValues.CHAT_FREE + "");
        freeP2pCustomization.map = map;
        freeP2pCustomization.showName = patient.name;
        freeP2pCustomization.showAge = patient.age;
        freeP2pCustomization.showIll = patient.questionDetail;
        freeP2pCustomization.showSex = patient.sex;
        freeP2pCustomization.picList = patient.picList;
        freeP2pCustomization.isVideo = patient.isVideo;
        freeP2pCustomization.type = ConstantValues.P2P_FREE;//转诊
        return freeP2pCustomization;
    }

    // 与医生聊天
    public static SessionCustomization getDoctorP2pCustomization(final FriendList doctor) {
        mDoctor = doctor;

        doctorP2pCustomization = new SessionCustomization() {
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);
            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item) {
                return new StickerAttachment(category, item);
            }
        };

        doctorP2pCustomization.withSticker = true;

        Map<String, Object> map = new HashMap<>();
        map.put(ConstantValues.KEY_ORDER, "0");
        map.put(ConstantValues.KEY_SOURCE, ConstantValues.CHAT_DOCTOR + "");

        doctorP2pCustomization.map = map;
        doctorP2pCustomization.showName = doctor.userName;
        doctorP2pCustomization.type = ConstantValues.P2P_DOCTOR;
        return doctorP2pCustomization;
    }

    /**
     * 患者报到
     */
    public static SessionCustomization getReportCustomization(final PDValidate patient) {
        mPDValidate = patient;

        reportCustomization = new SessionCustomization() {
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);

            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item) {
                return new StickerAttachment(category, item);
            }
        };

        Map<String, Object> map = new HashMap<>();
        map.put(ConstantValues.KEY_ORDER, patient.id);
        map.put(ConstantValues.KEY_SOURCE, ConstantValues.CHAT_REPORT + "");

        reportCustomization.map = map;
        reportCustomization.showName = patient.name;
        reportCustomization.type = ConstantValues.P2P_REPORT;
        return reportCustomization;
    }

    public static SessionCustomization getTeamCustomization(String disId) {

        teamCustomization = new SessionCustomization() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                if (requestCode == TeamRequestCode.REQUEST_CODE) {
                    if (resultCode == Activity.RESULT_OK) {
                        String reason = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                        boolean finish = reason != null && (reason.equals(TeamExtras
                                .RESULT_EXTRA_REASON_DISMISS) || reason.equals(TeamExtras.RESULT_EXTRA_REASON_QUIT));
                        if (finish) {
                            activity.finish(); // 退出or解散群直接退出多人会话
                        }
                    }
                }
            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item) {
                return new StickerAttachment(category, item);
            }
        };

        // 定制ActionBar右边的按钮，可以加多个
        ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
        SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId) {
                initPopuptWindow(context, view, sessionId, SessionTypeEnum.Team);
            }
        };
        cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;

        teamCustomization.buttons = buttons;
        teamCustomization.withSticker = true;
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantValues.KEY_ORDER, disId);
        map.put(ConstantValues.KEY_SOURCE, ConstantValues.CHAT_CASE_DISCUSS+"");

        teamCustomization.map = map;
        return teamCustomization;
    }

    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(PatientAttachment.class, MsgViewHolderPatient.class);
        NimUIKit.registerMsgItemViewHolder(AVChatAttachment.class, MsgViewHolderAVChat.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
            }
        };

        NimUIKit.setSessionListener(listener);
    }

    private static void initPopuptWindow(Context context, View view, String sessionId, SessionTypeEnum sessionTypeEnum) {
        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(context, menuItemList, listener);
        }
        menuItemList.clear();
        menuItemList.addAll(getMoreMenuItems(context, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private static NIMPopupMenu.MenuItemClickListener listener = new NIMPopupMenu.MenuItemClickListener() {
        @Override
        public void onItemClick(final PopupMenuItem item) {
            switch (item.getTag()) {
                case ACTION_PATIENT_DETAIL:
                    Intent intent1 = new Intent(item.getContext(), ConsultPatientDetailActivity.class);
                    intent1.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                    item.getContext().startActivity(intent1);
                    break;
                case ACTION_HISTORY:

                    MessageHistoryActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum(), p2pCustomization); // 漫游消息查询
                    break;
                case ACTION_DIAGNOSIS_ADVICE:
                    Intent intent2 = new Intent(item.getContext(), DiagnosticShowActivity.class);
                    intent2.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                    intent2.putExtra(ConstantValues.KEY_TYPE, p2pCustomization.type);
                    intent2.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, true);
                    if (p2pCustomization.type == ConstantValues.P2P_TEXT_COMPLETE) {
                        intent2.putExtra(ConstantValues.KEY_END, true);
                    }
                    item.getContext().startActivity(intent2);
                    break;
                case ACTION_END:
                    String title = item.getContext().getString(R.string.end_consult).replace(" ", mPatient.patientName);
                    DialogManager.showConfimCancelDlg(item.getContext(), title, new DlgClick() {
                        @Override
                        public boolean click() {
                            //确定
                            CustomNotification command = new CustomNotification();

                            command.setSessionId(mPatient.patientIM);
                            command.setSessionType(SessionTypeEnum.P2P);

                            LogUtils.i("send msg 3:" + mPatient.id);
                            command.setContent("3:" + mPatient.id);
                          //  command.setContent(new Gson().toJson(bean));

                            // 设置该消息需要保证送达
                            command.setSendToOnlineUserOnly(false);
                            NIMClient.getService(MsgService.class).sendCustomNotification(command);
                            Toast.makeText(DemoCache.getContext(), R.string.end_request_post_ok, Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    }, new DlgClick() {
                        @Override
                        public boolean click() {
                            //取消
                            return false;
                        }
                    });
                    break;
            }
        }
    };

    private static List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        moreMenuItems.add(new PopupMenuItem(context, ACTION_PATIENT_DETAIL, sessionId,
                sessionTypeEnum, DemoCache.getContext().getString(R.string.patient_detail)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_DIAGNOSIS_ADVICE, sessionId,
                sessionTypeEnum, DemoCache.getContext().getString(R.string.diagnose_advice)));

        if (mType != ConstantValues.P2P_TEXT_COMPLETE) {
            moreMenuItems.add(new PopupMenuItem(context, ACTION_END, sessionId,
                    sessionTypeEnum, DemoCache.getContext().getString(R.string.post_end)));
        }
        moreMenuItems.add(new PopupMenuItem(context, ACTION_HISTORY, sessionId,
                sessionTypeEnum, DemoCache.getContext().getString(R.string.message_history)));
        return moreMenuItems;
    }
}
