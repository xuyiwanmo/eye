package com.sg.eyedoctor.settings.personalInfo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.IdCardUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.settings.personalInfo.adapter.DeptAdapter;
import com.sg.eyedoctor.settings.personalInfo.adapter.HospitalAdapter;
import com.sg.eyedoctor.settings.personalInfo.adapter.LevelAdapter;
import com.sg.eyedoctor.settings.personalInfo.adapter.TitleAdapter;
import com.sg.eyedoctor.settings.personalInfo.bean.Dept;
import com.sg.eyedoctor.settings.personalInfo.bean.Hospital;
import com.sg.eyedoctor.settings.personalInfo.bean.Level;
import com.sg.eyedoctor.settings.personalInfo.bean.Title;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 医生认证编辑
 */
@ContentView(R.layout.activity_doctor_approve_edit)
public class DoctorApproveEditActivity extends BaseActivity {

    @ViewInject(R.id.lv_approve)
    private ListView mApproveLv;
    @ViewInject(R.id.et_my_update_title)
    private EditText mTitleEt;
    @ViewInject(R.id.img_my_update_title_delete)
    private ImageView mTitleDeleteImg;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.ll_my_update_title)
    private LinearLayout mTitleLl;

    private HospitalAdapter mHospitalAdapter;
    private TitleAdapter mTitleAdapter;
    private LevelAdapter mLevelAdapter;
    private DeptAdapter mDeptAdapter;

    private ArrayList<Hospital> mHospitals;
    private ArrayList<Level> mLevels;
    private ArrayList<Title> mTitles;
    private ArrayList<Dept> mDepts;

    private int hosId;
    /**
     *0执业  1医院  2.科室   3.职称   4.级别 5身份证
     */
    private int mSort = ConstantValues.APPROVE_OPERATION;
    private String mHint="";

    @Override
    protected void initView() {

        mHospitals = new ArrayList<>();
        mDepts = new ArrayList<>();
        mTitles = new ArrayList<>();
        mLevels = new ArrayList<>();

        mSort = getIntent().getIntExtra(ConstantValues.KEY_SORT, 0);
        mHint = getIntent().getStringExtra(ConstantValues.KEY_HINT);
        switch (mSort) {
            case ConstantValues.APPROVE_OPERATION:
                mActionbar.setTitle(R.string.operation_num);
                mTitleLl.setVisibility(View.VISIBLE);
                mActionbar.setRightTvVisible(View.VISIBLE);
                KeyBoardUtils.showKeyboard(mTitleEt);
                break;
            case ConstantValues.APPROVE_IDCARD:
                mActionbar.setTitle(R.string.idcard);
                mTitleLl.setVisibility(View.VISIBLE);
                mActionbar.setRightTvVisible(View.VISIBLE);
                KeyBoardUtils.showKeyboard(mTitleEt);
                break;
            case ConstantValues.APPROVE_HOSPITAL:
                mActionbar.setTitle(R.string.hospital);
                mHospitals = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
                mHospitalAdapter = new HospitalAdapter(mContext,mHospitals,0);
                mApproveLv.setAdapter(mHospitalAdapter);
                mApproveLv.setVisibility(View.VISIBLE);

                break;
            case ConstantValues.APPROVE_DEPT:
                mActionbar.setTitle(R.string.administrative_office);
                mDepts = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
                mDeptAdapter = new DeptAdapter(mContext,mDepts,0);
                mApproveLv.setAdapter(mDeptAdapter);
                mApproveLv.setVisibility(View.VISIBLE);
                break;
            case ConstantValues.APPROVE_TITLE:
                mActionbar.setTitle(R.string.title);
                mTitles = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
                mTitleAdapter = new TitleAdapter(mContext,mTitles,0);
                mApproveLv.setAdapter(mTitleAdapter);
                mApproveLv.setVisibility(View.VISIBLE);

                break;
            case ConstantValues.APPROVE_RANK:
                mActionbar.setTitle(R.string.rank);
                mLevels = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
                mLevelAdapter = new LevelAdapter(mContext,mLevels,0);
                mApproveLv.setAdapter(mLevelAdapter);
                mApproveLv.setVisibility(View.VISIBLE);
                break;
        }
        initChoose();

    }

    private void initChoose() {
        switch (mSort){
            case ConstantValues.APPROVE_OPERATION:
            case ConstantValues.APPROVE_IDCARD:
                mTitleEt.setText(mHint);

                break;
        }
        switch (mSort) {
            case ConstantValues.APPROVE_HOSPITAL:
                for (int i = 0; i < mHospitals.size(); i++) {
                    Hospital hospital=mHospitals.get(i);
                    if(hospital.hosName.equals(mHint)){
                       hospital.isChoose=true;
                    }
                }

                break;
            case ConstantValues.APPROVE_DEPT:
                for (int i = 0; i < mDepts.size(); i++) {
                    Dept hospital=mDepts.get(i);
                    if(hospital.deptName.equals(mHint)){
                        hospital.isChoose=true;
                    }
                }

                break;
            case ConstantValues.APPROVE_TITLE:
                for (int i = 0; i < mTitles.size(); i++) {
                    Title hospital=mTitles.get(i);
                    if(hospital.name.equals(mHint)){
                        hospital.isChoose=true;
                    }
                }
                break;
            case ConstantValues.APPROVE_RANK:
                for (int i = 0; i < mLevels.size(); i++) {
                    Level hospital=mLevels.get(i);
                    if(hospital.name.equals(mHint)){
                        hospital.isChoose=true;
                    }
                }
                break;
        }

    }

    @Override
    protected void initListener() {
        mApproveLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < parent.getChildCount(); i++) {
                    ImageView img = (ImageView) parent.getChildAt(i).findViewById(R.id.img_approve_check);
                    img.setSelected(false);
                }
                ImageView img = (ImageView) view.findViewById(R.id.img_approve_check);
                img.setSelected(true);

                switch (mSort) {
                    case ConstantValues.APPROVE_OPERATION:
                        break;
                    case ConstantValues.APPROVE_HOSPITAL:
                        mDoctor.licenseHospital = mHospitals.get(position).hosName;
                        try {
                            x.db().saveOrUpdate(mDoctor);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        hosId = position;
                        Intent intent = new Intent(DoctorApproveEditActivity.this, DoctorApproveActivity.class);
                        intent.putExtra(ConstantValues.KEY_HOS_ID, hosId);
                        startActivity(intent);
                        AppManager.getAppManager().finishActivity();
                        break;
                    case ConstantValues.APPROVE_DEPT:
                        mDoctor.licenseDept = mDepts.get(position).deptName;
                        try {
                            x.db().saveOrUpdate(mDoctor);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        AppManager.getAppManager().finishActivity();
                        break;
                    case ConstantValues.APPROVE_TITLE:
                        mDoctor.licenseTitle = mTitles.get(position).name;
                        try {
                            x.db().saveOrUpdate(mDoctor);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        AppManager.getAppManager().finishActivity();
                        break;
                    case ConstantValues.APPROVE_RANK:
                        mDoctor.licenseLevel = mLevels.get(position).name;
                        try {
                            x.db().saveOrUpdate(mDoctor);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        AppManager.getAppManager().finishActivity();
                        break;
                }
            }
        });
    }

    @Override
    protected void initActionbar() {
        if(mSort==ConstantValues.APPROVE_OPERATION||mSort==ConstantValues.APPROVE_IDCARD){
            mActionbar.setRightTv(R.string.complete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save();
                }
            });
        }else{
            mActionbar.setRightTvVisible(View.INVISIBLE);
        }
    }

    @Event(R.id.img_my_update_title_delete)
    private void deleteTitle(View view) {
        mTitleEt.setText("");
    }

    private void save() {
        if (mSort == ConstantValues.APPROVE_OPERATION) {
            mDoctor.licenseCard = mTitleEt.getText().toString();
        } else if (mSort == ConstantValues.APPROVE_IDCARD) {
            boolean isIdcard = IdCardUtils.isIdcard(getText(mTitleEt));
            if (isIdcard) {//错误信息为""
                mDoctor.idCard = mTitleEt.getText().toString();
            } else {
                showToast(R.string.edit_error);
                return;
            }
        }
        try {
            x.db().saveOrUpdate(mDoctor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        AppManager.getAppManager().finishActivity();
    }


}
