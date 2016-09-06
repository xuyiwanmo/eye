package com.sg.eyedoctor.helpUtils.doctorAsist.activity;

import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 辅助工具-医生助理-留言
 */
@ContentView(R.layout.activity_leave_word)
public class LeaveWordActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.leave_word);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
    }
}
