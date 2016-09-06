package com.sg.eyedoctor.consult.phoneConsult.activity;

import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.netease.nim.common.util.string.StringUtil;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.consult.phoneConsult.adapter.PhoneConsultTimeSettingAdapter;
import com.sg.eyedoctor.consult.phoneConsult.bean.TimeSet;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@ContentView(R.layout.activity_phone_consult_time)
public class PhoneConsultTimeActivity extends BaseActivity implements PhoneConsultTimeSettingAdapter.SetTimeCallback {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.time_lv)
    private NoScrollListView mTimeLv;

    private ArrayList<TimeSet> mTimeSets;
    private PhoneConsultTimeSettingAdapter mSettingAdapter;
    private TimePickerView mTimePickerView;
    private OptionsPickerView<Integer> mNumPickerView;
    private ArrayList<Integer> mNums = new ArrayList<>();

    private int mPosition = 0;
    private int mDay = 1;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.post_ok);
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
        mTimeSets = new ArrayList<>();
        mDay = getIntent().getIntExtra(ConstantValues.KEY_DAY, 1);
        if (getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA) != null) {
            mTimeSets = getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
        }

        mSettingAdapter = new PhoneConsultTimeSettingAdapter(this, mTimeSets, 0);
        mSettingAdapter.setCallback(this);
        mTimeLv.setAdapter(mSettingAdapter);

        //时间选择器
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        Date a = new Date();
        a.setHours(0);
        a.setMinutes(0);
        a.setSeconds(0);
        mTimePickerView.setTime(a);
        mTimePickerView.setCyclic(false);
        mTimePickerView.setCancelable(true);

        //数字选择器
        for (int i = 1; i <= 48; i++) {
            mNums.add(i);
        }
        mNumPickerView = new OptionsPickerView<>(this);
        mNumPickerView.setPicker(mNums);
        mNumPickerView.setCyclic(false);
        mNumPickerView.setCancelable(true);

    }

    @Override
    protected void initListener() {
        //时间选择后回调
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                date.setDate(10);
                date.setYear(2012);
                date.setMonth(10);
                TimeSet timeSet = mTimeSets.get(mPosition);
                timeSet.time = date.getTime();
                mSettingAdapter.setData(mTimeSets);
            }
        });
        mNumPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                TimeSet timeSet = mTimeSets.get(mPosition);
                timeSet.count = mNums.get(options1);
                mSettingAdapter.setData(mTimeSets);
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDate()) {//可以上传
                    showdialog();

                    ArrayList<String> times = new ArrayList<String>();

                    for (TimeSet timeSet : mTimeSets) {
                        for (int i = 0; i < timeSet.count; i++) {
                            times.add(CommonUtils.getTime(new Date(timeSet.time + 30 * 60 * 1000 * i)));
                        }
                    }
                    for (String time : times) {
                        LogUtils.i(time);
                    }
                    BaseManager.vasDutyRosterSet(mDay, times, mCallback);
                }
            }
        });

    }

    private boolean checkDate() {
        if (mTimeSets.size() == 0) {
            DialogManager.showConfimDlg(mContext, R.string.empty_set_time, new DlgClick() {
                @Override
                public boolean click() {
                    return false;
                }
            });
            return false;
        }
        for (TimeSet timeSet : mTimeSets) {
            if (timeSet.count == 0) {
                DialogManager.showConfimDlg(mContext, R.string.error_set_time_count_zero, new DlgClick() {
                    @Override
                    public boolean click() {
                        return false;
                    }
                });
                return false;
            }
        }
//        for (TimeSet timeSet : mTimeSets) {
//            timeSet.lastTime = timeSet.time + 30 * 60 * 1000 * timeSet.count;
//        }

        boolean check = true;

        ArrayList<TimeSet> tempTimes = new ArrayList<TimeSet>();
        for (TimeSet timeSet : mTimeSets) {
            for (int i = 0; i < timeSet.count; i++) {
                tempTimes.add(new TimeSet(timeSet.time + 30 * 60 * 1000 * i, 1));
            }
        }

        Collections.sort(tempTimes, new SortClass());
        for (TimeSet tempTime : tempTimes) {
            LogUtils.i("--" + CommonUtils.getDetailTime(new Date(tempTime.time)));
        }
        for (int i = 0; i < tempTimes.size() - 1; i++) {
            int j = i + 1;
            if (getMinutesDiff(CommonUtils.longToHM(tempTimes.get(i).time), CommonUtils.longToHM(tempTimes.get(j).time))
                    < 30) {
                check = false;
                break;
            }
        }

        if (!check) {
            DialogManager.showConfimDlg(mContext, R.string.error_set_time, new DlgClick() {
                @Override
                public boolean click() {
                    return false;
                }
            });
        }
        return check;

    }

    @Override
    public void request(int position) {
        mPosition = position;
        mNumPickerView.show();
    }

    @Override
    public void clickTime(int position) {
        mPosition = position;
        mTimePickerView.show();
    }

    @Override
    public void setCount(int position, String num) {
        if (num == null || num.equals("")) {
            return;
        }
        TimeSet timeSet = mTimeSets.get(position);

        timeSet.count = Integer.valueOf(num);

        mSettingAdapter.setData(mTimeSets);
    }

    @Override
    public void decrease(final int postion) {

        DialogManager.showConfimCancelDlg(mContext, R.string.delete_confirm, new DlgClick() {
            @Override
            public boolean click() {
                mTimeSets.remove(postion);
                mSettingAdapter.setData(mTimeSets);
                return false;
            }
        }, new DlgClick() {
            @Override
            public boolean click() {
                return false;
            }
        });

    }

    @Event(R.id.add_ll)
    private void addTime(View view) {
        mTimeSets.add(new TimeSet(System.currentTimeMillis(), 1));
        mSettingAdapter.setData(mTimeSets);
    }

    class SortClass implements Comparator {

        @Override
        public int compare(Object time1, Object time2) {
            long flag =((TimeSet) time1).time- ((TimeSet) time2).time;

            if (flag < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static int getMinutesDiff(String startDate, String endDate) {
        int ret = 0;
        if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)) {
            // return ret;
        } else {
            String startDateStr[] = startDate.split(":");
            String endDateStr[] = endDate.split(":");
            if (startDateStr[0].startsWith("0")) {
                startDateStr[0] = startDateStr[0].substring(1);
            }
            if (startDateStr[1].startsWith("0")) {
                startDateStr[1] = startDateStr[1].substring(1);
            }
            if (endDateStr[0].startsWith("0")) {
                endDateStr[0] = endDateStr[0].substring(1);
            }
            if (endDateStr[1].startsWith("0")) {
                endDateStr[1] = endDateStr[1].substring(1);
            }
            int s = Integer.parseInt(startDateStr[0]) * 60 + Integer.parseInt(startDateStr[1]);
            int e = Integer.parseInt(endDateStr[0]) * 60 + Integer.parseInt(endDateStr[1]);
            ret = e - s;
        }
        LogUtils.i("ret:  "+ret);
        return ret;

    }
}
