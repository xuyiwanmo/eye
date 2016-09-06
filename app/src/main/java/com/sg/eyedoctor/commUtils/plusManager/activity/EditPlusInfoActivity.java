package com.sg.eyedoctor.commUtils.plusManager.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.plusManager.bean.XtrAppointment;
import com.sg.eyedoctor.commUtils.plusManager.bean.XtrHostpital;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 请填写加号信息
 */
@ContentView(R.layout.activity_edit_plus_info)
public class EditPlusInfoActivity extends BaseActivity {
    public static final int TYPE_FROM_TIME = 0;
    public static final int TYPE_TO_TIME = 1;

    public static final int NORMAL = 1;
    public static final int PROFESSOR = 2;
    public static final int SPECIAL_PROCUREMENT = 3;
    public static final int COLLEGE = 4;
    public static final int INTERNATION = 5;

    public int mChooseType = TYPE_FROM_TIME;

    private int mCurrent = 0;
    private int mOutpatientType = NORMAL;

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.type1_tv)
    private TextView mType1Tv;
    @ViewInject(R.id.type2_tv)
    private TextView mType2Tv;
    @ViewInject(R.id.type3_tv)
    private TextView mType3Tv;
    @ViewInject(R.id.type4_tv)
    private TextView mType4Tv;
    @ViewInject(R.id.type5_tv)
    private TextView mType5Tv;
    @ViewInject(R.id.out_patient_hos_et)
    private TextView mOutPatientHosEt;
    @ViewInject(R.id.from_time_tv)
    private TextView mFromTimeTv;
    @ViewInject(R.id.to_time_tv)
    private TextView mToTimeTv;
    @ViewInject(R.id.add_count_et)
    private EditText mAddCountEt;
    @ViewInject(R.id.mark_et)
    private EditText mMarkEt;
    @ViewInject(R.id.ensure_tv)
    private TextView mTvLogin;
    @ViewInject(R.id.type_rl)
    private RelativeLayout mTypeRl;

    private TimePickerView mTimePickerView;
    private OptionsPickerView<String> mHosPv;
    private Date mFromTime = null;
    private Date mToTime = null;
    private XtrAppointment mAppointment;
    private ArrayList<XtrHostpital> mHostpitals=new ArrayList<>();
    private ArrayList<String> mHostpitalNames=new ArrayList<>();
    private String mHosId;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                showToast(R.string.operation_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mHosCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                Type objectType = new TypeToken<BaseArrayResp<XtrHostpital>>(){}.getType();
                BaseArrayResp<XtrHostpital> res=new Gson().fromJson(result, objectType);
                mHostpitals=res.value;
                for (XtrHostpital hostpital : mHostpitals) {
                    mHostpitalNames.add(hostpital.hosName);
                }
                mHosPv.setPicker(mHostpitalNames);
                showToast(R.string.get_hospital_ok);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {

        mCurrent = getIntent().getIntExtra(ConstantValues.KEY_POSITON, 0);
        mAppointment = (XtrAppointment)getIntent().getSerializableExtra(ConstantValues.KEY_UPDATE);
        if(mAppointment!=null){//修改
            setTextView(mFromTimeTv,mAppointment.beginSpan);
            setTextView(mToTimeTv,mAppointment.endSpan);
            setTextView(mOutPatientHosEt,mAppointment.xtrHospital);
            setTextView(mAddCountEt,mAppointment.xtrAccount);
            setTextView(mMarkEt, mAppointment.remark);

            TextView textView = (TextView) mTypeRl.getChildAt(mAppointment.xtrType-1);
            textView.setSelected(true);
            textView.setTextColor(getcolor(R.color.white));
            mHosId=mAppointment.hospitalId;
            mOutpatientType=mAppointment.xtrType;
        }else{
            TextView textView = (TextView) mTypeRl.getChildAt(0);
            textView.setSelected(true);
            textView.setTextColor(getcolor(R.color.white));
        }
        //时间选择器
        if(mCurrent%2==0){
            mTimePickerView = new TimePickerView(this, TimePickerView.Type.MORNING_HOURS_MINS);
        }else{
            mTimePickerView = new TimePickerView(this, TimePickerView.Type.AFTERNOON_HOURS_MINS);
        }

     //   mTimePickerView.setTime(new Date());
        mTimePickerView.setCyclic(false);
        mTimePickerView.setCancelable(true);


    }

    @Override
    protected void initListener() {
        for (int i = 0; i < mTypeRl.getChildCount(); i++) {
            final TextView textView = (TextView) mTypeRl.getChildAt(i);

            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < mTypeRl.getChildCount(); j++) {
                        TextView textView = (TextView) mTypeRl.getChildAt(j);
                        textView.setTextColor(getcolor(R.color.black));
                        textView.setSelected(false);
                    }

                    TextView text = (TextView) v;
                    text.setSelected(true);
                    text.setTextColor(getcolor(R.color.white));
                    mOutpatientType=(int)v.getTag()+1;

                }
            });
        }

        //时间选择后回调
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {

                if (mChooseType == TYPE_FROM_TIME) {
                    mFromTimeTv.setTextColor(getcolor(R.color.black));
                    mFromTimeTv.setText(CommonUtils.getTime(date));
                    mFromTime = date;
                } else {
                    mToTimeTv.setTextColor(getcolor(R.color.black));
                    mToTimeTv.setText(CommonUtils.getTime(date));
                    mToTime = date;
                }
            }
        });
    }

    @Override
    protected void initActionbar() {
    }

    @Event(R.id.from_time_tv)
    private void chooseFromTime(View view) {
        mChooseType = TYPE_FROM_TIME;
        KeyBoardUtils.hideKeyboard(mAddCountEt);
        mTimePickerView.show();

    }

    @Event(R.id.to_time_tv)
    private void chooseToTime(View view) {
        mChooseType = TYPE_TO_TIME;
        KeyBoardUtils.hideKeyboard(mAddCountEt);
        mTimePickerView.show();
    }
    @Event(R.id.out_patient_hos_et)
    private void chooseHos(View view) {
        mHosPv=new OptionsPickerView<>(mContext);
        mHosPv.setPicker(mHostpitalNames);
        mHosPv.setCyclic(false);
        mHosPv.setCancelable(true);
        mHosPv.show();
        mHosPv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                XtrHostpital hostpital=mHostpitals.get(options1);
                mOutPatientHosEt.setText(hostpital.hosName);
                mHosId=hostpital.hospitalId;
            }
        });
    }

    @Event({R.id.ensure_tv})
    private void save(View view) {
        if (TextUtils.isEmpty(mOutPatientHosEt.getText())) {
            showToast(R.string.out_patient_hospital_empty);
            return;
        }
        if (mFromTimeTv.getText().equals("请选择")||mToTimeTv.getText().equals("请选择")) {
            showToast(R.string.out_patient_time_empty);
            return;
        }

        if (compareTime()) {
            showToast(R.string.error_choose_time);
            return;
        }
        if (TextUtils.isEmpty(mAddCountEt.getText())) {
            showToast(R.string.out_patient_count_empty);
            return;
        }

        if(!TextUtils.isDigitsOnly(mAddCountEt.getText())){
            showToast(R.string.out_patient_count_not_number);
            mAddCountEt.setText("");
            return;

        }
        if (mAppointment==null){
            BaseManager.xtrAppointmentAdd(mCurrent, getText(mFromTimeTv), getText(mToTimeTv), getText(mAddCountEt), getText(mOutPatientHosEt),mHosId+"", mOutpatientType + "", mDoctor.addPrice, getText(mMarkEt), mCallback);
        } else {
            BaseManager.xtrAppointmentUpdate(mAppointment.id, getText(mFromTimeTv), getText(mToTimeTv), getText(mAddCountEt), getText(mOutPatientHosEt),mHosId+"",mOutpatientType + "",mDoctor.addPrice, getText(mMarkEt), mCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getHospitalByDoc(mHosCallback);
    }

    public static Date toTime(String str){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }


    private boolean compareTime() {
        Date begin=toTime(mFromTimeTv.getText().toString());
        Date end=toTime(mToTimeTv.getText().toString());
        if(begin.getTime()>end.getTime()){
            return true;
        }

        return false;
    }



}

