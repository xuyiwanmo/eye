package com.sg.eyedoctor.consult.videoConsult.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.ConsultSort;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;
import com.sg.eyedoctor.consult.videoConsult.adapter.VideoAddAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * 视频咨询:加号
 */
@ContentView(R.layout.fragment_consult_new)
public class VideoAddFragment extends BaseFragment implements VideoAddAdapter.AddCallback {

    @ViewInject(R.id.lv_couslt)
    private ListView mConsultNewList;

    private VideoAddAdapter mConsultAdapter;
    private ArrayList<Patient> mRecords;
    private String mXtrId;

    private NetCallback mDataCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);
                mRecords = res.value;
                Collections.sort(mRecords, new ConsultSort());
                mConsultAdapter.setData(mRecords);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                BaseManager.getServiceXtrRecordList(mXtrId,"", mDataCallback);
            }else {
                closeDialog();
                showToast(R.string.post_fail);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    public VideoAddFragment(String xtrId) {
        mXtrId = xtrId;
    }

    public VideoAddFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecords = new ArrayList<>();
        mConsultAdapter = new VideoAddAdapter(getActivity(),mRecords, 0,this);
        mConsultNewList.setAdapter(mConsultAdapter);
        UiUtils.setEmptyText(getActivity(), mConsultNewList, getString(R.string.empty));

    }

    @Override
    protected void initView() {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, mDoctor.userName);
        fields.put(UserInfoFieldEnum.AVATAR, CommonUtils.formatUrl(mDoctor.picFileName));
        NIMClient.getService(UserService.class).updateUserInfo(fields);
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser&&isVisible()) {
            showLoginDlg();
            BaseManager.getServiceXtrRecordList(mXtrId, "",mDataCallback);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint() ) {
            showLoginDlg();
            BaseManager.getServiceXtrRecordList(mXtrId,"", mDataCallback);
        }
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void agreeOrRefuse(int position, int type) {
        Patient record=mRecords.get(position);
        showLoginDlg();
        BaseManager.serviceXtrRecordModify(record.id,type,mCallback);
    }
}
