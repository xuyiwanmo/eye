package com.sg.eyedoctor.main.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.settings.personalInfo.activity.DoctorApproveActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_certification)
public class CertificationActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.certification_tv)
    private TextView mCertificationTv;
    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Event(R.id.certification_tv)
    private void certification(View view) {
        Intent intent=new Intent(mContext, DoctorApproveActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initActionbar() {
        String title = getIntent().getStringExtra(ConstantValues.KEY_TITLE);
        mActionbar.setTitle(title);
    }

}
