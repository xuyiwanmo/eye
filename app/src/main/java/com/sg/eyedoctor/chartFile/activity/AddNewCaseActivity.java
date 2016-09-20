package com.sg.eyedoctor.chartFile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.adapter.AddImageGridAdapter;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.advice.adapter.DiagnosticDrugAdapter;
import com.sg.eyedoctor.consult.advice.bean.AdviceDrug;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 病历夹-
 * 新增病例
 */
@ContentView(R.layout.activity_add_new_case)
public class AddNewCaseActivity extends BaseActivity implements DiagnosticDrugAdapter.UpdateGrugCount {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.illness_et)
    private EditText mIllnessEt;
    @ViewInject(R.id.content_et)
    private EditText mContentEt;
    @ViewInject(R.id.drug_et)
    private EditText mDrugEt;
    @ViewInject(R.id.check_et)
    private EditText mCheckEt;
    @ViewInject(R.id.image_gv)
    private GridView mImageGv;
    private AdviceDrug mDrug;
    private PatientByGroup mPatient;
    private ArrayList<String> mSelectPath=new ArrayList<>();
    private AddImageGridAdapter mGridAdapter;
    private ArrayList<PicBean> mPicBeans = new ArrayList<>();

    //上传图片回调
    private NetCallback mPicCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PicBean>>() {
                }.getType();
                BaseArrayResp<PicBean> res = new Gson().fromJson(result, objectType);
                mPicBeans = res.value;
                BaseManager.newMedicalRecordAdd(mPatient.id, getText(mIllnessEt), getText(mContentEt), getText(mDrugEt), getText(mCheckEt), mPicBeans, mDataCallback);
            }else{
                showToast(R.string.get_data_error);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    //发布回调
    private NetCallback mDataCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.publish_ok);
                AppManager.getAppManager().finishActivity();
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
        mPatient = (PatientByGroup) getIntent().getSerializableExtra(ConstantValues.KEY_PATIENT);
        setTextView(mNameTv, mPatient.name);
        setTextView(mAgeTv, CommonUtils.dateToAge(mPatient.birth) + "岁");

        mGridAdapter = new AddImageGridAdapter(this, mSelectPath, 4);
        mImageGv.setAdapter(mGridAdapter);
    }

    @Override
    protected void initListener() {
        mImageGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mSelectPath.size()) {
                    chooseImg();
                } else {
                    //查看大图
                    Intent intent = new Intent(mContext, LookBigPicActivity.class);
                    ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                    for (String s : mSelectPath) {
                        picDataList.add(new PicLocalBean(s,s));
                    }
                    intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                    intent.putExtra(LookBigPicActivity.PICDATALIST, picDataList);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.complete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
    }

    private void chooseImg() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, ConstantValues.CHART_REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantValues.CHART_REQUEST_IMAGE && resultCode == RESULT_OK) {
            mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            mGridAdapter.setData(mSelectPath);
            UiUtils.setViewHeightBasedOnChildren(mImageGv, 16, 16);
        }
    }

    @Override
    public void increase(int postion) {
    }

    @Override
    public void decrease(final int postion) {
    }

    @Override
    public void edit(int postion, String count) {
    }

    private void post() {
        String illness=mIllnessEt.getText().toString();
        String describe=mContentEt.getText().toString();
        if (illness==null||illness.equals("")){
            showToast("疾病诊断不能为空!");
            return;
        }
        if (describe==null||describe.equals("")){
            showToast("症状描述不能为空!");
            return;
        }
        showdialog();
        if (mSelectPath.size() != 0) {
            BaseManager.pictureUpload(mSelectPath, mPicCallback);//上传图片
        } else {
            BaseManager.newMedicalRecordAdd(mPatient.id, getText(mIllnessEt), getText(mContentEt), getText(mDrugEt), getText(mCheckEt), new ArrayList<PicBean>(), mDataCallback);
        }
    }
}
