package com.sg.eyedoctor.commUtils.caseDiscuss.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.PictureAdapter;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail.PicList;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


@ContentView(R.layout.activity_look_patient_case)
public class LookPatientCaseActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.ill_check_tv)
    private TextView mIllCheckTv;
    @ViewInject(R.id.ill_tv)
    private TextView mIllTv;
    @ViewInject(R.id.illness_tv)
    private TextView mIllnessTv;
    @ViewInject(R.id.img_gv)
    private GridView mImgGv;
    private CaseDiscussDetail mCaseDiscussDetail;

    private ArrayList<PicList> mPicLists=new ArrayList<>();
    private PictureAdapter mPictureAdapter;

    @Override
    protected void initView() {
        mActionbar.setRightBtnVisible(View.INVISIBLE);
        mActionbar.setTitle(R.string.look_patient_case);

        Intent intent = getIntent();
        mCaseDiscussDetail = intent.getParcelableExtra(ConstantValues.EXTRA_RESULT);
        mPicLists=mCaseDiscussDetail.picList;
        setTextView(mNameTv,mCaseDiscussDetail.patientName);
        setTextView(mAgeTv,mCaseDiscussDetail.age+"");
        setTextView(mSexTv,mCaseDiscussDetail.sex.equals("0")?"女":"男");
        setTextView(mIllnessTv,mCaseDiscussDetail.illness);
        setTextView(mIllCheckTv,mCaseDiscussDetail.diagnosisResult);

        if (mPicLists==null) {
            mPicLists=new ArrayList<>();
        }
        mPictureAdapter=new PictureAdapter(this,mPicLists,0);
        mImgGv.setAdapter(mPictureAdapter);

        if (mPicLists==null||mPicLists.size()==0){
            mImgGv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        mImgGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, LookBigPicActivity.class);
                intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                intent.putExtra(LookBigPicActivity.PICDATALIST, getPictureList(mPicLists));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    private ArrayList<PicLocalBean> getPictureList(ArrayList<PicList> picLists) {
        ArrayList<PicLocalBean> pictures=new ArrayList<>();
        for (PicList picList : picLists) {
            pictures.add(new PicLocalBean(picList.microPicUrl,picList.picUrl));
        }
        return pictures;
    }


}



