package com.sg.eyedoctor.helpUtils.doctorAdvice.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.netease.nim.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.common.ui.popupmenu.PopupMenuItem;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.impl.DlgEdit;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.GiveGrugWay;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.HospitalPatientDetail;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.LongAdviceCheck;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.LongAdviceDrug;
import com.sg.eyedoctor.helpUtils.doctorAdvice.constant.Constants;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新增医嘱
 */
@ContentView(R.layout.activity_add_doctor_advice)
public class AddDoctorAdviceActivity extends BaseActivity {

    @ViewInject(R.id.unit_name_rl)
    private LinearLayout mUnitNameRl;
    @ViewInject(R.id.eye_rl)
    private LinearLayout mEyeRl;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.once_name_rl)
    private LinearLayout mOnceNameRl;
    @ViewInject(R.id.frequency_name_rl)
    private LinearLayout mFrequencyNameRl;
    @ViewInject(R.id.first_day_name_rl)
    private LinearLayout mFirstDayNameRl;

    @ViewInject(R.id.usage_name_rl)
    private LinearLayout mUsageNameRl;
    @ViewInject(R.id.price_name_rl)
    private LinearLayout mPriceNameRl;
    @ViewInject(R.id.property_name_rl)
    private LinearLayout mPropertyNameRl;

    @ViewInject(R.id.stop_type_tv)
    private TextView mStopTypeTv;
    @ViewInject(R.id.pharmacyUnit_tv)
    private TextView mPharmacyUnitTv;
    @ViewInject(R.id.reson1_tv)
    private TextView mReson1Tv;
    @ViewInject(R.id.reson2_tv)
    private TextView mReson2Tv;
    @ViewInject(R.id.reson3_tv)
    private TextView mReson3Tv;
    @ViewInject(R.id.reson4_tv)
    private TextView mReson4Tv;
    @ViewInject(R.id.reson5_tv)
    private TextView mReson5Tv;
    @ViewInject(R.id.stop_reason_rl)
    private RelativeLayout mStopReasonRl;
    @ViewInject(R.id.time_tv)
    private TextView mTimeTv;
    @ViewInject(R.id.long_advice_tv)
    private TextView mLongAdviceTv;
    @ViewInject(R.id.unit_tv)
    private TextView mUnitTv;
    @ViewInject(R.id.unit_name_tv)
    private TextView mUnitNameTv;
    @ViewInject(R.id.once_tv)
    private TextView mOnceTv;
    @ViewInject(R.id.once_name_tv)
    private TextView mOnceNameTv;
    @ViewInject(R.id.count_tv)
    private TextView mCountTv;
    @ViewInject(R.id.frequency_tv)
    private TextView mFrequencyTv;
    @ViewInject(R.id.frequency_name_tv)
    private TextView mFrequencyNameTv;
    @ViewInject(R.id.first_day_tv)
    private TextView mFirstDayTv;
    @ViewInject(R.id.first_day_name_tv)
    private TextView mFirstDayNameTv;

    @ViewInject(R.id.usage_tv)
    private TextView mUsageTv;
    @ViewInject(R.id.eye_tv)
    private TextView mEyeTv;
    @ViewInject(R.id.usage_name_tv)
    private TextView mUsageNameTv;
    @ViewInject(R.id.price_tv)
    private TextView mPriceTv;
    @ViewInject(R.id.property_tv)
    private TextView mPropertyTv;
    @ViewInject(R.id.property_name_tv)
    private TextView mPropertyNameTv;
    @ViewInject(R.id.doctor_tv)
    private TextView mDoctorTv;
    @ViewInject(R.id.state_tv)
    private TextView mStateTv;

    private TimePickerView mDateView;
    private int mAdviceType = 0;
    private String mPostType = "1";  //项目类别 1 药品 2 诊疗


    private LongAdviceCheck mCheck;
    private LongAdviceDrug mDrug;

    private OptionsPickerView<String> mFrequencyPv;
    private OptionsPickerView<String> mEyePv;
    private OptionsPickerView<String> mCountPv;
    private OptionsPickerView<String> mGiveGrugWayPv;
    private ArrayList<String> options;
    private ArrayList<String> mNumOptions;
    private ArrayList<String> mEyes;

    private HospitalPatientDetail mDetail;

    private HospitalPatientDetail mDbDetail;

    private NIMPopupMenu popupMenu;
    private List<PopupMenuItem> menuItemList;
    private GiveGrugWay mGiveGrugWay;
    private int mCanEdit;  //0为新增   1为本地新增:不能修改类型    2完全不能修改

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                showToast("上传服务器成功");
                try {
                    x.db().deleteById(HospitalPatientDetail.class, mDbDetail.Id);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                AppManager.getAppManager().finishActivity();
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    @Override
    protected void initView() {

        mDbDetail = new HospitalPatientDetail();
        mDbDetail.hisHospitalized = getIntent().getStringExtra(ConstantValues.KEY_HIS);
        mCanEdit = getIntent().getIntExtra(ConstantValues.KEY_CAN_EDIT, 0);
        mDbDetail.state = "0";
        mDetail = (HospitalPatientDetail) getIntent().getSerializableExtra(ConstantValues.KEY_DATA);
        mDoctorTv.setText(mDoctor.userName);
        if (mDetail != null) {
            mDbDetail=mDetail;
            if(mDetail.adviceTime!=null){
                setTextView(mTimeTv, mDetail.adviceTime);
            }else{
                setTextView(mTimeTv, CommonUtils.getDetailTime(new Date(System.currentTimeMillis())));
            }

            setTextView(mLongAdviceTv, mDetail.adviceName);
            setTextView(mUnitTv, mDetail.measureUnit);
            if(mDetail.adviceDoctor!=null){
                setTextView(mDoctorTv, mDetail.adviceDoctor);
            }

            setTextView(mOnceTv, mDetail.dose);
            setTextView(mFrequencyTv, mDetail.frequency);
            setTextView(mPriceTv, mDetail.price + "");
            setTextView(mCountTv, mDetail.total + "");
            setTextView(mUsageTv, mDetail.giveDrugWay);
            setTextView(mEyeTv, mDetail.eyes);
            setTextView(mPharmacyUnitTv, mDetail.pharmacyUnit);
            setTextView(mFirstDayTv, mDetail.firstDayCount);
            mPostType=mDbDetail.type;

            //药品  1,2
            if (mDetail.type.equals("1") && mDetail.yzqx.equals("1")) {
                mAdviceType = 0;
            } else if (mDetail.type.equals("1") && mDetail.yzqx.equals("2")) {
                mAdviceType = 1;
            } else if (mDetail.type.equals("2") && mDetail.yzqx.equals("2")) {
                mAdviceType = 2;
            } else if (mDetail.type.equals("2") && mDetail.yzqx.equals("1")) {
                mAdviceType = 3;
            } else if (mDetail.type.equals("1") && mDetail.yzqx.equals("4")) {
                mAdviceType = 4;
            }
        }

        mStopReasonRl.getChildAt(mAdviceType).setSelected(true);

        //停诊年月日
        mDateView = new TimePickerView(this, TimePickerView.Type.ALL);
        mDateView.setTime(new Date());
        mDateView.setCyclic(false);
        mDateView.setCancelable(true);

        //频次选择
        mFrequencyPv = new OptionsPickerView<>(mContext);
        options = new ArrayList<>();
        String[] array = getResources().getStringArray(R.array.drug_pc);

        for (String s : array) {
            options.add(s);
        }

        mFrequencyPv.setPicker(options);
        mFrequencyPv.setCyclic(false);
        mFrequencyPv.setCancelable(true);

        //首日次数
        mCountPv = new OptionsPickerView<>(mContext);
        mNumOptions = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            mNumOptions.add(i + "次");
        }
        mCountPv.setPicker(mNumOptions);
        mCountPv.setCyclic(false);
        mCountPv.setCancelable(true);

        //给药路径
        mGiveGrugWayPv = new OptionsPickerView<>(mContext);
        ArrayList<String> giveWays = new ArrayList<>();
        for (GiveGrugWay giveGrugWay : Constants.sGiveGrugWays) {
            giveWays.add(giveGrugWay.wayName);
        }
        mGiveGrugWayPv.setPicker(giveWays);
        mGiveGrugWayPv.setCyclic(false);
        mGiveGrugWayPv.setCancelable(true);

        //眼别
        mEyePv = new OptionsPickerView<>(mContext);
        mEyes = new ArrayList<>();
        mEyes.add("左");
        mEyes.add("右");
        mEyes.add("双");
        mEyePv.setPicker(mEyes);
        mEyePv.setCyclic(false);
        mEyePv.setCancelable(true);

        if (mCanEdit==0) {

            //选择类型
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
                        mAdviceType = (int) v.getTag(R.id.tag_position);
                        mDbDetail.mAdviceType = mAdviceType;

                        init();
                    }
                });
            }

        }
        init();

    }



    @Override
    protected void initListener() {
        if (mCanEdit==2) {//完全不能修改
            return;
        }
        //时间选择后回调
        mDateView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String str = "yyyy-MM-dd";
                mTimeTv.setText(CommonUtils.getDetailTime(date));
                mDbDetail.adviceTime = CommonUtils.getDetailTime(date);

            }
        });
        mFrequencyPv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mFrequencyTv.setText(options.get(options1));
                mDbDetail.frequency = options.get(options1);
            }
        });
        mCountPv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mFirstDayTv.setText(mNumOptions.get(options1));

            }
        });
        mGiveGrugWayPv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mGiveGrugWay = Constants.sGiveGrugWays.get(options1);
                mUsageTv.setText(mGiveGrugWay.wayName);

                mDbDetail.giveDrugWay = mGiveGrugWay.wayName;
                mDbDetail.giveDrugWayId = mGiveGrugWay.wayId;
            }
        });
        mEyePv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                mDbDetail.eyes = mEyes.get(options1);
                mEyeTv.setText(mEyes.get(options1));
            }
        });
    }

    @Override
    protected void initActionbar() {
        if (mCanEdit==2) {
            mActionbar.setRightBtnVisible(View.GONE);
            return;
        }

        mActionbar.setRightBtnImg(R.drawable.ic_add_case, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopuptWindow(v, "", null);
            }
        });

    }

    @Event(R.id.eye_rl)
    private void chooseBothEye(View view) {
        if (mCanEdit==2) {
            return;
        }
        mEyePv.show();
    }

    @Event(R.id.time_tv)
    private void beginTime(View view) {
        if (mCanEdit==2) {
            return;
        }
        mDateView.show();
    }

    @Event(R.id.frequency_tv)
    private void frequencyChoose(View view) {
        if (mCanEdit==2) {
            return;
        }
        mFrequencyPv.show();
    }

    @Event(R.id.usage_name_rl)
    private void giveGrugWayPv(View view) {
        if (mCanEdit==2) {
            return;
        }
        mGiveGrugWayPv.show();
    }

    @Event(R.id.count_tv)
    private void count(View view) {
        if (mCanEdit==2) {
            return;
        }
        DialogManager.showEditDlg(AddDoctorAdviceActivity.this, R.string.enter_count, new DlgEdit() {
            @Override
            public boolean edit(String text) {
                mCountTv.setText(text);
                mDbDetail.total = text;
                return false;
            }
        }, InputType.TYPE_CLASS_NUMBER);
    }

    @Event(R.id.first_day_name_rl)
    private void firstDay(View view) {
        if (mCanEdit==2) {
            return;
        }
        DialogManager.showEditDlg(AddDoctorAdviceActivity.this, R.string.enter_count, new DlgEdit() {
            @Override
            public boolean edit(String text) {
                mFirstDayTv.setText(text);
                mDbDetail.firstDayCount = text;
                return false;
            }
        }, InputType.TYPE_CLASS_NUMBER);
    }

    @Event(R.id.long_advice_tv)
    private void longAdvice(View view) {
        if (mCanEdit==2) {
            return;
        }

        if (mAdviceType == 0 || mAdviceType == 1 || mAdviceType == 4) {//药品
            Intent intent = new Intent(mContext, LongAdviceDrugActivity.class);
            startActivityForResult(intent, 1);

        } else {//诊疗
            Intent intent = new Intent(mContext, LongAdviceCheckActivity.class);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) { //药品
            mDrug = (LongAdviceDrug) data.getSerializableExtra(ConstantValues.KEY_DATA);
            mLongAdviceTv.setText(mDrug.drugName);
            mOnceTv.setText(mDrug.pharmacyPackaging);
            mPropertyTv.setText("不发");
            mPriceTv.setText("￥" + mDrug.price);
            mUnitTv.setText(mDrug.drugStandard);
            mPharmacyUnitTv.setText(mDrug.pharmacyUnit);

            mDbDetail.adviceName = mDrug.drugName;
            mDbDetail.measureUnit = mDrug.drugStandard;
            mDbDetail.price = mDrug.price;
            mDbDetail.checkOrdrugId = mDrug.drugNumber;
            mDbDetail.pharmacyUnit = mDrug.pharmacyUnit;
            mDbDetail.dose = mDrug.pharmacyPackaging;
            mPostType= "1";

        } else if (requestCode == 2 && resultCode == RESULT_OK) {//诊疗
            mCheck = (LongAdviceCheck) data.getSerializableExtra(ConstantValues.KEY_DATA);
            mLongAdviceTv.setText(mCheck.itemName);
            mPriceTv.setText("￥" + mCheck.price);
            mOnceTv.setText(mCheck.dosageQuantity);
            mPharmacyUnitTv.setText(mCheck.dosageUnit);

            mDbDetail.adviceName = mCheck.itemName;
            mDbDetail.checkOrdrugId = mCheck.itemCode;
            mDbDetail.price = mCheck.price;
            mDbDetail.pharmacyUnit = mCheck.dosageUnit;
            mDbDetail.dose = mCheck.dosageQuantity;
            mPostType= "2";
        }
    }

    private void init() {
        int visible;

        switch (mAdviceType) {
            //药品
            case 0:
            case 1:
            case 4:
                visible = View.VISIBLE;

                mFrequencyNameRl.setVisibility(visible);
                mOnceNameRl.setVisibility(visible);

                //   mPropertyNameRl.setVisibility(visible);
                mUnitNameRl.setVisibility(visible);
                mFirstDayNameRl.setVisibility(visible);
                mUnitNameRl.setVisibility(visible);

                mDbDetail.type = 1 + "";

                break;

            //诊疗
            case 2:
            case 3:
                visible = View.GONE;

                mFrequencyNameRl.setVisibility(visible);
                mPropertyNameRl.setVisibility(visible);
                mUnitNameRl.setVisibility(visible);
                mFirstDayNameRl.setVisibility(visible);
                mUnitNameRl.setVisibility(visible);
                mUsageNameRl.setVisibility(visible);

                mDbDetail.type = 2 + "";
                break;

        }

        //医嘱权限   医嘱权限 1 长期   2临时  4出院带药
        switch (mAdviceType) {
            case 0:
            case 3:
                mDbDetail.yzqx = "1";
                break;
            case 1:
            case 2:
                mDbDetail.yzqx = "2";
                break;
            case 4:
                mDbDetail.yzqx = "4";
                break;
        }
    }

    private List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        moreMenuItems.add(new PopupMenuItem(context, 1, sessionId,
                sessionTypeEnum, getString(R.string.save_advice_to_local)));
        moreMenuItems.add(new PopupMenuItem(context, 2, sessionId,
                sessionTypeEnum, getString(R.string.save_advice_to_server)));

        return moreMenuItems;
    }

    private void initPopuptWindow(View view, String sessionId, SessionTypeEnum sessionTypeEnum) {

        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(mContext, menuItemList, listener);
        }
        menuItemList.clear();
        menuItemList.addAll(getMoreMenuItems(mContext, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private NIMPopupMenu.MenuItemClickListener listener = new NIMPopupMenu.MenuItemClickListener() {
        @Override
        public void onItemClick(final PopupMenuItem item) {
            if (mTimeTv.getText().toString().equals("请选择")) {
                showToast("开始时间未填写");
                return;
            }
            if (mLongAdviceTv.getText().toString().equals("请选择")) {
                showToast("长期医嘱未填写");
                return;
            }
            if (mEyeTv.getText().toString().equals("请选择")) {
//                showToast("眼别未选择");
//                return;
                mDbDetail.eyes="";
            }

            switch (mAdviceType) {
                //药品
                case 0:
                case 1:
                case 4:

                    if (mFrequencyTv.getText().toString().equals("请选择")) {
                        showToast("频次未选择");
                        return;
                    }
                    if (mFirstDayTv.getText().toString().equals("请填写次数")) {
                        showToast("首日次数未填写");
                        return;
                    }

                    if (mUsageTv.getText().toString().equals("请选择")) {
                        showToast("用法未选择");
                        return;
                    }
                    break;


            }
            if (mCountTv.getText().toString().equals("请填写次数")) {
                showToast("总量未填写");
                return;
            }


            if (!mDbDetail.type.equals(mPostType)) {
                DialogManager.showConfimDlg(mContext, R.string.error_type, new DlgClick() {
                    @Override
                    public boolean click() {

                        return false;
                    }
                });

                return;
            }

            mDbDetail.adviceDoctor=mDoctorTv.getText().toString();
            switch (item.getTag()) {
                case 1:
                    try {
                        x.db().saveOrUpdate(mDbDetail);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    showToast("保存到本地成功");
                    AppManager.getAppManager().finishActivity();
                    break;
                case 2:
                    showdialog();
                    BaseManager.addAdvice(mDbDetail.hisHospitalized, mPostType + "", mDbDetail.checkOrdrugId, mDbDetail.total, mDbDetail.yzqx + "", mDbDetail.frequency, mDbDetail.giveDrugWayId + "", mDbDetail.eyes, mCallback);
                    break;

            }
        }
    };



}
