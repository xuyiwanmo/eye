package com.netease.nim.session.action;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.session.SessionCustomization;
import com.netease.nim.session.module.Container;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.Serializable;

//import com.netease.nim.uikit.session.SessionCustomization;
//import com.netease.nim.uikit.session.module.Container;

/**
 * Action基类。<br>
 * 注意：在子类中调用startActivityForResult时，requestCode必须用makeRequestCode封装一遍，否则不能再onActivityResult中收到结果。
 * requestCode仅能使用最低8位。
 */
public abstract class BaseAction implements Serializable {
    public SessionCustomization customization;
    private int iconResId;

    private int titleId;

    private transient int index;
    private transient Container container;

    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected BaseAction(int iconResId, int titleId) {
        this.iconResId = iconResId;
        this.titleId = titleId;
    }
    protected BaseAction(int iconResId, int titleId,SessionCustomization customization) {
        this.iconResId = iconResId;
        this.titleId = titleId;
        this.customization = customization;
    }



    public void setCustomization() {
        this.customization = customization;
    }

    public Activity getActivity() {
        return container.activity;
    }

    public String getAccount() {
        return container.account;
    }

    public SessionTypeEnum getSessionType() {
        return container.sessionType;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getTitleId() {
        return titleId;
    }

    public Container getContainer() {
        return container;
    }

    public abstract void onClick();

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // default: empty
    }

    protected void sendMessage(IMMessage message) {

        if (customization != null&&customization.map!=null) {
            message.setRemoteExtension(customization.map);
        }
        container.proxy.sendMessage(message);
    }

    protected int makeRequestCode(int requestCode) {
        if ((requestCode & 0xffffff00) != 0) {
            throw new IllegalArgumentException("Can only use lower 8 bits for requestCode");
        }
        return ((index + 1) << 8) + (requestCode & 0xff);
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
