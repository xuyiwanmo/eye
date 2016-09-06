package com.sg.eyedoctor.helpUtils.massNotice.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.helpUtils.massNotice.bean.DoctorPatientContact;
import com.sg.eyedoctor.helpUtils.massNotice.bean.MassReceiver;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 群发通知
 */
@ContentView(R.layout.activity_mass_notice)
public class MassNoticeActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.mass_title_et)
    private EditText mTitleEt;
    @ViewInject(R.id.content_et)
    private EditText mContentEt;
    @ViewInject(R.id.add_img)
    private ImageView mAddImg;
    @ViewInject(R.id.send_people_tv)
    private TextView mPeopleTv;
    @ViewInject(R.id.send_tv)
    private TextView mSendTv;

    private ArrayList<DoctorPatientContact> mTempContacts = new ArrayList<>();
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                showToast(R.string.publish_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

    }

    @Event(R.id.add_img)
    private void startAdd(View view) {
        Intent intent = new Intent(mContext, MassChooseContactActivity.class);
        intent.putParcelableArrayListExtra("data", mTempContacts);
        startActivityForResult(intent, 1);
    }

    @Event(R.id.send_tv)
    private void send(View view) {
        send();
    }

    @Override
    protected void initListener() {


    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.mass_notice);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
        mActionbar.setRightTv(R.string.already_send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlreadySendNoticeActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            mTempContacts = data.getParcelableArrayListExtra("data");

            String result = "";
            for (int i = 0; i < mTempContacts.size(); i++) {
                result = result + mTempContacts.get(i).name + "，";
            }
            if (!TextUtils.isEmpty(result)) {
                result = result.substring(0, result.length() - 1);
            }

            mPeopleTv.setText(result);
        }
    }

    private void send() {
        String title = getText(mTitleEt);
        String content = getText(mContentEt);
        if (title == null || title.equals("")) {
            showToast("主题未填写!");
            return;
        }
        if (content == null || content.equals("")) {
            showToast("通知内容未填写!");
            return;
        }
        if (mTempContacts.size() == 0) {
            showToast("群发成员不能为空!");
            return;
        }
        ArrayList<MassReceiver> receiverList = new ArrayList<>();

        for (DoctorPatientContact tempContact : mTempContacts) {
            receiverList.add(new MassReceiver(tempContact.id,tempContact.name, tempContact.type));
        }
        showdialog();
        BaseManager.massNotice(title, content, receiverList, mCallback);
    }


}
