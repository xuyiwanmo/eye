package com.sg.eyedoctor.loginRegister.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.loginRegister.adapter.GuideAdapter;
import com.sg.eyedoctor.loginRegister.fragment.GuideFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


@ContentView(R.layout.activity_guide)
public class GuideActivity extends BaseActivity {
    @ViewInject(R.id.guide_vp)
    private ViewPager mGuideVp;

    private FragmentManager mFragmentManager;
    private Fragment mFirstFragment;
    private Fragment mSecondFragment;
    private Fragment mThirdFragment;
    private ArrayList<Fragment> mFragments=new ArrayList<>();
    private GuideAdapter mGuideAdapter;

    @Override
    protected void initView() {
        mFragmentManager=getSupportFragmentManager();
        mFirstFragment=new GuideFragment(false,R.drawable.guide_1);
        mSecondFragment=new GuideFragment(false,R.drawable.guide_2);
        mThirdFragment=new GuideFragment(true,R.drawable.guide_3);
        mFragments.add(mFirstFragment);
        mFragments.add(mSecondFragment);
        mFragments.add(mThirdFragment);
        mGuideAdapter=new GuideAdapter(mFragmentManager,mFragments);
        mGuideVp.setAdapter(mGuideAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }
}
