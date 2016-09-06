package com.sg.eyedoctor.helpUtils.doctorAdvice.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuCreator;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.helpUtils.doctorAdvice.adapter.HospitalPatientDetailAdapter;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.HospitalPatient;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.HospitalPatientDetail;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 医嘱-病人详情
 */
@ContentView(R.layout.activity_hospital_patient_detail)
public class HospitalPatientDetailActivity extends BaseActivity implements HospitalPatientDetailAdapter.EditClick {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.bed_tv)
    private TextView mQualityTv;
    @ViewInject(R.id.patient_no_tv)
    private TextView mBedTv;
    @ViewInject(R.id.hospital_tv)
    private TextView mHospitalTv;
    @ViewInject(R.id.office_tv)
    private TextView mOweTv;
    @ViewInject(R.id.money_tv)
    private TextView mMoneyTv;
    @ViewInject(R.id.area_tv)
    private TextView mAreaTv;
    @ViewInject(R.id.doctor_tv)
    private TextView mDoctorTv;
    @ViewInject(R.id.enter_hospital_tv)
    private TextView mEnterHospitalTv;
    @ViewInject(R.id.health_care_tv)
    private TextView mHealthCareTv;
    @ViewInject(R.id.diagnostic_tv)
    private TextView mDiagnosticTv;
    @ViewInject(R.id.advice_lv)
    private ListView mAdviceLv;
    @ViewInject(R.id.local_lv)
    private SwipeMenuListView mLocalLv;

    private int mPage = 1;
    private int mRows = 10;

    //  private String zyh;
    private HospitalPatient mPatientDetail;

    private ArrayList<HospitalPatientDetail> mHospitalPatients = new ArrayList<>();
    private ArrayList<HospitalPatientDetail> mLocalPatients = new ArrayList<>();

    private HospitalPatientDetailAdapter mAdapter;
    private HospitalPatientDetailAdapter mLoaclAdapter;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseRowsResp<HospitalPatientDetail>>() {
                }.getType();
                BaseRowsResp<HospitalPatientDetail> res = new Gson().fromJson(result, objectType);

                mHospitalPatients=res.value.rows;
                mAdapter.setData(mHospitalPatients);

                addData();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };


    @Override
    protected void initView() {

        mPatientDetail = (HospitalPatient) getIntent().getSerializableExtra(ConstantValues.KEY_DATA);
        if (mPatientDetail != null) {
            setTextView(mNameTv, mPatientDetail.patientName);
            setTextView(mSexTv, mPatientDetail.patientSex);
            setTextView(mAgeTv, CommonUtils.dateToAge(mPatientDetail.patientBirt) + "岁");
            setTextView(mMoneyTv, mPatientDetail.moneyLeft + "");
            setTextView(mAreaTv, mPatientDetail.ward + "");
            setTextView(mHospitalTv, mPatientDetail.hisHospitalized);
            setTextView(mOweTv, mPatientDetail.moneyLeft < 0 ? "是" : "否");
            setTextView(mDoctorTv, mPatientDetail.residents);
            setTextView(mBedTv, mPatientDetail.bedNumber);
            setTextView(mQualityTv, mPatientDetail.chargeName + "");
            setTextView(mEnterHospitalTv, mPatientDetail.admissionDate + "");
            setTextView(mHealthCareTv, null);

        }

        mActionbar.setRightTv(R.string.new_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddDoctorAdviceActivity.class);
                intent.putExtra("his", mPatientDetail.hisHospitalized);
                intent.putExtra("canEdit", 0);

                startActivity(intent);
            }
        });
        mAdapter = new HospitalPatientDetailAdapter(mContext, mHospitalPatients, 0);
        mAdviceLv.setAdapter(mAdapter);

        mLoaclAdapter = new HospitalPatientDetailAdapter(mContext, mLocalPatients, 0);
        mLocalLv.setAdapter(mLoaclAdapter);
      //  createMenu();
        mLoaclAdapter.setEditClick(this);

    }

    @Override
    protected void initListener() {
        mAdviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddDoctorAdviceActivity.class);
                intent.putExtra("data", mHospitalPatients.get(position));
                intent.putExtra("canEdit", 2);
                startActivity(intent);
            }
        });

        mLocalLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddDoctorAdviceActivity.class);
                intent.putExtra("data", mLocalPatients.get(position));
                intent.putExtra("his", mLocalPatients.get(position).hisHospitalized);
                intent.putExtra("canEdit", 1);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        showdialog();
        BaseManager.getAdvice(mPage + "", mRows + "", mPatientDetail.hisHospitalized + "", mCallback);
    }

    private void addData() {
        mLocalPatients.clear();
        try {
            List<HospitalPatientDetail> mTemp = x.db().selector(HospitalPatientDetail.class)
                    .where("hisHospitalized", "=", mPatientDetail.hisHospitalized)
                    .orderBy("id", true).findAll();
            if (mTemp != null) {
                mLocalPatients.addAll(mTemp);
                LogUtils.i(mLocalPatients.size()+"  ------");
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        mLoaclAdapter.setData(mLocalPatients);

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = mLocalLv.getLayoutParams();
        // 设置高度
        params.height =mLocalPatients.size()*CommonUtils.dp2px(mContext,85) ;

        mLocalLv.setLayoutParams(params);


    }


    @Override
    public void editClick(final int position) {

        DialogManager.showConfimCancelDlg(this, R.string.delete_confirm, new DlgClick() {
            @Override
            public boolean click() {
                HospitalPatientDetail detail = mLocalPatients.get(position);
                LogUtils.i("delete: " + detail.Id);
                try {
                    x.db().deleteById(HospitalPatientDetail.class, detail.Id);
                    showToast("删除成功");
                } catch (DbException e) {
                    showToast("删除异常,请稍后重试");
                    e.printStackTrace();
                }

                addData();
                return false;
            }
        }, new DlgClick() {
            @Override
            public boolean click() {
                return false;

            }
        });

    }

    private void createMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());

                openItem.setBackground(new ColorDrawable(getcolor(R.color.actionsheet_red)));
                openItem.setWidth(CommonUtils.dp2px(mContext,80));
                openItem.setTitle("删除");
                openItem.setTitleSize(16);
                openItem.setTitleColor(getcolor(R.color.white));

                menu.addMenuItem(openItem);

            }
        };

        mLocalLv.setMenuCreator(creator);
    }
}
