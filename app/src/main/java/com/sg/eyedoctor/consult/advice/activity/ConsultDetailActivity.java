package com.sg.eyedoctor.consult.advice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
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
 * 咨询详情
 */
@ContentView(R.layout.activity_consult_detail)
public class ConsultDetailActivity extends BaseActivity {

    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.sex_tv)
    private TextView mSexTv;
    @ViewInject(R.id.age_tv)
    private TextView mAgeTv;
    @ViewInject(R.id.illness_tv)
    private TextView mIllnessTv;
    @ViewInject(R.id.empty_tv)
    private TextView mEmptyTv;
    @ViewInject(R.id.picture_gv)
    private GridView mPictureGv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private Patient mPatient;
    private ConsultDetail mConsultDetail;

    private PictureGridAdapter mPictureGridAdapter;
    private ArrayList<Picture> mPictures=new ArrayList<>();

    private int mType=0;  //1更多  0隐藏
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
        mPatient=getIntent().getParcelableExtra(ConstantValues.KEY_PATIENT);
        mType=getIntent().getIntExtra(ConstantValues.KEY_TYPE, 0);
        setTextView(mNameTv,mPatient.patientName);
        setTextView(mSexTv,mPatient.sex.equals("1")?"男":"女");
        setTextView(mIllnessTv,mPatient.description,"未填写");
        setTextView(mAgeTv,mPatient.age+"岁");

        mPictureGridAdapter=new PictureGridAdapter(this,mPictures,0);
        mPictureGv.setAdapter(mPictureGridAdapter);
        showdialog();
        BaseManager.getDiagnosisAdvice(mPatient.id, mCallback);
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
        //if(mType==1){//更多
            mActionbar.setRightTv(R.string.more, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ConsultPatientDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity();
                }
            });
      //  }
    }

    public void parseDataToArray() {
        mPictures.clear();
        for (ConsultDetail.PicList picList : mConsultDetail.picList) {
            mPictures.add(new Picture(picList.picUrl,picList.microPicUrl));
        }

        if(mPictures.size()==0){
            mPictureGv.setVisibility(View.GONE);
            mEmptyTv.setVisibility(View.VISIBLE);
        }else{
            mPictureGridAdapter.setData(mPictures);
            UiUtils.setImgGridviewHeight(this, 4, mPictureGv, mPictures);
            mPictureGv.setVisibility(View.VISIBLE);
            mEmptyTv.setVisibility(View.GONE);
        }
    }

}
