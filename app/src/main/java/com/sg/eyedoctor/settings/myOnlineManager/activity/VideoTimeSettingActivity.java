package com.sg.eyedoctor.settings.myOnlineManager.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.settings.myOnlineManager.adapter.VideoTimeSettingAdapter;
import com.sg.eyedoctor.settings.myOnlineManager.bean.PostVideoTime;
import com.sg.eyedoctor.settings.myOnlineManager.bean.VideoTime;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;

/**
 * 视频咨询设置时间
 */
@ContentView(R.layout.activity_video_time_setting)
public class VideoTimeSettingActivity extends BaseActivity implements VideoTimeSettingAdapter.ViedoTimeCallback {
    @ViewInject(R.id.video_lv)
    private NoScrollListView mVideoLv;
    @ViewInject(R.id.add_ll)
    private LinearLayout mAddLl;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private VideoTimeSettingAdapter mAdapter;
    private ArrayList<VideoTime> mVideoTimes = new ArrayList<>();
    private TimePickerView mTimePickerView;
    private OptionsPickerView<Integer> mNumPickerView;
    private ArrayList<Integer> mNums = new ArrayList<>();
    private int mType = 1;
    private int mPosition = 0;
    private int mTimeType = 0; //0为startTime 1为endtime

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
        mType = getIntent().getIntExtra(ConstantValues.KEY_DAY, 1);
        mVideoTimes=getIntent().getParcelableArrayListExtra(ConstantValues.KEY_DATA);
        initList();
        mAdapter = new VideoTimeSettingAdapter(mContext, mVideoTimes, 0);
        mAdapter.setCallback(this);
        mVideoLv.setAdapter(mAdapter);
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
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                VideoTime time = mVideoTimes.get(mPosition);
                if (mTimeType == 0) {//startime
                    time.startTime = CommonUtils.getTime(date);
                } else {
                    time.endTime = CommonUtils.getTime(date);
                }
                mAdapter.setData(mVideoTimes);
            }
        });

        mNumPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                VideoTime timeSet = mVideoTimes.get(mPosition);
                timeSet.serviceAmount = mNums.get(options1)+"";
                mAdapter.setData(mVideoTimes);
            }
        });

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkData()) {//时间冲突
                    DialogManager.showConfimDlg(mContext, R.string.error_enter_time, new DlgClick() {
                        @Override
                        public boolean click() {
                            return false;
                        }
                    });
                    return;
                }
                //上传
                ArrayList<PostVideoTime> tempTimes = new ArrayList<PostVideoTime>();
                for (VideoTime videoTime : mVideoTimes) {
                    String serviceTime = videoTime.startTime + "-" + videoTime.endTime;
                    tempTimes.add(new PostVideoTime(serviceTime, videoTime.serviceAmount));
                }
                BaseManager.vasAnnoucementSet(mType + "", tempTimes, mCallback);
            }
        });
    }

    @Override
    public void decrease(int position) {
        mPosition = position;
        mVideoTimes.remove(position);
        mAdapter.setData(mVideoTimes);
    }

    @Override
    public void clickEndTime(int position) {
        mPosition = position;
        mTimeType = 1;
        mTimePickerView.show();
    }

    @Override
    public void clickCount(int position) {
        mPosition = position;
        mNumPickerView.show();
    }

    @Override
    public void clickStartTime(int position) {
        mPosition = position;
        mTimeType = 0;
        mTimePickerView.show();
    }

    @Event(R.id.add_ll)
    private void addTime(View view) {
        long currTime = System.currentTimeMillis();
        mVideoTimes.add(new VideoTime("00:00", "00:00", 1));
        mAdapter.setData(mVideoTimes);
    }

    private boolean checkData() {
        for (VideoTime videoTime : mVideoTimes) {
            if (videoTime.endTime.compareTo(videoTime.startTime)<=0){
                return false;
            }
        }

        for (int i = 0; i < mVideoTimes.size(); i++) {
            VideoTime currTime = mVideoTimes.get(i);
            for (int j = i+1; j < mVideoTimes.size(); j++) {
                VideoTime nextTime = mVideoTimes.get(j);
                //时间小于下一个时间的首   或者大于下一个时间的尾部
                if(currTime.endTime.compareTo(nextTime.startTime)<=0||currTime.startTime.compareTo(nextTime.endTime)>=0){
                }else{
                    return false;
                }
            }
        }
        return true;
    }

    private void initList() {
        for (VideoTime videoTime : mVideoTimes) {
            videoTime.startTime= videoTime.serviceTime.substring(0, 5);
            videoTime.endTime= videoTime.serviceTime.substring(6, 11);
        }
    }


}
