package com.sg.eyedoctor.loginRegister.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.CommFragment;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.loginRegister.activity.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_guide)
public class GuideFragment extends CommFragment {
    @ViewInject(R.id.experience_tv)
    private TextView mExperienceTv;
    @ViewInject(R.id.guide_img)
    private ImageView mGuideImg;

    private boolean mShowExperience;
    private int mImgId;

    public GuideFragment(boolean showexperience,int imgId) {
        mShowExperience=showexperience;
        mImgId=imgId;
    }

    @Override
    protected void initView() {
        ImageLoader.getInstance().displayImage("drawable://" + mImgId, mGuideImg);
        if(mShowExperience){
            mExperienceTv.setVisibility(View.VISIBLE);
        }else{
            mExperienceTv.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initListener() {

    }

    @Event(R.id.experience_tv)
    private void experience(View view) {
        LoginActivity.start(getActivity());
        AppManager.getAppManager().finishActivity();
    }
}
