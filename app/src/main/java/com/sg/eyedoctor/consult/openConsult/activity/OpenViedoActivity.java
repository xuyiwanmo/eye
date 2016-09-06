package com.sg.eyedoctor.consult.openConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.openConsult.bean.ConsultBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 视频咨询  认证开启
 */
@ContentView(R.layout.activity_open_viedo)
public class OpenViedoActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.detail_tv)
    private TextView mDetailTv;
    @ViewInject(R.id.set_img)
    private ImageView mSetImg;
    @ViewInject(R.id.open_tv)
    private TextView mOpenTv;

    private ConsultBean mConsultBean;

    @Override
    protected void initView() {
        mConsultBean=getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
        String detail=getString(R.string.not_open_consult).replace(" ",getString(mConsultBean.titleId));
        mDetailTv.setText(detail);
        mActionbar.setTitle(mConsultBean.titleId);
        mSetImg.setImageResource(R.drawable.ic_exclamation_mark);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.open_tv)
    private void open(View view) {

        startActivity(new Intent(mContext, mConsultBean.activity));
        AppManager.getAppManager().finishActivity();
    }

}
