package com.sg.eyedoctor.helpUtils.massNotice.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.ChooseContactAdapter;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.helpUtils.massNotice.adapter.ChoosePatientAdapter;
import com.sg.eyedoctor.helpUtils.massNotice.adapter.MassGalleyAdapter;
import com.sg.eyedoctor.helpUtils.massNotice.bean.DoctorPatientContact;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_mass_choose_contact)
public class MassChooseContactActivity extends BaseActivity implements ChooseContactAdapter.DoctorCheckedCallback, ChoosePatientAdapter.PatientCheckedCallback, SearchLayout.SearchCallback {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchLayout;
    @ViewInject(R.id.head_rv)
    private RecyclerView mHeadRv;
    @ViewInject(R.id.choose_all_ll)
    private LinearLayout mAllLl;
    @ViewInject(R.id.all_check_img)
    private ImageView mCheckAllImg;
    @ViewInject(R.id.all_choose_tv)
    private TextView mCheckAllTv;
    @ViewInject(R.id.patient_lv)
    private NoScrollListView mPatientLv;
    @ViewInject(R.id.doctor_lv)
    private NoScrollListView mDoctorLv;

    private ChooseContactAdapter mDoctorAdapter;
    private ChoosePatientAdapter mPatientAdapter;
    private MassGalleyAdapter mHeadAdapter;

    private ArrayList<DoctorPatientContact> mTempContacts = new ArrayList<>();
    private boolean mIsChooseAll = false;

    //患者报到   数据
    private ArrayList<PDValidate> mPatients = new ArrayList<>();
    private ArrayList<PDValidate> mSearchPatients = new ArrayList<>();
    private NetCallback mNetCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {

               //查询医生好友
                BaseManager.circleFriendListFind("",mCallback);
                Type objectType = new TypeToken<BaseArrayResp<PDValidate>>() {
                }.getType();
                BaseArrayResp<PDValidate> res = new Gson().fromJson(result, objectType);

                mPatients = res.value;
                mPatientAdapter.setData(mPatients);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    //医生好友
    private ArrayList<FriendList> mDoctors = new ArrayList<>();
    private ArrayList<FriendList> mSearchDoctors = new ArrayList<>();
    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<FriendList>>(){}.getType();
                BaseArrayResp<FriendList> res=new Gson().fromJson(result, objectType);
                mDoctors = res.value;
                setFriendsChecked();
                mDoctorAdapter.setData(mDoctors);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mTempContacts = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeadRv.setLayoutManager(linearLayoutManager);

        mHeadAdapter = new MassGalleyAdapter(mContext, mTempContacts);
        mHeadRv.setAdapter(mHeadAdapter);
        mDoctorAdapter = new ChooseContactAdapter(mContext, mDoctors, 0);
        mDoctorLv.setAdapter(mDoctorAdapter);
        mDoctorAdapter.setDoctorCheckedCallback(this);

        mPatientAdapter = new ChoosePatientAdapter(mContext, mPatients, 0);
        mPatientLv.setAdapter(mPatientAdapter);
        mPatientAdapter.setCheckedCallback(this);
        mSearchLayout.setCallback(this);

        UiUtils.setEmptyText(mContext, mPatientLv, getString(R.string.empty));

        showdialog();
        BaseManager.getPDFriendList("",mNetCallback);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.choose_contact);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
        mActionbar.setRightTv(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("data", mTempContacts);
                setResult(RESULT_OK, intent);
                AppManager.getAppManager().finishActivity();

            }
        });
    }

    @Event(R.id.choose_all_ll)
    private void choose(View view) {
        String text = mCheckAllTv.getText().toString().trim();

        if (text.equals(getString(R.string.all_choose_false))) {
            mCheckAllTv.setText(R.string.all_choose);
            mTempContacts.clear();
            setAllFriendschoose(false);
            mCheckAllImg.setSelected(false);
        } else if (text.equals(getString(R.string.all_choose))) {
            mCheckAllTv.setText(R.string.all_choose_false);
            setAllFriendschoose(true);
            mTempContacts.clear();
            for (FriendList doctor : mDoctors) {
                mTempContacts.add(new DoctorPatientContact(doctor.friendId, doctor.picFileName, doctor.userName, 1));
            }
            for (PDValidate doctor : mPatients) {
                mTempContacts.add(new DoctorPatientContact(doctor.id, "", doctor.name, 2));
            }

            mCheckAllImg.setSelected(true);
        }
        mPatientAdapter.notifyDataSetChanged();
        mDoctorAdapter.notifyDataSetChanged();
        mHeadAdapter.notifyDataSetChanged();

    }

    //我的医生好友 点击回调
    @Override
    public void setDoctorChecked(boolean isChecked, FriendList checkFriend, int position) {

        int size=mTempContacts.size();
        for (int i = 0; i < size; i++) {
            DoctorPatientContact friendList = mTempContacts.get(i);
            if (friendList.id.equals(checkFriend.friendId)) {  //存在
                if (!isChecked) {//不存在
                    mTempContacts.remove(i);
                    i--;
                    size--;
                }
            }
        }
        for (int i = 0; i < mDoctors.size(); i++) {
            FriendList friendList = mDoctors.get(i);
            if (friendList.friendId.equals(checkFriend.friendId)) {  //存在
                friendList.isChecked = isChecked;
            }
        }
        if (isChecked) {
            mTempContacts.add(new DoctorPatientContact(checkFriend.friendId, checkFriend.picFileName, checkFriend.userName, 1));
        }
        //    mContactAdapter.notifyDataSetChanged();
        mHeadAdapter.notifyDataSetChanged();

        judgeAllChecked();
    }

    //我的患者  点击回调
    @Override
    public void setPatientChecked(boolean isChecked, PDValidate checkFriend, int position) {
        int size=mTempContacts.size();
        for (int i = 0; i < size; i++) {
            DoctorPatientContact friendList = mTempContacts.get(i);
            if (friendList.id.equals(checkFriend.id)) {  //存在
                if (!isChecked) {//不存在
                    mTempContacts.remove(i);
                    i--;
                    size--;
                }
            }
        }
        for (int i = 0; i < mPatients.size(); i++) {
            PDValidate friendList = mPatients.get(i);
            if (friendList.id.equals(checkFriend.id)) {  //存在
                friendList.isChecked = isChecked;
            }
        }
        if (isChecked) {
            mTempContacts.add(new DoctorPatientContact(checkFriend.id, "", checkFriend.name, 2));
        }
        //mContactAdapter.notifyDataSetChanged();
        mHeadAdapter.notifyDataSetChanged();

        judgeAllChecked();
    }

    private void setFriendsChecked() {
        //清除无用的账户
        for (int i = 0; i < mDoctors.size(); i++) {
            FriendList friendList = mDoctors.get(i);
            if (friendList.userName == null || TextUtils.isEmpty(friendList.userName)) {
                mDoctors.remove(i);
            }
        }

        for (DoctorPatientContact friendList : mTempContacts) {
            for (FriendList allFriend : mDoctors) {

                if (allFriend.friendId.equals(friendList.id)) {
                    allFriend.isChecked = true;
                }
            }
            for (PDValidate allFriend : mPatients) {
                if (allFriend.id.equals(friendList.id)) {
                    allFriend.isChecked = true;
                }
            }
        }
    }

    private void judgeAllChecked() {
        int trueCount = 0;
        int falseCount = 0;
        int allcount = mPatients.size() + mDoctors.size();

        for (FriendList allFriend : mDoctors) {
            if (allFriend.isChecked) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        for (PDValidate patient : mPatients) {
            if (patient.isChecked) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        if (trueCount == allcount) {
            //全选
            mCheckAllImg.setSelected(true);
            mCheckAllTv.setText(R.string.all_choose_false);
            mIsChooseAll = !mIsChooseAll;

        } else if (falseCount == allcount - 1) {
            //反选
            mCheckAllImg.setSelected(false);
            mCheckAllTv.setText(R.string.all_choose);
            mIsChooseAll = !mIsChooseAll;
        } else {
            mCheckAllImg.setSelected(false);
            mCheckAllTv.setText(R.string.all_choose);
        }
    }

    public void setAllFriendschoose(boolean checked) {
        for (FriendList allFriend : mDoctors) {
            allFriend.isChecked = checked;
        }
        for (PDValidate allFriend : mPatients) {
            allFriend.isChecked = checked;
        }
    }

    @Override
    public void fillData(String filterStr) {
        if (filterStr == null) {
            return;
        }else{
            mAllLl.setVisibility(View.GONE);
        }
        ArrayList<PDValidate> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mAllLl.setVisibility(View.VISIBLE);
            filterDateList = mPatients;
        } else {
            filterDateList.clear();
            for (PDValidate sortModel : mPatients) {
                String cname = sortModel.name;
                if (cname == null) {
                    continue;
                }
                if (cname.contains(filterStr)) {
                    filterDateList.add(sortModel);
                }
            }
        }
        mPatientAdapter.updateListView(filterDateList);

        ArrayList<FriendList> patientfilterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            patientfilterDateList = mDoctors;
        } else {
            patientfilterDateList.clear();
            for (FriendList sortModel : mDoctors) {
                String cname = sortModel.userName;
                if (cname == null) {
                    continue;
                }
                if (cname.contains(filterStr)) {
                    patientfilterDateList.add(sortModel);
                }
            }
        }
        mDoctorAdapter.setData(patientfilterDateList);
    }

    @Override
    public void cancel() {
        mDoctorAdapter.setData(mDoctors);
        mPatientAdapter.setData(mPatients);
        mAllLl.setVisibility(View.VISIBLE);
    }
}
