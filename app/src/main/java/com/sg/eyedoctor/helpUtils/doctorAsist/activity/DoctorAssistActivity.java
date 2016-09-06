package com.sg.eyedoctor.helpUtils.doctorAsist.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_doctor_assist)
public class DoctorAssistActivity extends BaseActivity {


    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.hotline_ll)
    private LinearLayout mHotLineLl;

    @ViewInject(R.id.callback_tv)
    private TextView mCallTv;

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.callback_tv)
    private void callback(View view) {

    }

    @Event(R.id.hotline_ll)
    private void hotLine(View view) {
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.hot_line_number)));
        mContext.startActivity(intent);
    }


}
