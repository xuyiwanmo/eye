package com.sg.eyedoctor.chartFile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.NewCaseDetail;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.textConsult.adapter.PictureGridAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultationList;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 病历夹-患者中心-病例详情
 */
@ContentView(R.layout.activity_new_case_show)
public class NewCaseShowActivity extends BaseActivity {
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.img_tv)
    private TextView mImgTv;
    @ViewInject(R.id.illness_tv)
    private TextView mIllnessTv;
    @ViewInject(R.id.picture_gv)
    private GridView mPictureGv;
//    @ViewInject(R.id.drug_lv)
//    private NoScrollListView mDrugLv;
//    @ViewInject(R.id.check_lv)
//    private NoScrollListView mCheckLv;
    @ViewInject(R.id.diagnose_result_tv)
    private TextView mResultTv;
    @ViewInject(R.id.check_tv)
    private TextView mCheckTv;
    @ViewInject(R.id.drug_tv)
    private TextView mDrugTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private ConsultationList mPatient;
    private NewCaseDetail mConsultDetail;

//    private ArrayAdapter<String> mCheckAdapter;
//    private ArrayList<String> mChecks = new ArrayList<>();
//
//    private SimpleAdapter mDrugAdapter;
//    private ArrayList<HashMap<String, Object>> mDrugs = new ArrayList<HashMap<String, Object>>();
//    private String[] mKeys = {"name", "unit"};//Map的key集合数组
//    private int[] mIds = {R.id.name_tv, R.id.unit};//对应布局文件的id

    private PictureGridAdapter mPictureGridAdapter;
    private ArrayList<Picture> mPictures = new ArrayList<>();

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<NewCaseDetail>>() {
                }.getType();
                BaseResp<NewCaseDetail> res = new Gson().fromJson(result, objectType);
                mConsultDetail = res.value;
                parseDataToArray();
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
        mPatient = getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);

//        mCheckAdapter = new ArrayAdapter<String>(mContext, R.layout.item_consult_check, R.id.check_name, mChecks);
//        mCheckLv.setAdapter(mCheckAdapter);
//
//        mDrugAdapter = new SimpleAdapter(this,
//                mDrugs, R.layout.item_consult_drug, mKeys, mIds);
//        mDrugLv.setAdapter(mDrugAdapter);

        mPictureGridAdapter = new PictureGridAdapter(this, mPictures, 0);
        mPictureGv.setAdapter(mPictureGridAdapter);

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
        BaseManager.getNewMedicalRecord(mPatient.newId, mCallback);
    }

    public void parseDataToArray() {
        setTextView(mNameTv, mConsultDetail.patientName);
        setTextView(mSexTv, mConsultDetail.sex.equals("1") ? "男" : "女");
        setTextView(mIllnessTv, mConsultDetail.description);
        setTextView(mAgeTv, mConsultDetail.age + "岁");
        mPictures.clear();
//        if (mConsultDetail.checkList != null) {
//            for (CheckBean checkList : mConsultDetail.checkList) {
//                mChecks.add(checkList.checkName);
//            }
//            mCheckAdapter.notifyDataSetChanged();
//        }
//        if (mConsultDetail.drugList != null) {
//            HashMap<String, Object> map = null;
//            for (DrugBean drugList : mConsultDetail.drugList) {
//                map = new HashMap<String, Object>();
//                map.put("name", drugList.drugName);
//                map.put("unit", drugList.drugUnit);
//                mDrugs.add(map);
//            }
//            mDrugAdapter.notifyDataSetChanged();
//        }

        if (mConsultDetail.picList != null) {
            for (PicBean picList : mConsultDetail.picList) {
                mPictures.add(new Picture(picList.picUrl, picList.microPicUrl));
            }
            mPictureGridAdapter.setData(mPictures);
            UiUtils.setImgGridviewHeight(this, 4, mPictureGv, mPictures);
        }
        if (mConsultDetail.picList == null || mConsultDetail.picList.size() == 0) {
            mImgTv.setVisibility(View.VISIBLE);
        } else {
            mImgTv.setVisibility(View.GONE);
        }
        setTextView(mResultTv, mConsultDetail.result, "未诊断");
        setTextView(mIllnessTv, mConsultDetail.description);
        setTextView(mDrugTv, mConsultDetail.drugList,"无记录");
        setTextView(mCheckTv, mConsultDetail.checkList,"无记录");

//        if (mDrugs.size() != 0) {
//            mDrugEmptyTv.setVisibility(View.GONE);
//        } else {
//            mDrugEmptyTv.setVisibility(View.VISIBLE);
//        }
//        if (mChecks.size() != 0) {
//            mCheckEmptyTv.setVisibility(View.GONE);
//        } else {
//            mCheckEmptyTv.setVisibility(View.VISIBLE);
//        }
    }
}
