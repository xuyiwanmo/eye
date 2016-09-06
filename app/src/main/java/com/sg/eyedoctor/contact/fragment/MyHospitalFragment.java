package com.sg.eyedoctor.contact.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.contact.adapter.HosDoctorExpandAdapter;
import com.sg.eyedoctor.contact.bean.DepartDoctor;
import com.sg.eyedoctor.contact.bean.Department;
import com.sg.eyedoctor.settings.personalInfo.activity.DoctorApproveActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.fragment_my_hospital)
public class MyHospitalFragment extends BaseFragment implements HosDoctorExpandAdapter.GroupClick{
    @ViewInject(R.id.hospital_elv)
    private ExpandableListView mHosElv;
    @ViewInject(R.id.not_see_ll)
    private LinearLayout mSeeLl;
    private int mCurrQueryGroupId;
    private HosDoctorExpandAdapter mAdapter;
    private ArrayList<Department> mDepartments = new ArrayList<>();
    private ArrayList<DepartDoctor> mTempDoctors = new ArrayList<>();
    private ArrayList<ArrayList<DepartDoctor>> mDoctors = new ArrayList<>();

    private NetCallback mDeptCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Department>>(){}.getType();
                BaseArrayResp<Department> res=new Gson().fromJson(result, objectType);

                mDepartments=res.value;
                initGroup();
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    private NetCallback mDoctorCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<DepartDoctor>>() {
                }.getType();
                BaseArrayResp<DepartDoctor> res = new Gson().fromJson(result, objectType);

                mTempDoctors = res.value;
                mDoctors.set(mCurrQueryGroupId, mTempDoctors);
                mAdapter.setData(mDepartments, mDoctors);
                if (mTempDoctors.size() == 0) {
                    showToast("未查询到医生");
                }
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };


    @Override
    protected void initView() {
        mAdapter=new HosDoctorExpandAdapter(getActivity(),mDepartments,mDoctors);
        mAdapter.setGroupClick(this);
        mHosElv.setGroupIndicator(null);
        mHosElv.setAdapter(mAdapter);
        UiUtils.setEmptyText(getActivity(), mHosElv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if(mDoctor.state==2){//认证
            showLoginDlg();
            BaseManager.getHosAddressBookDept(mDeptCallback);
            mHosElv.setVisibility(View.VISIBLE);
            mSeeLl.setVisibility(View.GONE);
        }else{//未认证
            mHosElv.setVisibility(View.GONE);
            mSeeLl.setVisibility(View.VISIBLE);
        }

    }

    private void initGroup() {
        ArrayList<DepartDoctor> item1 = new ArrayList<>();

        for (Department group : mDepartments) {
            mDoctors.add(item1);
        }
        mAdapter.setData(mDepartments, mDoctors);
        for (int i = 0; i < mDepartments.size(); i++) {
            mHosElv.collapseGroup(i);//全部收缩
        }

    }

    @Override
    public void groupLongClick(int position) {

    }

    @Override
    public void groupClick(int groupPosition, boolean isExpanded) {

        if (isExpanded) {//已经展开
            mHosElv.collapseGroup(groupPosition);
        } else {
            mHosElv.expandGroup(groupPosition);
        }

        if (mHosElv.isGroupExpanded(groupPosition)) {
            String groupId = mDepartments.get(groupPosition).departId;
            showLoginDlg();
            mCurrQueryGroupId = groupPosition;
            BaseManager.getHosAddressBook(groupId, mDoctorCallback);
        }
    }

    @Event(R.id.certification_tv)
    private void certification(View view) {
        Intent intent=new Intent(getActivity(), DoctorApproveActivity.class);
        startActivity(intent);
    }
}
