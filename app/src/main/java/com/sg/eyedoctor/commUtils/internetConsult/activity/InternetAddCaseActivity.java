package com.sg.eyedoctor.commUtils.internetConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.activity.ChartFileActivity;
import com.sg.eyedoctor.chartFile.bean.MedicalRecord;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.adapter.AddUrlImageGridAdapter;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.SpeechManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * 添加互联网会诊
 */
@ContentView(R.layout.activity_internet_add_case)
public class InternetAddCaseActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.title_et)
    private EditText mTitleEt;
    @ViewInject(R.id.name_et)
    private EditText mNameEt;
    @ViewInject(R.id.male_img)
    private ImageView mMaleImg;
    @ViewInject(R.id.female_tv)
    private TextView mMaleTv;
    @ViewInject(R.id.female_img)
    private ImageView mFemaleImg;
    @ViewInject(R.id.female_tv)
    private TextView mFemaleTv;
    @ViewInject(R.id.age_et)
    private EditText mAgeEt;
    @ViewInject(R.id.diagnose_tv)
    private EditText mDiagnoseEt;
    @ViewInject(R.id.diagnose_img)
    private ImageView mDiagnoseImg;
    @ViewInject(R.id.illness_et)
    private EditText mIllnessEt;
    @ViewInject(R.id.illness_img)
    private ImageView mIllnessImg;
    @ViewInject(R.id.remark_et)
    private EditText mRemarkEt;
    @ViewInject(R.id.remark_img)
    private ImageView mRemarkImg;
    @ViewInject(R.id.add_photo_gv)
    private GridView mAddPhotoGv;
    @ViewInject(R.id.save_tv)
    private TextView mSaveTv;

    private int mSex = 1;  //0女  1男
    private ArrayList<String> mCameraPath = new ArrayList<>();
    private AddUrlImageGridAdapter mRecAdapter;

    private SpeechManager mSpeechManager;
    private StringBuffer mSpeechStr;

    private ArrayList<String> mDeletePath = new ArrayList<>();//删除的地址图片
    private ArrayList<PicBean> mPicBeans = new ArrayList<>();//导入图片
    private ArrayList<PicLocalBean> mAllPics = new ArrayList<>();//所有图片
    //上传图片
    private NetCallback mPicCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PicBean>>() {
                }.getType();
                BaseArrayResp<PicBean> res = new Gson().fromJson(result, objectType);
                mPicBeans.addAll(res.value);
                BaseManager.netConsultationAdd(mPicBeans, getText(mTitleEt), getText(mRemarkEt), getText(mNameEt), mSex + "", getText(mAgeEt), getText(mDiagnoseEt), getText(mIllnessEt), mCallback);
            } else {
                showToast(R.string.get_data_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.publish_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mActionbar.setTitle(R.string.add_patient_case);
        mActionbar.setRightBtnVisible(View.GONE);
        mActionbar.setRightTv(R.string.import_case, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ChartFileActivity.class), ConstantValues.REQUEST_IMPORT_CASE);
            }
        });

        mMaleImg.setSelected(true);
        mSex = 1;
        mRecAdapter = new AddUrlImageGridAdapter(this, mAllPics, 4);
        mAddPhotoGv.setAdapter(mRecAdapter);
        mAddPhotoGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mAllPics.size()) {
                    chooseImg();
                } else {
                    //查看大图
                    Intent intent = new Intent(mContext, LookBigPicActivity.class);
                    ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                    for (String s : mCameraPath) {
                        picDataList.add(new PicLocalBean(s, s));
                    }
                    intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                    intent.putExtra(LookBigPicActivity.PICDATALIST, picDataList);
                    startActivity(intent);
                }
            }
        });
        mSpeechStr = new StringBuffer();
        mSpeechManager = new SpeechManager(mContext);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantValues.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {

                Iterator<PicLocalBean> iterator = Collections.synchronizedList(mAllPics).iterator();
                while (iterator.hasNext()) {
                    PicLocalBean item = iterator.next();
                    if (item.localUrl != null) {
                        iterator.remove();
                    }
                }

                mCameraPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String s : mCameraPath) {
                    PicLocalBean bean = new PicLocalBean();
                    bean.localUrl = s;
                    mAllPics.add(bean);
                }

                notifyPicAdapter();
            }
        } else if (requestCode == ConstantValues.REQUEST_IMPORT_CASE) {
            if (resultCode == RESULT_OK) {
                MedicalRecord mRecord = data.getParcelableExtra(ConstantValues.KEY_RECORD);
                setTextView(mNameEt, mRecord.name);
                setTextView(mIllnessEt, mRecord.illness);
                setTextView(mDiagnoseEt, mRecord.diagnosis);
                setTextView(mAgeEt, mRecord.age + "");
                setTextView(mRemarkEt, mRecord.remark);
                if (mRecord.sex==1) {
                    male(mMaleImg);
                }else{
                    female(mMaleImg);
                }
                mAllPics.clear();
                mCameraPath.clear();
                if (mRecord.picList != null) {
                    mPicBeans = mRecord.picList;
                    for (PicBean picBean : mRecord.picList) {
                        mAllPics.add(new PicLocalBean(picBean.picUrl, picBean.picUrl));
                    }
                }
                notifyPicAdapter();
            }
        }else if (requestCode == ConstantValues.REQUEST_DELETE_IMAGE) {
            if (resultCode == RESULT_OK) {
                mDeletePath = data.getStringArrayListExtra(ConstantValues.KEY_DELETE);
                LogUtils.i("mDeletePath  :" + mDeletePath.size());
                int size = mAllPics.size();
                //从总图片集合删除
                for (String s : mDeletePath) {
                    for (int i = 0; i < size; i++) {
                        PicLocalBean bean = mAllPics.get(i);
                        if ((bean.picUrl != null && bean.picUrl.equals(s))
                                || (bean.localUrl != null && bean.localUrl.equals(s))) {
                            size--;
                            mAllPics.remove(i);
                        }
                    }
                }

                //删除本地相册
                size = mCameraPath.size();
                //从总图片集合删除
                for (String s : mDeletePath) {
                    for (int i = 0; i < size; i++) {
                        String bean = mCameraPath.get(i);
                        if (bean.equals(s)) {
                            size--;
                            mCameraPath.remove(i);
                        }
                    }
                }

                //删除网络
                size = mPicBeans.size();
                //从总图片集合删除
                for (String s : mDeletePath) {
                    for (int i = 0; i < size; i++) {
                        PicBean bean = mPicBeans.get(i);
                        if (bean.picUrl != null && bean.picUrl.equals(s)) {
                            size--;
                            mPicBeans.remove(i);
                        }
                    }
                }

                notifyPicAdapter();
            }
        }
    }

    @Event(R.id.save_tv)
    private void save(View view) {
        if (CommonUtils.isEmpty(mTitleEt)) {
            showToast("标题" + getString(R.string.can_not_empty));
            return;
        }
        if (CommonUtils.isEmpty(mNameEt)) {
            showToast("姓名" + getString(R.string.can_not_empty));
            return;
        }
        if (CommonUtils.isEmpty(mAgeEt)) {
            showToast("年龄" + getString(R.string.can_not_empty));
            return;
        }
        if (!CommonUtils.isNumeric(mAgeEt.getText().toString())) {
            showToast("年龄" + getString(R.string.can_not_num));
            return;
        }
        if (CommonUtils.isEmpty(mDiagnoseEt)) {
            showToast("疾病诊断" + getString(R.string.can_not_empty));
            return;
        }
        if (CommonUtils.isEmpty(mIllnessEt)) {
            showToast("症状描述" + getString(R.string.can_not_empty));
            return;
        }
        if (CommonUtils.isEmpty(mRemarkEt)) {
            showToast("备注" + getString(R.string.can_not_empty));
            return;
        }

        showdialog();

        if (mCameraPath.size() == 0) {
            BaseManager.netConsultationAdd(mPicBeans, getText(mTitleEt), getText(mRemarkEt), getText(mNameEt), mSex + "", getText(mAgeEt), getText(mDiagnoseEt), getText(mIllnessEt), mCallback);
        } else {
            BaseManager.pictureUpload(mCameraPath, mPicCallback);//上传图片
        }


    }

    private void chooseImg() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9-mAllPics.size());
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        if (mCameraPath != null && mCameraPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mCameraPath);
        }
        startActivityForResult(intent, ConstantValues.REQUEST_IMAGE);
    }

    @Event({R.id.male_img, R.id.male_tv})
    private void male(View view) {
        mMaleImg.setSelected(true);
        mFemaleImg.setSelected(false);
        mSex = 1;
    }

    @Event({R.id.female_img, R.id.female_tv})
    private void female(View view) {
        mMaleImg.setSelected(false);
        mFemaleImg.setSelected(true);
        mSex = 0;
    }

    @Event({R.id.diagnose_img, R.id.remark_img, R.id.illness_img})
    private void speech(final View view) {
        mSpeechStr = new StringBuffer();
        mSpeechManager.showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {

                if (isLast) {
                    mSpeechStr.append(result);
                }

                String str = mSpeechStr.toString();
                switch (view.getId()) {
                    case R.id.diagnose_img:
                        mDiagnoseEt.setText(str);
                        break;
                    case R.id.remark_img:
                        mRemarkEt.setText(str);
                        break;
                    case R.id.illness_img:
                        mIllnessEt.setText(str);
                        break;
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpeechManager.destroy();
    }

    private void notifyPicAdapter() {
        mRecAdapter.setData(mAllPics);
        setViewHeightBasedOnChildren(mAddPhotoGv, 16, 16);
        mRecAdapter.notifyDataSetChanged();
    }
}
