package com.netease.nim.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.cache.FriendDataCache;
import com.netease.nim.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.session.SessionCustomization;
import com.netease.nim.session.constant.Extras;
import com.netease.nim.session.fragment.MessageFragment;
import com.netease.nim.uinfo.UserInfoHelper;
import com.netease.nim.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.activity.ReportNoteActivity;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.contact.activity.DoctorDetailActivity;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.helpUtils.freeConsult.bean.FreePatient;
import com.sg.eyedoctor.helpUtils.freeConsult.bean.HosDept;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */

@ContentView(R.layout.nim_message_activity)
public class P2PMessageActivity extends BaseMessageActivity {
    @ViewInject(R.id.actionbar)
    MyActionbar mActionbar;
    FriendList mFriendList;
    FreePatient mFreePatient;
    private boolean isResume = false;

    private NIMPopupMenu popupMenu;
    private List<PopupMenuItem> menuItemList;
    private ArrayList<HosDept> mHosDepts = new ArrayList<>();

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                // showToast("订单状态已修改!");
            } else {
                //  showToast(R.string.error_contact_account);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    //医生获取科室
    private NetCallback mHosCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<HosDept>>() {
                }.getType();
                BaseArrayResp<HosDept> res = new Gson().fromJson(result, objectType);

                mHosDepts = res.value;
                getMoreMenuItems(mContext, sessionId, SessionTypeEnum.P2P);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    //转诊
    private NetCallback mTransCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.transform_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    public static void start(Context context, String contactId, SessionCustomization customization) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, String contactId, SessionCustomization customization, Parcelable object) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(ConstantValues.KEY_DATA, object);
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        // 单聊特例话数据，包括个人信息，
        registerObservers(true);

        mActionbar.setTitle(customization.showName);

        if (customization.buttons != null && customization.buttons.size() != 0) {
            final SessionCustomization.OptionsButton button = customization.buttons.get(0);

            mActionbar.setRightBtnImg(customization.buttons.get(0).iconId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.onClick(mContext, v, sessionId);
                }
            });

        } else if (customization.type == ConstantValues.P2P_DOCTOR) {
            mFriendList = getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
            mActionbar.setRightTv(R.string.personal_info, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DoctorDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, mFriendList);
                    intent.putExtra(ConstantValues.KEY_TYPE, false);
                    startActivity(intent);
                }
            });
        } else if (customization.type == ConstantValues.P2P_FREE) {
            mFreePatient = getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
            if (!customization.showComplete) {
                showdialog();
                BaseManager.getHosDept(mHosCallback);
                //转诊
                mActionbar.setRightTv(R.string.transform_dept, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initPopuptWindow(mContext, v, sessionId, SessionTypeEnum.P2P);
                    }
                });
            }
        } else if (customization.type == ConstantValues.P2P_REPORT) {
            mActionbar.setRightTv(R.string.patient_report_note, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PDValidate report = getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
                    Intent intent = new Intent(mContext, ReportNoteActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, report);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(agreeObserver, register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            // setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            //  setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            //   setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            // setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {

                    }
                }
            };
        }

        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };
    Observer<CustomNotification> agreeObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            String str = message.getContent();


        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }
        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment(mCallback);
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

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
            showdialog();
            BaseManager.questionTransfer(item.getStrTag(), mFreePatient.questionId, mTransCallback);

        }
    };

    private List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        for (HosDept hosDept : mHosDepts) {
            moreMenuItems.add(new PopupMenuItem(context, hosDept.depID, sessionId,
                    sessionTypeEnum, hosDept.depName));
        }

        return moreMenuItems;
    }

    @Override
    public void patientPushAgree(String orderNo) {
        super.patientPushAgree(orderNo);
        if(customization.map!=null&&customization.map.get(ConstantValues.KEY_ORDER)!=null&&customization.map.get(ConstantValues.KEY_ORDER).equals(orderNo)){
            DialogManager.showConfimDlg(this, R.string.patient_agree, new DlgClick() {
                @Override
                public boolean click() {
                    AppManager.getAppManager().finishActivity();
                    return true;
                }
            });
        }
    }

    @Override
    public void patientPushRefuse(String orderNo) {
        super.patientPushRefuse(orderNo);

        if(customization.map!=null&&customization.map.get(ConstantValues.KEY_ORDER)!=null&&customization.map.get(ConstantValues.KEY_ORDER).equals(orderNo)){
            DialogManager.showConfimDlg(this, R.string.patient_deagree, new DlgClick() {
                @Override
                public boolean click() {
                    return false;
                }
            });
        }

    }
}
