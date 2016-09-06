package com.sg.eyedoctor.settings.myOnlineManager.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.DescribeView;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.settings.myOnlineManager.adapter.ShowVideoAdapter;
import com.sg.eyedoctor.settings.myOnlineManager.bean.VideoTime;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 视频咨询开启
 */
@ContentView(R.layout.activity_set_video_consult)
public class SetVideoConsultActivity extends BaseActivity {
    @ViewInject(R.id.monday_tv)
    private TextView mMondayTv;
    @ViewInject(R.id.monday_lv)
    private NoScrollListView mMondayLv;
    @ViewInject(R.id.tuesday_tv)
    private TextView mTuesdayTv;
    @ViewInject(R.id.tuesday_lv)
    private NoScrollListView mTuesdayLv;
    @ViewInject(R.id.wednesday_tv)
    private TextView mWednesdayTv;
    @ViewInject(R.id.wednesday_lv)
    private NoScrollListView mWednesdayLv;
    @ViewInject(R.id.thursday_tv)
    private TextView mThursdayTv;
    @ViewInject(R.id.thursday_lv)
    private NoScrollListView mThursdayLv;
    @ViewInject(R.id.friday_tv)
    private TextView mFridayTv;
    @ViewInject(R.id.friday_lv)
    private NoScrollListView mFridayLv;
    @ViewInject(R.id.saturday_tv)
    private TextView mSaturdayTv;
    @ViewInject(R.id.saturday_lv)
    private NoScrollListView mSaturdayLv;
    @ViewInject(R.id.sunday_tv)
    private TextView mSundayTv;
    @ViewInject(R.id.sunday_lv)
    private NoScrollListView mSundayLv;
    @ViewInject(R.id.open_dv)
    private DescribeView mOpenDv;
    @ViewInject(R.id.price_et)
    private EditText mPriceTv;
    @ViewInject(R.id.price_all_sv)
    private ScrollView mPriceSv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.count_et)
    private TextView mCountEt;

    private ArrayList<VideoTime> mVideoTimes = new ArrayList<>();
    private ArrayList<VideoTime> mMondayTimes = new ArrayList<>();
    private ArrayList<VideoTime> mTuesdayTimes = new ArrayList<>();
    private ArrayList<VideoTime> mWednesdayTimes = new ArrayList<>();
    private ArrayList<VideoTime> mThursdayTimes = new ArrayList<>();
    private ArrayList<VideoTime> mFridayTimes = new ArrayList<>();
    private ArrayList<VideoTime> mSaturdayTimes = new ArrayList<>();
    private ArrayList<VideoTime> mSundayTimes = new ArrayList<>();
    private ShowVideoAdapter mMondayAdapter;
    private ShowVideoAdapter mTuesdayAdapter;
    private ShowVideoAdapter mWednesdayAdapter;
    private ShowVideoAdapter mThursdayAdapter;
    private ShowVideoAdapter mFridayAdapter;
    private ShowVideoAdapter mSaturdayAdapter;
    private ShowVideoAdapter mSundayAdapter;

    private OptionsPickerView<String> mPickerView;
    private ArrayList<String> mNums = new ArrayList<>();
    private boolean mIsOpen = false;
    private String mPrice;
    private int mDay = 1;

    private NetCallback mOpenCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                showToast(R.string.operation_ok);
                mDoctor.videoIsOpen="True";
                mDoctor.videoPrice = mPriceTv.getText().toString();
                //提交了保存
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mCloseCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {

                BaseManager.serviceRecordClose(mCloseServiceCallback);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mCloseServiceCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.colse_ok);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    private NetCallback mQueryCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<VideoTime>>() {
                }.getType();
                BaseArrayResp<VideoTime> res = new Gson().fromJson(result, objectType);
                mVideoTimes.clear();
                mVideoTimes.addAll(res.value);
                initWeek();

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {
        mIsOpen = mDoctor.videoIsOpen.equals("True");
        mPrice = mDoctor.videoPrice;
        mOpenDv.setSelected(mIsOpen);
        mPriceTv.setText(mPrice);

        mOpenDv.setOpenImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsOpen = !mIsOpen;
                mOpenDv.setSelected(mIsOpen);
                viewChange();
            }
        });

        for (int i = 1; i <= 5; i++) {
            mNums.add(i + "");
        }
        mPickerView = new OptionsPickerView<>(mContext);
        mPickerView.setPicker(mNums);
        mPickerView.setCyclic(false);
        mPickerView.setCancelable(true);

        mMondayAdapter = new ShowVideoAdapter(this, mMondayTimes, 0);
        mTuesdayAdapter = new ShowVideoAdapter(this, mTuesdayTimes, 0);
        mWednesdayAdapter = new ShowVideoAdapter(this, mWednesdayTimes, 0);
        mThursdayAdapter = new ShowVideoAdapter(this, mThursdayTimes, 0);
        mFridayAdapter = new ShowVideoAdapter(this, mFridayTimes, 0);
        mSaturdayAdapter = new ShowVideoAdapter(this, mSaturdayTimes, 0);
        mSundayAdapter = new ShowVideoAdapter(this, mSundayTimes, 0);

        mMondayLv.setAdapter(mMondayAdapter);
        mTuesdayLv.setAdapter(mTuesdayAdapter);
        mWednesdayLv.setAdapter(mWednesdayAdapter);
        mThursdayLv.setAdapter(mThursdayAdapter);
        mFridayLv.setAdapter(mFridayAdapter);
        mSaturdayLv.setAdapter(mSaturdayAdapter);
        mSundayLv.setAdapter(mSundayAdapter);

        //zhang

        mIsOpen = mDoctor.videoIsOpen.equals("True");
        mPrice = mDoctor.videoPrice;

        if (mIsOpen) {
            mPriceSv.setVisibility(View.VISIBLE);
            mPriceTv.setText(mPrice);
            mActionbar.setRightTvVisible(View.VISIBLE);

        } else {
            mPriceSv.setVisibility(View.GONE);
            mActionbar.setRightTvVisible(View.INVISIBLE);
        }


    }

    @Override
    protected void initListener() {
        mPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mCountEt.setText(mNums.get(options1));
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price = mPriceTv.getText().toString();
                if ((CommonUtils.isNumeric(price) || CommonUtils.isDecimal(price))&&Double.valueOf(price)!=0.0) {
                    showdialog();
                    BaseManager.vasServiceSet(ConstantValues.TYPE_VIDEO + "", price, mOpenCallback);
                } else {
                    showToast(R.string.enter_right_price);
                    return;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getVasAnnoucement(mQueryCallback);
    }

    @Event(R.id.count_et)
    private void showNum(View view) {
        mPickerView.show();
    }

    @Event({R.id.monday_tv, R.id.tuesday_tv, R.id.wednesday_tv, R.id.thursday_tv, R.id.friday_tv, R.id.saturday_tv, R.id.sunday_tv})
    private void onClick(View view) {

        switch (view.getId()) {
            case R.id.monday_tv:
                mDay = ConstantValues.MONDAY;
                activityStart(mMondayTimes);
                break;
            case R.id.tuesday_tv:
                mDay = ConstantValues.TUESDAY;
                activityStart(mTuesdayTimes);
                break;
            case R.id.wednesday_tv:
                mDay = ConstantValues.WEDNESDAY;
                activityStart(mWednesdayTimes);
                break;
            case R.id.thursday_tv:
                mDay = ConstantValues.THURSDAY;
                activityStart(mThursdayTimes);
                break;
            case R.id.friday_tv:
                mDay = ConstantValues.FRIDAY;
                activityStart(mFridayTimes);
                break;
            case R.id.saturday_tv:
                mDay = ConstantValues.SATURDAY;
                activityStart(mSaturdayTimes);
                break;
            case R.id.sunday_tv:
                mDay = ConstantValues.SUNDAY;
                activityStart(mSundayTimes);
                break;
        }
    }

    private void activityStart(ArrayList<VideoTime> timeSets) {
        Intent intent = new Intent(mContext, VideoTimeSettingActivity.class);
        intent.putExtra(ConstantValues.KEY_DAY, mDay);
        intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA, timeSets);
        startActivity(intent);
    }

    private void initWeek() {
        mMondayTimes.clear();
        mTuesdayTimes.clear();
        mSaturdayTimes.clear();
        mWednesdayTimes.clear();
        mSundayTimes.clear();
        mThursdayTimes.clear();
        mFridayTimes.clear();
        for (VideoTime videoTime : mVideoTimes) {
            switch (videoTime.week) {
                case ConstantValues.MONDAY+"":
                    mMondayTimes.add(videoTime);
                    break;
                case ConstantValues.TUESDAY+"":
                    mTuesdayTimes.add(videoTime);
                    break;
                case ConstantValues.WEDNESDAY+"":
                    mWednesdayTimes.add(videoTime);
                    break;
                case ConstantValues.THURSDAY+"":
                    mThursdayTimes.add(videoTime);
                    break;
                case ConstantValues.FRIDAY+"":
                    mFridayTimes.add(videoTime);
                    break;
                case ConstantValues.SATURDAY+"":
                    mSaturdayTimes.add(videoTime);
                    break;
                case ConstantValues.SUNDAY+"":
                    mSundayTimes.add(videoTime);
                    break;
            }
        }
        mMondayAdapter.setData(mMondayTimes);
        mSundayAdapter.setData(mSundayTimes);
        mSaturdayAdapter.setData(mSaturdayTimes);
        mFridayAdapter.setData(mFridayTimes);
        mThursdayAdapter.setData(mThursdayTimes);
        mWednesdayAdapter.setData(mWednesdayTimes);
        mTuesdayAdapter.setData(mTuesdayTimes);
    }

    private void viewChange() {

        if (mIsOpen) {
            mPriceSv.setVisibility(View.VISIBLE);
            mPriceTv.setText(mPrice);
            mActionbar.setRightTvVisible(View.VISIBLE);
            mDoctor.videoIsOpen = "True";

            showdialog();
            BaseManager.getVasAnnoucement(mQueryCallback);

        } else {
            mPriceSv.setVisibility(View.GONE);
            mActionbar.setRightTvVisible(View.INVISIBLE);
            mDoctor.videoIsOpen = "False";
            try {
                x.db().saveOrUpdate(mDoctor);
            } catch (DbException e) {
                e.printStackTrace();
            }
            showdialog();
            BaseManager.vasServiceClose(mDoctor.videoId, mCloseCallback);
        }
    }

}
