package com.sg.eyedoctor.consult.phoneConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScroolGridView;
import com.sg.eyedoctor.consult.phoneConsult.adapter.PhoneConsultTimeAdapter;
import com.sg.eyedoctor.consult.phoneConsult.bean.PhoneConsultTime;
import com.sg.eyedoctor.consult.phoneConsult.bean.TimeSet;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 电话咨询设置
 */
@ContentView(R.layout.activity_phone_consult_setting)
public class PhoneConsultSettingActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.price_tv)
    private TextView mPriceTv;
    @ViewInject(R.id.monday_gv)
    private NoScroolGridView mMondayGv;
    @ViewInject(R.id.tuesday_gv)
    private NoScroolGridView mTuesdayGv;
    @ViewInject(R.id.wednesday_gv)
    private NoScroolGridView mWednesdayGv;
    @ViewInject(R.id.thursday_gv)
    private NoScroolGridView mThursdayGv;
    @ViewInject(R.id.friday_gv)
    private NoScroolGridView mFridayGv;
    @ViewInject(R.id.saturday_gv)
    private NoScroolGridView mSaturdayGv;
    @ViewInject(R.id.sunday_gv)
    private NoScroolGridView mSundayGv;

    private int mDay = 1;
    private ArrayList<PhoneConsultTime> mMondayTimes = new ArrayList<>();
    private ArrayList<PhoneConsultTime> mTuesdayTimes = new ArrayList<>();
    private ArrayList<PhoneConsultTime> mWednesdayTimes = new ArrayList<>();
    private ArrayList<PhoneConsultTime> mThursdayTimes = new ArrayList<>();
    private ArrayList<PhoneConsultTime> mFridayTimes = new ArrayList<>();
    private ArrayList<PhoneConsultTime> mSaturdayTimes = new ArrayList<>();
    private ArrayList<PhoneConsultTime> mSundayTimes = new ArrayList<>();

    private ArrayList<TimeSet> mMondaySets = new ArrayList<>();
    private ArrayList<TimeSet> mTuesdaySets = new ArrayList<>();
    private ArrayList<TimeSet> mWednesdaySets = new ArrayList<>();
    private ArrayList<TimeSet> mThursdaySets = new ArrayList<>();
    private ArrayList<TimeSet> mFridaySets = new ArrayList<>();
    private ArrayList<TimeSet> mSaturdaySets = new ArrayList<>();
    private ArrayList<TimeSet> mSundaySets = new ArrayList<>();

    private PhoneConsultTimeAdapter mMondayAdapter;
    private PhoneConsultTimeAdapter mTuesdayAdapter;
    private PhoneConsultTimeAdapter mWednesdayAdapter;
    private PhoneConsultTimeAdapter mThursdayAdapter;
    private PhoneConsultTimeAdapter mFridayAdapter;
    private PhoneConsultTimeAdapter mSaturdayAdapter;
    private PhoneConsultTimeAdapter mSundayAdapter;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PhoneConsultTime>>() {
                }.getType();
                BaseArrayResp<PhoneConsultTime> res = new Gson().fromJson(result, objectType);
                sortWeek(res.value);

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    private void sortWeek(ArrayList<PhoneConsultTime> value) {
        mMondayTimes.clear();
        mTuesdayTimes.clear();
        mWednesdayTimes.clear();
        mThursdayTimes.clear();
        mFridayTimes.clear();
        mSaturdayTimes.clear();
        mSundayTimes.clear();

        mMondaySets.clear();
        mTuesdaySets.clear();
        mWednesdaySets.clear();
        mThursdaySets.clear();
        mFridaySets.clear();
        mSaturdaySets.clear();
        mSundaySets.clear();
        for (PhoneConsultTime phoneConsultTime : value) {
            phoneConsultTime.isFirst=true;
            phoneConsultTime.count=1;
            switch (phoneConsultTime.week) {
                case 1:
                    mMondayTimes.add(phoneConsultTime);
             //       mMondaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));
                    break;
                case 2:
                    mTuesdayTimes.add(phoneConsultTime);
               //     mTuesdaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));

                    break;
                case 3:
                    mWednesdayTimes.add(phoneConsultTime);
               //     mWednesdaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));

                    break;
                case 4:
                    mThursdayTimes.add(phoneConsultTime);
               //     mThursdaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));

                    break;
                case 5:
                    mFridayTimes.add(phoneConsultTime);
              //      mFridaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));
                    break;
                case 6:
                    mSaturdayTimes.add(phoneConsultTime);
               //     mSaturdaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));

                    break;
                case 7:
                    mSundayTimes.add(phoneConsultTime);
              //      mSundaySets.add(new TimeSet(CommonUtils.strToDate(phoneConsultTime.dutyTime), 1, true));
                    break;
            }
        }


//        for (TimeSet mondaySet : mMondaySets) {
//            if (!mondaySet.isFirst) {
//                mMondaySets.remove(mondaySet);
//            }
//        }

        addTimes(mMondayTimes,mMondaySets);
        addTimes(mTuesdayTimes,mTuesdaySets);
        addTimes(mWednesdayTimes,mWednesdaySets);
        addTimes(mThursdayTimes,mThursdaySets);
        addTimes(mFridayTimes,mFridaySets);
        addTimes(mSaturdayTimes,mSaturdaySets);
        addTimes(mSundayTimes,mSundaySets);

        mMondayAdapter.setData(mMondayTimes);
        mTuesdayAdapter.setData(mTuesdayTimes);
        mWednesdayAdapter.setData(mWednesdayTimes);
        mThursdayAdapter.setData(mThursdayTimes);
        mFridayAdapter.setData(mFridayTimes);
        mSaturdayAdapter.setData(mSaturdayTimes);
        mSundayAdapter.setData(mSundayTimes);
    }

    private void addTimes(ArrayList<PhoneConsultTime> times,ArrayList<TimeSet> timeSets) {


        for (int i = times.size()-1; i >0; i--) {

            PhoneConsultTime currSet = times.get(i);
            PhoneConsultTime preSet = times.get(i - 1);
            if (CommonUtils.strToDate(currSet.dutyTime) - CommonUtils.strToDate(preSet.dutyTime)== 30 * 60 * 1000) {
                preSet.isFirst=true;
                currSet.isFirst=false;
                preSet.count=currSet.count+preSet.count;

            } else {
                if(i==1){
                    preSet.isFirst=true;
                }else{
                    preSet.isFirst=false;
                }
                currSet.isFirst=true;
            }
        }

        for (int i = 0; i < times.size(); i++) {
            PhoneConsultTime time=times.get(i);
            if(time.isFirst){
                long longtime=CommonUtils.strToDate(time.dutyTime);
                timeSets.add(new TimeSet(longtime,time.count));
            }
        }

        LogUtils.i(timeSets.toString());
        times.add(new PhoneConsultTime(1));
        if (times.size() % 4 != 0) {
            int j = 4 - times.size() % 4;
            for (int i = 0; i < j; i++) {
                times.add(new PhoneConsultTime(2));
            }
        }
    }

    @Override
    protected void initView() {

        mMondayAdapter = new PhoneConsultTimeAdapter(this, mMondayTimes, 4);
        mTuesdayAdapter = new PhoneConsultTimeAdapter(this, mTuesdayTimes, 4);
        mWednesdayAdapter = new PhoneConsultTimeAdapter(this, mWednesdayTimes, 4);
        mThursdayAdapter = new PhoneConsultTimeAdapter(this, mThursdayTimes, 4);
        mFridayAdapter = new PhoneConsultTimeAdapter(this, mFridayTimes, 4);
        mSaturdayAdapter = new PhoneConsultTimeAdapter(this, mSaturdayTimes, 4);
        mSundayAdapter = new PhoneConsultTimeAdapter(this, mSundayTimes, 4);

        mMondayGv.setAdapter(mMondayAdapter);
        mTuesdayGv.setAdapter(mTuesdayAdapter);
        mWednesdayGv.setAdapter(mWednesdayAdapter);
        mThursdayGv.setAdapter(mThursdayAdapter);
        mFridayGv.setAdapter(mFridayAdapter);
        mSaturdayGv.setAdapter(mSaturdayAdapter);
        mSundayGv.setAdapter(mSundayAdapter);


    }

    @Override
    protected void initListener() {
        mMondayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 1;

                activityStart(mMondayTimes,mMondaySets, position);

            }
        });
        mTuesdayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 2;

                activityStart(mTuesdayTimes,mTuesdaySets, position);

            }
        });
        mWednesdayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 3;

                activityStart(mWednesdayTimes,mWednesdaySets, position);

            }
        });
        mThursdayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 4;

                activityStart(mThursdayTimes,mThursdaySets, position);

            }
        });
        mFridayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 5;

                activityStart(mFridayTimes,mFridaySets, position);

            }
        });
        mSaturdayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 6;

                activityStart(mSaturdayTimes, mSaturdaySets,position);

            }
        });
        mSundayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDay = 7;

                activityStart(mSundayTimes,mSundaySets, position);

            }
        });

    }

    private void activityStart(ArrayList<PhoneConsultTime> times, ArrayList<TimeSet> timeSets,int position) {
        if (times.get(position).type == 1) {
            Intent intent = new Intent(mContext, PhoneConsultTimeActivity.class);
            intent.putExtra(ConstantValues.KEY_DAY, mDay);
            intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA,timeSets);
            startActivity(intent);
        }
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextView(mPriceTv,mDoctor.phonePrice);
        showdialog();
        BaseManager.getVasDutyRoster(mCallback);

    }
}
