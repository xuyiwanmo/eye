package com.sg.eyedoctor.commUtils.outPatientRecord.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.outPatientRecord.adapter.MyPatientDateAdapter;
import com.sg.eyedoctor.commUtils.outPatientRecord.bean.AppointPatient;
import com.sg.eyedoctor.commUtils.outPatientRecord.fragment.OutPatientFragment;
import com.sg.eyedoctor.commUtils.outPatientRecord.request.AppointPatientParams;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.main.adapter.TabPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 首页->门诊记录
 */

//查询形式  2016-01-13
@ContentView(R.layout.activity_out_patient_record)
public class OutPatientRecordActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener,
        ViewPager.OnPageChangeListener {

    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;
    @ViewInject(R.id.gv_my_patient_date)
    private GridView mDateGv;
    @ViewInject(R.id.tv_my_patient_choose)
    private TextView mChooseTv;
    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;
    @ViewInject(R.id.imgBack)
    private ImageView mBackImg;

    private TabPagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;

    private AppointPatientParams mAppointParams;

    private MyPatientDateAdapter mDateAdapter;
    private ArrayList<AppointPatient> mPatients;
    private ArrayList<Date> mDates;

    private int mStatus = 1;
    private OutPatientFragment mAppointmentFragment;
    private OutPatientFragment mOutPatientFragment;
    private ArrayList<Fragment> mFragments;
    private Date mCurDate;
    private TimePickerView mTimePickerView;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<AppointPatient>>(){}.getType();
                BaseArrayResp<AppointPatient> res=new Gson().fromJson(result, objectType);

                mPatients = res.value;

            } else {
                showToast(R.string.query_empty);
                mPatients = new ArrayList<>();
            }
            if (mPatients == null) {
                return;
            }
            if (mStatus == 1) {
                mAppointmentFragment.setData(mPatients);
            }
            if (mStatus == 2) {
                mOutPatientFragment.setData(mPatients);
            }
        }

        @Override
        protected void requestError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Event(R.id.tv_my_patient_choose)
    private void chooseDdate(View view) {

        mTimePickerView.show();
    }


    //******************************viewpager start***********************************

    @Override
    public void onIndicatorSelected(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTopIndicator.setTabsDisplay(OutPatientRecordActivity.this, position);

        if (mStatus != position + 1) {  //位置不一样才弹出
            mStatus = position + 1;
            queryData();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //*****************************************end************************************

    private void queryData() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        mAppointParams = new AppointPatientParams();
        mAppointParams.type = mStatus + "";
        mAppointParams.docCode = "1001";
        mAppointParams.queryDate = format.format(mCurDate);//"2016-01-13"
        mAppointParams.queryTpye = "1";
        showdialog();
        BaseManager.queryAppointPatient(mAppointParams, mCallback);
    }

    private void chageDate(Date date) {

        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例

        ca.setTime(date);
        ca.add(Calendar.DAY_OF_MONTH, -2); //减2
        initDate(ca);
        ca.add(Calendar.DAY_OF_MONTH, -4);
        mCurDate = ca.getTime();
        queryData();

    }

    private void initDate(Calendar ca) {
        mDates = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ca.add(Calendar.DAY_OF_MONTH, 1);
            mDates.add(ca.getTime());
        }
        mDateAdapter.setData(mDates);
        mDateAdapter.setSelectPosition(1);
    }


    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mAppointmentFragment = new OutPatientFragment(false);
        mOutPatientFragment = new OutPatientFragment(true);
        mFragments = new ArrayList<>();
        mFragments.add(mAppointmentFragment);
        mFragments.add(mOutPatientFragment);

        mPagerAdapter = new TabPagerAdapter(mFragmentManager, mViewPager, mFragments, this);
        mTopIndicator.setLabels(getResources().getStringArray(R.array.patient_record_array));
        mTopIndicator.setOnTopIndicatorListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();

        mDates = new ArrayList<>();
        mDateAdapter = new MyPatientDateAdapter(mContext, mDates,0);
        mDateGv.setAdapter(mDateAdapter);

        mDateGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDateAdapter.setSelectPosition(position);
                mCurDate = mDates.get(position);
                queryData();
            }
        });

        Calendar ca = Calendar.getInstance();
        Date date = new Date();
        ca.setTime(date);
        ca.add(Calendar.DAY_OF_MONTH, -2); //减1
        initDate(ca);
        ca.add(Calendar.DAY_OF_MONTH, -4); //减1
        mCurDate = ca.getTime();

        //时间选择器
        Date a = new Date();
        a.setHours(0);
        a.setMinutes(0);
        a.setSeconds(0);
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY,a,false,true);

        queryData();
    }

    @Override
    protected void initListener() {
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
               chageDate(date);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

}
