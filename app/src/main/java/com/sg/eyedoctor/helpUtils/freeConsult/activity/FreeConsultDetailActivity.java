package com.sg.eyedoctor.helpUtils.freeConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.textConsult.adapter.PictureGridAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultDetail;
import com.sg.eyedoctor.helpUtils.freeConsult.bean.FreePatient;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 咨询详情
 */
@ContentView(R.layout.activity_consult_detail)
public class FreeConsultDetailActivity extends BaseActivity {

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

    private FreePatient mPatient;
    private ConsultDetail mConsultDetail;

    private PictureGridAdapter mPictureGridAdapter;
    private ArrayList<Picture> mPictures=new ArrayList<>();


    @Override
    protected void initView() {
        mPatient=getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
        setTextView(mNameTv,mPatient.name);
        if(mPatient.sex.equals("1")||mPatient.sex.equals("男")){
            setTextView(mSexTv,"男");
        }else{
            setTextView(mSexTv,"女");
        }

        setTextView(mIllnessTv,mPatient.questionDetail);
        setTextView(mAgeTv,mPatient.age+"岁");

        mPictures=mPatient.picList;
        mPictureGridAdapter=new PictureGridAdapter(this,mPictures,0);
        mPictureGv.setAdapter(mPictureGridAdapter);

        if(mPictures==null||mPictures.size()==0){
            mEmptyTv.setVisibility(View.VISIBLE);
            mPictureGv.setVisibility(View.GONE);
        }else{
            mEmptyTv.setVisibility(View.GONE);
            mPictureGv.setVisibility(View.VISIBLE);
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

    public void parseDataToArray() {
        mPictures.clear();
        for (ConsultDetail.PicList picList : mConsultDetail.picList) {
            mPictures.add(new Picture(picList.picUrl,picList.microPicUrl));
        }
        mPictureGridAdapter.setData(mPictures);
        UiUtils.setImgGridviewHeight(this, 4, mPictureGv, mPictures);
    }

}
