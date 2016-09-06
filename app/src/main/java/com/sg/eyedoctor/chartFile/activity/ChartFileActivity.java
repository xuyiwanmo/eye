package com.sg.eyedoctor.chartFile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.adapter.MedicalRecordExpandAdapter;
import com.sg.eyedoctor.chartFile.bean.Group;
import com.sg.eyedoctor.chartFile.bean.MedicalRecord;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_chart_file)
public class ChartFileActivity extends BaseActivity implements MedicalRecordExpandAdapter.GroupClick{



    @ViewInject(R.id.patient_smelv)
    private SwipeMenuExpandableListView mExpandableListView;

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private MedicalRecordExpandAdapter mAdapter;
    private ArrayList<Group> mGroups = new ArrayList<>();
    private ArrayList<ArrayList<PatientByGroup>> mPatients = new ArrayList<>();
    private ArrayList<PatientByGroup> mPatientParamses = new ArrayList<>();
    private ArrayList<String> mGroupNames;
    private int mCurrQueryGroupId;

    private int mAction=ConstantValues.CHART_QUERY_GROUP;

    private NetCallback mGroupCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Group>>() {
                }.getType();
                BaseArrayResp<Group> res = new Gson().fromJson(result, objectType);
                mGroups = res.value;

                initGroup();
                switch (mAction){
                    case ConstantValues.CHART_DELETE_GROUP:
                        showToast(R.string.delete_group_ok);
                        break;
                    case ConstantValues.CHART_RENAME_GROUP:
                        showToast(R.string.rename_group_ok);
                        break;
                }
                mAction=ConstantValues.CHART_QUERY_GROUP;
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    private NetCallback mItemCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PatientByGroup>>() {
                }.getType();
                BaseArrayResp<PatientByGroup> res = new Gson().fromJson(result, objectType);

                mPatientParamses = res.value;
                mPatients.set(mCurrQueryGroupId, mPatientParamses);
                mAdapter.setData(mGroups, mPatients);
                if (mPatientParamses.size() == 0) {
                    showToast(R.string.patient_empty);
                }
            } else {

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mAdapter = new MedicalRecordExpandAdapter(mContext, mGroups, mPatients);
        mAdapter.setGroupClick(this);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setGroupIndicator(null);

        mGroupNames = new ArrayList<>();
        initData();
    }

    @Override
    protected void initListener() {

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                PatientByGroup patient = mPatients.get(groupPosition).get(childPosition);
                Intent intent = new Intent(mContext, PatientCenterActivity.class);
                intent.putExtra(ConstantValues.KEY_PATIENT, patient);
                intent.putExtra(ConstantValues.KEY_TYPE, ConstantValues.PATIENT_CENTER_CHOOSE);
                startActivityForResult(intent, ConstantValues.REQUEST_IMPORT_CASE);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantValues.REQUEST_IMPORT_CASE){
            if (resultCode == RESULT_OK) {
                MedicalRecord mRecord=data.getParcelableExtra(ConstantValues.KEY_RECORD);
                Intent intent=new Intent();
                intent.putExtra(ConstantValues.KEY_RECORD,mRecord);
                setResult(RESULT_OK, intent);
                AppManager.getAppManager().finishActivity();
            }
        }
    }

    @Override
    protected void initActionbar() {
        
    }

    @Override
    public void groupLongClick(int position) {

    }

    @Override
    public void groupClick(int groupPosition, boolean isExpanded) {
        if (isExpanded) {//已经展开
            mExpandableListView.collapseGroup(groupPosition);
        } else {
            mExpandableListView.expandGroup(groupPosition);
        }

        if (mExpandableListView.isGroupExpanded(groupPosition)) {
            String groupId = mGroups.get(groupPosition).id;
            showdialog();
            mCurrQueryGroupId = groupPosition;
            BaseManager.getPatientByGroup(groupId, mItemCallback);
        }

    }

    private void initGroup() {
        ArrayList<PatientByGroup> item1 = new ArrayList<>();

        for (Group group : mGroups) {
            mPatients.add(item1);
        }
        mAdapter.setData(mGroups, mPatients);
        for (int i = 0; i < mGroups.size(); i++) {
            mExpandableListView.collapseGroup(i);//全部收缩
        }
        mGroupNames.clear();
        for (Group group : mGroups) {
            if (group.groupName != null) {
                mGroupNames.add(group.groupName);
            }
        }
    }

    public void initData() {
        showdialog();
        BaseManager.getGroupByDoc(mGroupCallback);
    }
}
