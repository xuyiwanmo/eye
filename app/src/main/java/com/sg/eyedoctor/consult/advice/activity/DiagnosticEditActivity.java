package com.sg.eyedoctor.consult.advice.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.manager.SpeechManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.BlueTextView;
import com.sg.eyedoctor.common.view.NoScroolGridView;
import com.sg.eyedoctor.consult.advice.adapter.DiagnosticDrugAdapter;
import com.sg.eyedoctor.consult.advice.bean.AdviceDrug;
import com.sg.eyedoctor.consult.textConsult.adapter.PictureGridAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.CheckBean;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultDetail;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;
import com.sg.eyedoctor.consult.textConsult.request.DrugBean;
import com.sg.eyedoctor.helpUtils.doctorAdvice.constant.Constants;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 诊断建议   未填写
 */
@ContentView(R.layout.activity_diagnostic_advice)
public class DiagnosticEditActivity extends BaseActivity implements DiagnosticDrugAdapter.UpdateGrugCount {

    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.drug_advice_et)
    private EditText mDrugAdviceEt;
    @ViewInject(R.id.check_advice_et)
    private EditText mCheckAdviceEt;
    @ViewInject(R.id.illness_tv)
    private TextView mIllnessTv;
    @ViewInject(R.id.empty_img_tv)
    private TextView mEmptyImgTv;
    //    @ViewInject(R.id.drug_add_img)
//    private ImageView mDrugAddImg;
//    @ViewInject(R.id.drug_lv)
//    private NoScrollListView mDrugLv;
//    @ViewInject(R.id.check_add_img)
//    private ImageView mCheckAddImg;
//    @ViewInject(R.id.check_lv)
//    private NoScrollListView mCheckLv;
    @ViewInject(R.id.advice_record_img)
    private ImageView mRecordImg;
    @ViewInject(R.id.drug_record_img)
    private ImageView mDrugRecordImg;
    @ViewInject(R.id.check_record_img)
    private ImageView mCheckRecordImg;

    @ViewInject(R.id.diagnose_tv)
    private EditText mDiagnoseEt;
    @ViewInject(R.id.result_record_img)
    private ImageView mResultRecordImg;
    @ViewInject(R.id.diagnose_result_et)
    private EditText mResultEt;
    @ViewInject(R.id.post_tv)
    private TextView mPostTv;
    @ViewInject(R.id.picture_gv)
    private NoScroolGridView mPictureGv;
    @ViewInject(R.id.img_tv)
    private BlueTextView mImgTv;

//    private ArrayList<AdviceCheck> mChecks = new ArrayList<>();
//    private ArrayList<AdviceDrug> mDrugs = new ArrayList<>();
//    private ArrayList<AdviceDrug> mTempDrugs = new ArrayList<>();
//    private DiagnosticCheckAdapter mCheckAdapter;
//    private DiagnosticDrugAdapter mDrugAdapter;

    private AdviceDrug grug;
    private Patient mPatient;
    private ConsultDetail mConsultDetail;

    private PictureGridAdapter mPictureGridAdapter;
    private ArrayList<Picture> mPictures = new ArrayList<>();
    private boolean isTextConsult;
    private ArrayList<DrugBean> drugBeans;
    private ArrayList<CheckBean> checkBeans;

    private NetCallback mPostCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                DialogManager.showConfimDlg(mContext, "您已提交成功!", new DlgClick() {
                    @Override
                    public boolean click() {
                        Intent intent = new Intent(mContext, DiagnosticShowActivity.class);
                        intent.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                        intent.putExtra(ConstantValues.KEY_END, true);
                        intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, isTextConsult);
                        startActivity(intent);
                        AppManager.getAppManager().finishActivity();
                        return false;
                    }
                });
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


    @Override
    protected void initView() {
        mPatient = getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);
        mConsultDetail = getIntent().getParcelableExtra("consultDetail");
        isTextConsult = getIntent().getBooleanExtra("isTextConsult", false);


//        mCheckAdapter = new DiagnosticCheckAdapter(this, mChecks, 0);
//        mCheckLv.setAdapter(mCheckAdapter);
//
//        mDrugAdapter = new DiagnosticDrugAdapter(this, mDrugs, 0, this);
//        mDrugLv.setAdapter(mDrugAdapter);

        mPictureGridAdapter = new PictureGridAdapter(this, mPictures, 0);
        mPictureGv.setAdapter(mPictureGridAdapter);


    }

    @Override
    protected void initListener() {
//        mCheckLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                DialogManager.showConfimCancelDlg(mContext, R.string.delete_confirm, new DlgClick() {
//                    @Override
//                    public boolean click() {
//                        mChecks.remove(position);
//                        mCheckAdapter.setData(mChecks);
//                        UiUtils.setHeight(mContext, mCheckAdapter, mCheckLv);
//                        return false;
//
//                    }
//                }, new DlgClick() {
//                    @Override
//                    public boolean click() {
//                        return false;
//                    }
//                });
//                return false;
//            }
//        });

        mResultEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = Constants.sShortSize - s.length();

                selectionStart = mResultEt.getSelectionStart();
                selectionEnd = mResultEt.getSelectionEnd();
                if (temp.length() > Constants.sShortSize) {
                    showToast("最大可输入字数为" + Constants.sShortSize);
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mResultEt.setText(s);
                    mResultEt.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        mDiagnoseEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = Constants.sLongSize - s.length();

                selectionStart = mResultEt.getSelectionStart();
                selectionEnd = mResultEt.getSelectionEnd();
                if (temp.length() > Constants.sLongSize) {
                    showToast("最大可输入字数为" + Constants.sLongSize);
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mDiagnoseEt.setText(s);
                    mDiagnoseEt.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        mPictureGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, LookBigPicActivity.class);

                intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                for (Picture s : mPictures) {
                    picDataList.add(new PicLocalBean(s.picUrl, s.picUrl));
                }
                intent.putExtra(LookBigPicActivity.PICDATALIST, picDataList);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }

//    @Event(R.id.drug_add_img)
//    private void addDrug(View view) {
//        startActivityForResult(new Intent(this, DrugAdviceActivity.class), 1);
//    }
//
//    @Event(R.id.check_add_img)
//    private void addCheck(View view) {
//        startActivityForResult(new Intent(this, CheckAdviceActivity.class), 2);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            mTempDrugs = data.getParcelableArrayListExtra("data");
//
//            for (AdviceDrug grug : mDrugs) {
//                for (int i = 0; i < mTempDrugs.size(); i++) {
//                    AdviceDrug tempGrug = mTempDrugs.get(i);
//                    if (tempGrug.id.equals(grug.id)) {
//                        grug.count = grug.count + tempGrug.count;
//                        mTempDrugs.remove(i);
//                        continue;
//                    }
//                }
//            }
//            mDrugs.addAll(mTempDrugs);
//            mDrugAdapter.setData(mDrugs);
//        }
//        if (requestCode == 2 && resultCode == RESULT_OK) {
//            AdviceCheck check = data.getParcelableExtra("data");
//            for (int i = 0; i < mChecks.size(); i++) {
//                AdviceCheck adviceCheck = mChecks.get(i);
//
//                if (adviceCheck.id.equals(check.id)) {
//                    mChecks.remove(i);
//                }
//
//            }
//            mChecks.add(check);
//            mCheckAdapter.setData(mChecks);
//
//            UiUtils.setHeight(this, mCheckAdapter, mCheckLv);
//
//        }
    }


    @Override
    public void increase(int postion) {
//        AdviceDrug grug = mDrugs.get(postion);
//        grug.count++;
//        mDrugAdapter.setData(mDrugs);

    }

    @Override
    public void decrease(final int postion) {
//        grug = mDrugs.get(postion);
//        if (grug.count - 1 == 0) {
//            DialogManager.showConfimCancelDlg(mContext, R.string.delete_when_decrease_to_zero, new DlgClick() {
//
//                @Override
//                public boolean click() {
//                    mDrugs.remove(postion);
//                    mDrugAdapter.setData(mDrugs);
//                    return false;
//                }
//            }, new DlgClick() {
//                @Override
//                public boolean click() {
//                    return false;
//                }
//            });
//        } else {
//
//            grug.count--;
//            mDrugAdapter.setData(mDrugs);
//        }

    }

    @Override
    public void edit(int postion, String count) {

    }


    @Event(R.id.post_tv)
    private void postAdvice(View view) {

        drugBeans = new ArrayList<>();
        checkBeans = new ArrayList<>();

//        for (AdviceDrug drug : mDrugs) {
//            drugBeans.add(new DrugBean(drug.id, drug.name, drug.unit, drug.count + ""));
//        }
//
//        for (AdviceCheck check : mChecks) {
//            checkBeans.add(new CheckBean(check.name, check.id));
//        }
        String tip = "";
        if (isTextConsult) {
            tip = "提交成功后，待患者确认咨询结束后将无法进行修改，确定提交？";
        } else {
            tip = "提交成功后，将无法进行修改，确定提交？";
        }

        DialogManager.showConfimCancelDlg(mContext, tip, new DlgClick() {
            @Override
            public boolean click() {
                BaseManager.diagnosisAdviceAdd(mPatient.id, mPatient.doctorId, mPatient.patientId, getText(mResultEt), getText(mDiagnoseEt), getText(mDrugAdviceEt), getText(mCheckAdviceEt), mPostCallback);
                return false;
            }
        }, new DlgClick() {
            @Override
            public boolean click() {
                return false;
            }
        });
    }

    @Event(R.id.advice_record_img)
    private void record(View view) {

        new SpeechManager(mContext).showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mDiagnoseEt.setText(result.toString());
                    mDiagnoseEt.setSelection(mDiagnoseEt.length());
                }
            }
        });
    }

    @Event(R.id.result_record_img)
    private void resultRecord(View view) {

        new SpeechManager(mContext).showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mResultEt.setText(result.toString());
                    mResultEt.setSelection(mResultEt.length());
                }
            }
        });
    }
    @Event(R.id.drug_record_img)
    private void drugRecord(View view) {

        new SpeechManager(mContext).showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mDrugAdviceEt.setText(result.toString());
                    mDrugAdviceEt.setSelection(mDrugAdviceEt.length());
                }
            }
        });
    }

    @Event(R.id.check_record_img)
    private void checkRecord(View view) {

        new SpeechManager(mContext).showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mCheckAdviceEt.setText(result.toString());
                    mCheckAdviceEt.setSelection(mCheckAdviceEt.length());
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        showdialog();
        BaseManager.getDiagnosisAdvice(mPatient.id, mCallback);
    }

    public void parseDataToArray() {
        mPictures.clear();

        if (mConsultDetail.picList != null) {
            for (ConsultDetail.PicList picList : mConsultDetail.picList) {
                mPictures.add(new Picture(picList.picUrl, picList.microPicUrl));
            }
            mPictureGridAdapter.setData(mPictures);
        //    UiUtils.setImgGridviewHeight(this, 4, mPictureGv, mPictures);
        }
        if (mConsultDetail.picList == null || mConsultDetail.picList.size() == 0) {
            mEmptyImgTv.setVisibility(View.VISIBLE);
            mPictureGv.setVisibility(View.GONE);
        } else {
            mEmptyImgTv.setVisibility(View.GONE);
            mPictureGv.setVisibility(View.VISIBLE);
        }

        setTextView(mDiagnoseEt, mConsultDetail.advice);
        parseDetail();
    }


    private void parseDetail() {

        if (mConsultDetail == null) {
            return;
        }
//        for (ConsultDetail.DrugList drugList : mConsultDetail.drugList) {
//            mDrugs.add(new AdviceDrug(drugList.drugId, drugList.drugName, drugList.drugUnit, drugList.drugCount));
//        }
//        mDrugAdapter.setData(mDrugs);
//
//        for (ConsultDetail.CheckList checkList : mConsultDetail.checkList) {
//            mChecks.add(new AdviceCheck(checkList.checkId, checkList.checkName));
//        }
//        mCheckAdapter.setData(mChecks);
        setTextView(mResultEt, mConsultDetail.result);
        setTextView(mDiagnoseEt, mConsultDetail.advice);

        setTextView(mDrugAdviceEt, mConsultDetail.drugList);
        setTextView(mCheckAdviceEt, mConsultDetail.checkList);

        //通过patient获取
        if (mPatient.age != null) {
            setTextView(mAgeTv, mPatient.age + "岁");
        }
        setTextView(mNameTv, mPatient.patientName);
        setTextView(mSexTv, mPatient.sex.equals("1") ? "男" : "女");
        setTextView(mIllnessTv, mPatient.description);

    }
}
