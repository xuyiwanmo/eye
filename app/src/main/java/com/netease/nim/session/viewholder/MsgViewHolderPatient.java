package com.netease.nim.session.viewholder;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.session.extension.PatientAttachment;
//import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.sg.eyedoctor.R;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderPatient extends MsgViewHolderBase {

    private TextView mIllTv;
    private TextView mNameTv;
    private TextView mSexTv;
    private TextView mAgeTv;

    private PatientAttachment msgAttachment;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_patient;
    }

    @Override
    protected void inflateContentView() {

        if (showPatientTip) {
            noticeTextView.setVisibility(View.VISIBLE);
            noticeTextView.setText(Html.fromHtml("<font color='#ffffff'>点击患者头像可查看本次咨询详情,点击</font><font color='#3bafd9'>右上角图标</font><font color='#ffffff'>可查看更多</font>"));
        } else {
            noticeTextView.setVisibility(View.GONE);
        }

        mNameTv = (TextView) view.findViewById(R.id.name_tv);
        mSexTv = (TextView) view.findViewById(R.id.sex);
        mIllTv = (TextView) view.findViewById(R.id.ill);
        mAgeTv = (TextView) view.findViewById(R.id.age);

    }

    @Override
    protected void bindContentView() {
        msgAttachment = (PatientAttachment) message.getAttachment();

        mIllTv.setText(msgAttachment.ill);
        mAgeTv.setText(msgAttachment.age + "岁");
        mNameTv.setText(msgAttachment.name);
        mSexTv.setText(msgAttachment.sex);

    }


    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }
}
