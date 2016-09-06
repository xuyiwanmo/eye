package com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 停诊通知
 */
@ContentView(R.layout.activity_stop_diagnose_notice)
public class StopDiagnoseNoticeActivity extends BaseActivity {
    public static final int TYPE_FROM_TIME = 0;
    public static final int TYPE_TO_TIME = 1;
    public static final int TYPE_FROM_DATE = 2;
    public static final int TYPE_TO_DATE = 3;

    public static final int WORK_PLAN = 1;
    public static final int SOMETHING_TEMPORY = 2;
    public static final int OTHER = 3;

    private int mOutpatientType = 0;

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.from_date_tv)
    private TextView mFromDateTv;
    @ViewInject(R.id.to_date_tv)
    private TextView mToDateTv;
    @ViewInject(R.id.from_time_tv)
    private TextView mFromTimeTv;
    @ViewInject(R.id.to_time_tv)
    private TextView mToTimeTv;
    @ViewInject(R.id.stop_reason_tv)
    private TextView mStopReasonTv;

    @ViewInject(R.id.stop_type_rl)
    private RelativeLayout mTypeRl;
    @ViewInject(R.id.stop_reason_rl)
    private RelativeLayout mReasonRl;

    @ViewInject(R.id.publish_tv)
    private TextView mPublishTv;

    private TimePickerView mTimeView;
    private TimePickerView mDateView;
    public int mDateType = TYPE_FROM_DATE;
    public int mTimeType = TYPE_FROM_TIME;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                showToast(R.string.publish_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        //停诊年月日
        mDateView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        mDateView.setTime(new Date());
        mDateView.setCyclic(false);
        mDateView.setCancelable(true);

        //停诊时:分
        mTimeView = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        mTimeView.setTime(new Date());
        mTimeView.setCyclic(false);
        mTimeView.setCancelable(true);
    }

    @Override
    protected void initListener() {
        for (int i = 0; i < mTypeRl.getChildCount(); i++) {
            final TextView textView = (TextView) mTypeRl.getChildAt(i);

            textView.setTag(R.id.tag_position, i);
            textView.setTag(R.id.tag_value, false);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = (boolean) v.getTag(R.id.tag_value);
                    isSelected = !isSelected;
                    v.setSelected(isSelected);
                    v.setTag(R.id.tag_value, isSelected);
                }
            });
        }

        for (int i = 0; i < mReasonRl.getChildCount(); i++) {
            final TextView textView = (TextView) mReasonRl.getChildAt(i);

            textView.setTag(R.id.tag_position, i);
            textView.setTag(R.id.tag_value, false);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < mReasonRl.getChildCount(); j++) {
                        TextView textView = (TextView) mReasonRl.getChildAt(j);
                        textView.setSelected(false);
                        textView.setTag(R.id.tag_value, false);
                    }

                    v.setSelected(true);
                    v.setTag(R.id.tag_value, true);
                    mOutpatientType = (int) v.getTag(R.id.tag_position) + 1;
                }
            });
        }

        //时间选择后回调
        mTimeView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String str = "HH:mm";

                if (mTimeType == TYPE_FROM_TIME) {
                    mFromTimeTv.setTextColor(getcolor(R.color.black));
                    mFromTimeTv.setText(getTime(str, date));

                } else {
                    mToTimeTv.setTextColor(getcolor(R.color.black));
                    mToTimeTv.setText(getTime(str, date));

                }
            }
        });
        //时间选择后回调
        mDateView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String str = "yyyy-MM-dd";
                if (mDateType == TYPE_FROM_DATE) {
                    mFromDateTv.setTextColor(getcolor(R.color.black));
                    mFromDateTv.setText(getTime(str, date));

                } else {
                    mToDateTv.setTextColor(getcolor(R.color.black));
                    mToDateTv.setText(getTime(str, date));

                }
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setTitle(R.string.stop_diagnose_notice);
        mActionbar.setRightTv(R.string.already_stop_diagnose, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AlreadyStopDiagnoseActivity.class));
            }
        });
    }

    @Event(R.id.from_time_tv)
    private void chooseFromTime(View view) {
        mTimeType = TYPE_FROM_TIME;
        mTimeView.show();

    }

    @Event(R.id.to_time_tv)
    private void chooseToTime(View view) {
        mTimeType = TYPE_TO_TIME;
        mTimeView.show();
    }

    @Event(R.id.from_date_tv)
    private void chooseFromDate(View view) {
        mDateType = TYPE_FROM_DATE;
        mDateView.show();

    }

    @Event(R.id.to_date_tv)
    private void chooseToDate(View view) {
        mDateType = TYPE_TO_DATE;
        mDateView.show();
    }

    @Event(R.id.publish_tv)
    private void save(View view) {

        if (TextUtils.isEmpty(mFromDateTv.getText()) || TextUtils.isEmpty(mToDateTv.getText())) {
            showToast("停诊日期" + getString(R.string.can_not_empty));
            return;
        }

        if (getText(mFromDateTv).compareTo(getText(mToDateTv)) > 0) {
            showToast("停诊日期" + getString(R.string.error_choose_time));
            return;
        }
        if (TextUtils.isEmpty(mFromTimeTv.getText()) || TextUtils.isEmpty(mToTimeTv.getText())) {
            showToast("停诊时间" + getString(R.string.can_not_empty));
            return;
        }
        if (getText(mFromTimeTv).compareTo(getText(mToTimeTv)) >= 0) {
            showToast("停诊时间" + getString(R.string.error_choose_time));
            return;
        }

        StringBuffer typeList=new StringBuffer("");
        for (int i = 0; i < mTypeRl.getChildCount(); i++) {
            final TextView textView = (TextView) mTypeRl.getChildAt(i);

            if ((boolean) textView.getTag(R.id.tag_value)) {
                typeList.append(((int)textView.getTag(R.id.tag_position)+1)+",");
            }
        }
        if (typeList.length()==0) {
            showToast("请选择停诊类型");
            return;
        }

        String stopReason = "";
        for (int i = 0; i < mReasonRl.getChildCount(); i++) {
            final TextView textView = (TextView) mReasonRl.getChildAt(i);

            if ((boolean) textView.getTag(R.id.tag_value)) {
                stopReason = textView.getText().toString();
            }
        }
        if (TextUtils.isEmpty(stopReason)) {
            showToast("请选择停诊原因");
            return;
        }

        showdialog();
        BaseManager.stopNotice(stopReason, getText(mFromDateTv), getText(mToDateTv), getText(mFromTimeTv), getText(mToTimeTv), typeList.substring(0,typeList.length()-1).toString(), mCallback);

    }

    public static String getTime(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    private boolean compareTime(String time1, String time2) {
        int i = time1.compareTo(time2);
        if (i > 0) {
            return true;
        }

        return false;
    }
}
