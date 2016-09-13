package com.sg.eyedoctor.consult.advice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.BlueTextView;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScroolGridView;
import com.sg.eyedoctor.consult.textConsult.adapter.PictureGridAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultDetail;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 诊断建议   显示
 */
@ContentView(R.layout.activity_diagnostic_show)
public class DiagnosticShowActivity extends BaseActivity {

    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.img_tv)
    private BlueTextView mImgTv;
    @ViewInject(R.id.illness_tv)
    private TextView mIllnessTv;
    @ViewInject(R.id.drug_tv)
    private TextView mDrugTv;
    @ViewInject(R.id.check_tv)
    private TextView mCheckTv;
    @ViewInject(R.id.picture_gv)
    private NoScroolGridView mPictureGv;
    @ViewInject(R.id.empty_img_tv)
    private TextView mEmptyImgTv;
//    @ViewInject(R.id.drug_lv)
//    private NoScrollListView mDrugLv;
//    @ViewInject(R.id.check_lv)
//    private NoScrollListView mCheckLv;
    @ViewInject(R.id.diagnose_tv)
    private TextView mDiagnoseEt;
    @ViewInject(R.id.diagnose_result_tv)
    private TextView mResultTv;
//    @ViewInject(R.id.check_empty_tv)
//    private TextView mCheckEmptyTv;
//    @ViewInject(R.id.drug_empty_tv)
//    private TextView mDrugEmptyTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private Patient mPatient;
    private ConsultDetail mConsultDetail;

//    private ArrayAdapter<String> mCheckAdapter;
//    private ArrayList<String> mChecks = new ArrayList<>();
//
//    private SimpleAdapter mDrugAdapter;
//    private ArrayList<HashMap<String, Object>> mDrugs = new ArrayList<HashMap<String, Object>>();
//    private String[] mKeys = {"name", "unit"};//Map的key集合数组
//    private int[] mIds = {R.id.name_tv, R.id.unit};//对应布局文件的id

    private PictureGridAdapter mPictureGridAdapter;
    private ArrayList<Picture> mPictures = new ArrayList<>();
    private String mOrderId;

    private boolean mCounsultEnd = false;//接收到 结束通知 不能再编辑
    private boolean isTextConsult;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<ConsultDetail>>() {
                }.getType();
                BaseResp<ConsultDetail> res = new Gson().fromJson(result, objectType);
                mConsultDetail = res.value;
                parseDataToArray();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    private int mType; //判断是否可以编辑   当诊断建议填写完毕,则不能修改

    @Override
    protected void initView() {
        mPatient = getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);
        mOrderId=getIntent().getStringExtra("orderId");
        mCounsultEnd = getIntent().getBooleanExtra("end", false);
        mType = getIntent().getIntExtra("type", 1);
        isTextConsult=getIntent().getBooleanExtra("isTextConsult",false);

        if (!mCounsultEnd) {
            showActionbarEdit();
        }

        //显示为诊断建议或者病历详情
        boolean isRecord=getIntent().getBooleanExtra(ConstantValues.KEY_IS_RECORD,false);
        if(isRecord){
            mActionbar.setTitle("病历详情");
        }


//        mCheckAdapter = new ArrayAdapter<String>(mContext, R.layout.item_consult_check, R.id.check_name, mChecks);
//        mCheckLv.setAdapter(mCheckAdapter);
//
//        mDrugAdapter = new SimpleAdapter(this,
//                mDrugs, R.layout.item_consult_drug, mKeys, mIds);
//        mDrugLv.setAdapter(mDrugAdapter);

        mPictureGridAdapter = new PictureGridAdapter(this, mPictures, 0);
        mPictureGv.setAdapter(mPictureGridAdapter);

    }

    private void showActionbarEdit() {
        if (!mCounsultEnd) {
            mActionbar.setRightTv(R.string.edit, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, DiagnosticEditActivity.class);
                    intent.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                    intent.putExtra(ConstantValues.KEY_CONSULT_DETAIL, mConsultDetail);
                    intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, isTextConsult);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity();
                }
            });
        }

    }

    @Override
    protected void initListener() {
        mPictureGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, LookBigPicActivity.class);

                intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                for (Picture s : mPictures) {
                    picDataList.add(new PicLocalBean(s.picUrl,s.picUrl));
                }
                intent.putExtra(LookBigPicActivity.PICDATALIST, picDataList);
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
        if(mOrderId!=null){
            BaseManager.getDiagnosisAdvice(mOrderId, mCallback);
        }else{
            BaseManager.getDiagnosisAdvice(mPatient.id, mCallback);
        }
    }

    public void parseDataToArray() {
        mPictures.clear();

        if (mConsultDetail.picList != null) {
            for (ConsultDetail.PicList picList : mConsultDetail.picList) {
                mPictures.add(new Picture(picList.picUrl, picList.microPicUrl));
            }
            mPictureGridAdapter.setData(mPictures);
         //   UiUtils.setImgGridviewHeight(this, 4, mPictureGv, mPictures);
        }

        LogUtils.i("长度"+mPictures.size());
        if(mConsultDetail.picList==null||mConsultDetail.picList.size()==0){
            mEmptyImgTv.setVisibility(View.VISIBLE);
        }else{
            mEmptyImgTv.setVisibility(View.GONE);
        }

        setTextView(mDiagnoseEt, mConsultDetail.advice,"无记录");
        setTextView(mCheckTv, mConsultDetail.checkList,"无记录");
        setTextView(mDrugTv, mConsultDetail.drugList,"无记录");
        setTextView(mResultTv, mConsultDetail.result,"未诊断");

        //通过patient
        setTextView(mIllnessTv, mConsultDetail.description,"未填写");
        setTextView(mNameTv, mPatient.patientName);
        if(mPatient.sex!=null){
            setTextView(mSexTv, mPatient.sex.equals("1") ? "男" : "女");
        }
        if(mPatient.age!=null){
            setTextView(mAgeTv, mPatient.age + "岁");
        }

        String str=mConsultDetail.advice;
        if (str == null || str.equals("null") || str.trim().equals("")) {
            showActionbarEdit();
        }
    }


}
