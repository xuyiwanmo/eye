package com.sg.eyedoctor.chartFile.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.adapter.DiagnosisAdapter;
import com.sg.eyedoctor.chartFile.adapter.DrugAdapter;
import com.sg.eyedoctor.chartFile.bean.Diagnosis;
import com.sg.eyedoctor.chartFile.bean.Drug;
import com.sg.eyedoctor.chartFile.bean.MedicineDetail;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultationList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 病历夹->患者中心->病历详情
 */
@ContentView(R.layout.activity_medicine_detail)
public class MedicineDetailActivity extends BaseActivity {
    @ViewInject(R.id.rl_cese_detail_check)
    private RelativeLayout mCheckRl;
    @ViewInject(R.id.tv_medicine_shop_empty)
    private TextView mShopEmptyTv;
    @ViewInject(R.id.tv_medicine_store_empty)
    private TextView mStoreEmptyTv;
    @ViewInject(R.id.tv_case_detail_name)
    private TextView mNameTv;
    @ViewInject(R.id.tv_case_detail_diagnose)
    private TextView mDiagnoseName;
    @ViewInject(R.id.lv_medicine_shop)
    private NoScrollListView mMedicineShopLv;
    @ViewInject(R.id.lv_medicine_store)
    private NoScrollListView mMedicineStoreLv;
    @ViewInject(R.id.lv_case_detail_check)
    private NoScrollListView mCheckLv;

    private ConsultationList mRecord;
    private DrugAdapter mDrugstoreAdapter;
    private DrugAdapter mPharmacyAdapter;
    private DiagnosisAdapter mDiagnosisAdapter;
    private ArrayList<Drug> mDrugstores; //药店
    private ArrayList<Drug> mPharmacies; //药房
    private ArrayList<Diagnosis> mDiagnosises;  //检查
    private String mPatientName;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<MedicineDetail>>(){}.getType();
                BaseResp<MedicineDetail> res=new Gson().fromJson(result, objectType);
                MedicineDetail value = res.value;
                if (value.drugstoreList != null) {
                    mDrugstores = value.drugstoreList;
                    mDrugstoreAdapter.setData(mDrugstores);
                    mStoreEmptyTv.setVisibility(View.GONE);
                } else {
                    mStoreEmptyTv.setVisibility(View.VISIBLE);
                }
                if (value.pharmacyList != null) {
                    mPharmacies = value.pharmacyList;
                    mPharmacyAdapter.setData(mPharmacies);
                    mShopEmptyTv.setVisibility(View.GONE);
                } else {
                    mShopEmptyTv.setVisibility(View.VISIBLE);
                }
                if (value.diagnosisList != null&&value.diagnosisList.size()!=0) {
                    mDiagnosises = value.diagnosisList;
                } else {
                    mDiagnosises.add(new Diagnosis("无记录"));
                }
                mDiagnosisAdapter.setData(mDiagnosises);
            }else{
                showToast(R.string.get_data_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mRecord = getIntent().getParcelableExtra(ConstantValues.KEY_RECORD);
        mPatientName=getIntent().getStringExtra(ConstantValues.KEY_DATA);
        mDrugstores = new ArrayList<>();
        mPharmacies = new ArrayList<>();
        mDiagnosises = new ArrayList<>();
        mDrugstoreAdapter = new DrugAdapter(this, mDrugstores,0);
        mPharmacyAdapter = new DrugAdapter(this, mPharmacies,0);
        mDiagnosisAdapter = new DiagnosisAdapter(this, mDiagnosises,0);
        mCheckLv.setAdapter(mDiagnosisAdapter);
        mMedicineShopLv.setAdapter(mPharmacyAdapter);
        mMedicineStoreLv.setAdapter(mDrugstoreAdapter);
        setTextView(mNameTv, mPatientName);

        if(mRecord.description!=null){
            setTextView(mDiagnoseName,"诊断: "+ mRecord.description);
        }

        if (mRecord != null) {
            showdialog();
            BaseManager.queryDetail(mRecord.outpatientCard, mCallback);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }


}
