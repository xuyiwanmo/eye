package com.sg.eyedoctor.commUtils.patientReport.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDApplyForm;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.textConsult.adapter.PictureGridAdapter;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 患者报到单
 */
@ContentView(R.layout.activity_report_note)
public class ReportNoteActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.empty_tv)
    private TextView mEmptyTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.date_tv)
    private TextView mDateTv;
    @ViewInject(R.id.result_tv)
    private TextView mResultTv;
    @ViewInject(R.id.illness_tv)
    private TextView mIllnessTv;
    @ViewInject(R.id.picture_ll)
    private LinearLayout mPictureLl;
    @ViewInject(R.id.picture_gv)
    private GridView mPictureGv;
    private PDValidate mPDValidate;
    private PDApplyForm mPDApplyForm;
    private PictureGridAdapter mPictureGridAdapter;
    private ArrayList<Picture> mPictures=new ArrayList<>();

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<PDApplyForm>>() {
                }.getType();
                BaseResp<PDApplyForm> res = new Gson().fromJson(result, objectType);
                mPDApplyForm = res.value;
                mPictures=mPDApplyForm.picList;
                mPictureGridAdapter.setData(mPictures);
                viewInit();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };


    @Override
    protected void initView() {
        mPDValidate = getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
        mPictureGridAdapter=new PictureGridAdapter(mContext,mPictures,0);
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
        if(mPDValidate.friendId!=null){
            BaseManager.getPDApplyForm(mPDValidate.friendId, mCallback);
        }else{
            BaseManager.getPDApplyForm(mPDValidate.patientId, mCallback);
        }

    }

    private void viewInit() {
        setTextView(mNameTv, mPDApplyForm.name);
        setTextView(mAgeTv, mPDApplyForm.age);
        if (mPDValidate.sex != null) {
            setTextView(mSexTv, mPDApplyForm.sex.equals("1")?"男":"女");
        }

        setTextView(mResultTv, mPDApplyForm.diagnosisResult,"未填写");
        setTextView(mIllnessTv, mPDApplyForm.illness, "未填写");
        if(mPDApplyForm.createDate!=null){
            setTextView(mDateTv, mPDApplyForm.treatmentDate.split(" ")[0].replace("/","-"));
        }


        if(mPictures!=null&&mPictures.size()!=0){
            UiUtils.setImgGridviewHeight(this, 4, mPictureGv, mPictures);
            mEmptyTv.setVisibility(View.GONE);
            mPictureGv.setVisibility(View.VISIBLE);
        }else{
            mPictureGv.setVisibility(View.GONE);
            mEmptyTv.setVisibility(View.VISIBLE);
        }

    }

}
