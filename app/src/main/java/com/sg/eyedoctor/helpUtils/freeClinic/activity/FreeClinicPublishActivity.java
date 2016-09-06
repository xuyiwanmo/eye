package com.sg.eyedoctor.helpUtils.freeClinic.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.consult.openConsult.activity.OpenViedoActivity;
import com.sg.eyedoctor.consult.openConsult.bean.ConsultBean;
import com.sg.eyedoctor.helpUtils.freeClinic.adapter.FreeClinicTimeAdapter;
import com.sg.eyedoctor.helpUtils.freeClinic.bean.GetFreeClinicList;
import com.sg.eyedoctor.helpUtils.freeClinic.bean.PostClinicList;
import com.sg.eyedoctor.settings.myOnlineManager.activity.SetVideoConsultActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ContentView(R.layout.activity_free_clinic_publish)
public class FreeClinicPublishActivity extends BaseActivity implements FreeClinicTimeAdapter.SetTimeCallback {

    @ViewInject(R.id.stop_type_tv)
    private TextView mStopTypeTv;
    @ViewInject(R.id.set_sv)
    private ScrollView mSetSv;
    @ViewInject(R.id.reson1_tv)
    private TextView mReson1Tv;
    @ViewInject(R.id.reson2_tv)
    private TextView mReson2Tv;
    @ViewInject(R.id.reson3_tv)
    private TextView mReson3Tv;
    @ViewInject(R.id.stop_reason_rl)
    private RelativeLayout mStopReasonRl;
    @ViewInject(R.id.set_video_tv)
    private TextView mSetVideoTv;
    @ViewInject(R.id.free_video_ll)
    private LinearLayout mFreeVideoLl;
    @ViewInject(R.id.time_lv)
    private NoScrollListView mTimeLv;
    @ViewInject(R.id.add_ll)
    private LinearLayout mAddLl;
    @ViewInject(R.id.publish_tv)
    private TextView mPublishTv;
    private ArrayList<PostClinicList> mDateSets = new ArrayList<>();
    private TimePickerView mTimePv;
    private OptionsPickerView<Integer> mNumPickerView;
    private FreeClinicTimeAdapter mAdapter;
    private int mOutpatientType;//0 图文
    private int mChoosePosition = 0;
    private Date mCurrentDate;
    private Date mEndDate;

    private GetFreeClinicList mFreeClinicList;
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                AppManager.getAppManager().finishActivity();
            }else{
                showToast(CommonUtils.getMsg(result));
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    private NetCallback mFreeCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                if (CommonUtils.isValueZero(result)) {//如果为0  可以开启

                    DialogManager.showConfimCancelDlg(mContext, "是否确定现在开启视频咨询？", new DlgClick() {
                        @Override
                        public boolean click() {
                            Intent intent = new Intent(mContext, FreeVideoActivity.class);
                            startActivity(intent);

                            return false;
                        }
                    }, new DlgClick() {
                        @Override
                        public boolean click() {
                            return false;
                        }
                    });

                } else {
                    DialogManager.showConfimDlg(mContext, "目前视频已开诊，无法设置义诊！", new DlgClick() {
                        @Override
                        public boolean click() {
                            return false;
                        }
                    });

                }

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };


    @Override
    protected void initView() {
        mFreeClinicList=getIntent().getParcelableExtra(ConstantValues.KEY_DATA);
        mOutpatientType = 0;
        if(mFreeClinicList!=null){
            PostClinicList postClinicList=new PostClinicList(mFreeClinicList.openDate.split(" ")[0],mFreeClinicList.amount,mFreeClinicList.type);
            mDateSets.add(postClinicList);
            mOutpatientType=mFreeClinicList.type-1;

            //不能修改
            mAddLl.setVisibility(View.GONE);
            mAdapter = new FreeClinicTimeAdapter(mContext, mDateSets, 0,false);
        }else{
            mAdapter = new FreeClinicTimeAdapter(mContext, mDateSets, 0,true);
            for (int i = 0; i < mStopReasonRl.getChildCount(); i++) {
                final TextView textView = (TextView) mStopReasonRl.getChildAt(i);

                textView.setTag(R.id.tag_position, i);
                textView.setTag(R.id.tag_value, false);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int j = 0; j < mStopReasonRl.getChildCount(); j++) {
                            TextView textView = (TextView) mStopReasonRl.getChildAt(j);
                            textView.setSelected(false);
                            textView.setTag(R.id.tag_value, false);
                        }

                        v.setSelected(true);
                        v.setTag(R.id.tag_value, true);
                        mOutpatientType = (int) v.getTag(R.id.tag_position);
                        viewInit();
                    }
                });
            }

        }
        initDate();

        mAdapter.setCallback(this);
        mTimeLv.setAdapter(mAdapter);
      //  UiUtils.setEmptyText(mContext, mTimeLv, getString(R.string.empty));

        mStopReasonRl.getChildAt(mOutpatientType).setSelected(true);

        //停诊年月日
        mTimePv = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        mTimePv.setTime(new Date());
        mTimePv.setCyclic(false);
        mTimePv.setCancelable(true);

        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            integers.add(i);
        }

        mNumPickerView = new OptionsPickerView<>(mContext);
        mNumPickerView.setPicker(integers);
        mNumPickerView.setCyclic(false);
        mNumPickerView.setCancelable(true);

        viewInit();
    }


    @Override
    protected void initListener() {
        mNumPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                PostClinicList date = mDateSets.get(mChoosePosition);
                date.amount = options1 + 1;
                mAdapter.setData(mDateSets);
            }
        });
        mTimePv.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() > mEndDate.getTime()) {
                    DialogManager.showConfimDlg(mContext, R.string.error_range_time, new DlgClick() {
                        @Override
                        public boolean click() {

                            return false;
                        }
                    });
                    return;
                }
                for (PostClinicList dateSet : mDateSets) {
                    if (CommonUtils.getDateTime(date).equals(dateSet.openDate)) {
                        DialogManager.showConfimDlg(mContext, R.string.error_time_twice, new DlgClick() {
                            @Override
                            public boolean click() {
                                return false;
                            }
                        });
                        return;
                    }
                }
                PostClinicList set = mDateSets.get(mChoosePosition);
                set.openDate = CommonUtils.getSpritTime(date);
                mAdapter.setData(mDateSets);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }


    //adapter回调接口

    /**
     * 点击数量填写
     */
    @Override
    public void request(int position) {
        mChoosePosition = position;
        mNumPickerView.show();
    }

    @Override
    public void clickTime(int position) {
        mChoosePosition = position;
        mTimePv.show();
    }

    @Override
    public void setCount(int position, String num) {

    }

    @Override
    public void decrease(int postion) {

        if(mFreeClinicList!=null){

        }
        mDateSets.remove(postion);
        mAdapter.setData(mDateSets);
    }

    /**
     * 添加时间段
     */
    @Event(R.id.add_ll)
    private void add(View view) {
        mDateSets.add(new PostClinicList("请选择", 1, mOutpatientType + 1));
        mAdapter.setData(mDateSets);
    }

    /**
     * 按钮 视频   现在设置
     */
    @Event(R.id.set_video_tv)
    private void setVideo(View view) {

        boolean isOpenVideo=mDoctor.videoIsOpen.equals("True");
        Intent intent = new Intent(mContext, OpenViedoActivity.class);
        if (!isOpenVideo) {//未开通  跳转到开通提示界面
            ConsultBean mConsultBean =new ConsultBean(R.drawable.home_video_consult,R.string.video_consult, SetVideoConsultActivity.class);
            intent.putExtra(ConstantValues.KEY_DATA, mConsultBean);
            startActivity(intent);
            return;
        }
        showdialog();
        BaseManager.getServiceRecord(mFreeCallback);
    }

    /**
     * 发布按钮
     */
    @Event(R.id.publish_tv)
    private void publish(View view) {
        if(mDateSets.size()==0){
            showToast("发布日期不能为空!");
            return;
        }

        showdialog();
        //设置所有的类型
        for (PostClinicList dateSet : mDateSets) {
            dateSet.type = mOutpatientType + 1;
        }

        BaseManager.freeClinicAdd(mDateSets, mCallback);

    }

    private void initDate() {
        mCurrentDate = new Date(System.currentTimeMillis());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mCurrentDate);
        calendar.add(calendar.DATE, 14);//把日期往后增加14天
        mEndDate = calendar.getTime(); //最迟可以预定的时间
    }


    private void viewInit() {
        if (mOutpatientType == 2) {//视频
            mFreeVideoLl.setVisibility(View.VISIBLE);
            mSetSv.setVisibility(View.GONE);
        } else {
            mFreeVideoLl.setVisibility(View.GONE);
            mSetSv.setVisibility(View.VISIBLE);
        }
    }

}
