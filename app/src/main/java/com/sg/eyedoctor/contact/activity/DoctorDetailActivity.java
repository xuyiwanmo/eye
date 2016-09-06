package com.sg.eyedoctor.contact.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 个人信息
 */
@ContentView(R.layout.activity_doctor_detail)
public class DoctorDetailActivity extends BaseActivity {

    @ViewInject(R.id.head_img)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.hospital_tv)
    private TextView mHospitalTv;
    @ViewInject(R.id.illness_tv)
    private TextView mOfficeTv;
    @ViewInject(R.id.title_tv)
    private TextView mTitleTv;
    @ViewInject(R.id.good_at_tv)
    private TextView mGoodAtTv;
    @ViewInject(R.id.add_send_btn)
    private Button mAddSendBtn;
    @ViewInject(R.id.agree_btn)
    private Button mAgreeBtn;
    @ViewInject(R.id.refuse_btn)
    private Button mRefuseBtn;
    @ViewInject(R.id.send_msg_btn)
    private Button mSendMsgBtn;
    private FriendList mSearchContact;

    private boolean mIsFriend=false;
    private boolean mShow=true;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)){
               showToast(R.string.send_add_request_ok);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    //判断是否是好友
    private NetCallback mExitCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)){
                Type objectType = new TypeToken<BaseResp<String>>(){}.getType();
                BaseResp<String> res=new Gson().fromJson(result, objectType);
                mIsFriend=res.value.equals("yes")?true:false;
                initSendMsg(mIsFriend);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {
        mSearchContact = getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
        mShow = getIntent().getBooleanExtra(ConstantValues.KEY_TYPE, true);
        CommonUtils.loadImg(mSearchContact.picFileName,mHeadImg);
        mNameTv.setText(mSearchContact.userName);
        mOfficeTv.setText(mSearchContact.licenseDept);
        mGoodAtTv.setText(mSearchContact.speciality);
        mTitleTv.setText(mSearchContact.licenseTitle);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.add_send_btn)
    private void sendAddrequest(View view){
        showdialog();
        BaseManager.circleFriendAdd(mSearchContact.id, mCallback);
    }
    @Event(R.id.send_msg_btn)
    private void sendMsg(View view){
        if (!CommonUtils.isLogin()){
            showToast(R.string.operation_not_open);
            return;
        }
        mSearchContact.loginId=mSearchContact.loginid;
        P2PMessageActivity.start(mContext, "d" + mSearchContact.loginid, SessionHelper.getDoctorP2pCustomization(mSearchContact),mSearchContact);
        AppManager.getAppManager().finishActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mShow){
            showdialog();
            BaseManager.checkFriendIsExist(mSearchContact.id, mExitCallback);
        }else{
            mSendMsgBtn.setVisibility(View.GONE);
            mAddSendBtn.setVisibility(View.GONE);
        }

    }

    private void initSendMsg(boolean isFriend) {
        mSendMsgBtn.setVisibility(isFriend?View.VISIBLE:View.GONE);
        mAddSendBtn.setVisibility(isFriend?View.GONE:View.VISIBLE);
    }

}
