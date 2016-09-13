package com.sg.eyedoctor.main.fragment;


import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.academic.activity.AcademicActivity;
import com.sg.eyedoctor.commUtils.addPatient.activity.AddPatientActivity;
import com.sg.eyedoctor.commUtils.caseDiscuss.activity.CaseDiscussActivity;
import com.sg.eyedoctor.commUtils.internetConsult.activity.InternetConsultationActivity;
import com.sg.eyedoctor.commUtils.outPatientRecord.activity.OutPatientRecordActivity;
import com.sg.eyedoctor.commUtils.patientReport.activity.PatientReportActivity;
import com.sg.eyedoctor.commUtils.plusManager.activity.PlusManagerActivity;
import com.sg.eyedoctor.commUtils.toolbox.activity.ToolBoxActivity;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.common.view.banner.CircleFlowIndicator;
import com.sg.eyedoctor.common.view.banner.ViewFlow;
import com.sg.eyedoctor.consult.openConsult.activity.OpenViedoActivity;
import com.sg.eyedoctor.consult.openConsult.bean.ConsultBean;
import com.sg.eyedoctor.consult.phoneConsult.activity.PhoneConsultActivity;
import com.sg.eyedoctor.consult.textConsult.activity.TextConsultActivity;
import com.sg.eyedoctor.consult.videoConsult.activity.VideoConsultActivity;
import com.sg.eyedoctor.consult.videoConsult.activity.VideoSettingActivity;
import com.sg.eyedoctor.consult.videoConsult.bean.ServiceRecord;
import com.sg.eyedoctor.contact.activity.ContactActivity;
import com.sg.eyedoctor.helpUtils.doctorAdvice.activity.HospitalPatientListActivity;
import com.sg.eyedoctor.helpUtils.doctorAsist.activity.DoctorAssistActivity;
import com.sg.eyedoctor.helpUtils.electronicRecords.activity.ElectricRecordActivity;
import com.sg.eyedoctor.helpUtils.freeClinic.activity.FreeClinicActivity;
import com.sg.eyedoctor.helpUtils.freeConsult.activity.FreeConsultActivity;
import com.sg.eyedoctor.helpUtils.massNotice.activity.MassNoticeActivity;
import com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.activity.StopDiagnoseNoticeActivity;
import com.sg.eyedoctor.main.activity.CertificationActivity;
import com.sg.eyedoctor.main.adapter.HomeBannerAdapter;
import com.sg.eyedoctor.main.adapter.HomeGridAdapter;
import com.sg.eyedoctor.main.bean.TeamRead;
import com.sg.eyedoctor.main.bean.UnreadCount;
import com.sg.eyedoctor.settings.myOnlineManager.activity.SetAddConsultActivity;
import com.sg.eyedoctor.settings.myOnlineManager.activity.SetPhoneConsultActivity;
import com.sg.eyedoctor.settings.myOnlineManager.activity.SetTextConsultActivity;
import com.sg.eyedoctor.settings.myOnlineManager.activity.SetVideoConsultActivity;
import com.sg.eyedoctor.settings.myStateMessage.activity.StateMessageActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    /**
     * 常用工具
     */
    private static final int COMMON_UTIL_ADD_PATIENT = 0;
    private static final int COMMON_UTIL_RECORD = 1;
    private static final int COMMON_UTIL_ADD_MANAGER = 2;
    private static final int COMMON_UTIL_PATIENT_REPORT = 3;
    private static final int COMMON_UTIL_CASE_DISCUSS = 4;
    private static final int COMMON_UTIL_INTERNET = 5;
    private static final int COMMON_UTIL_ACADMIC = 6;
    private static final int COMMON_UTIL_TOOLBOX = 7;
    /**
     * 辅助工具
     */
    private static final int HELP_UTIL_MASS_NOTICE = 0;
    private static final int HELP_UTIL_STOP_VISIT = 1;
    private static final int HELP_UTIL_ELECTRONIC_RECORD = 2;
    private static final int HELP_UTIL_DOCTOR_ADVICE = 3;
    private static final int HELP_UTIL_FREE_CLINIC = 4;
    private static final int HELP_UTIL_DOCTOR_ASIST = 5;
    private static final int HELP_UTIL_CONTACT = 6;
    private static final int HELP_UTIL_FREE_CONSULT = 7;

    @ViewInject(R.id.sv_home)
    private ScrollView mHomeSv;
    @ViewInject(R.id.img_doctor_head)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.tv_doctor_name)
    private TextView mNameTv;
    @ViewInject(R.id.auth_tv)
    private TextView mAuthTv;
    @ViewInject(R.id.tv_all_count)
    private TextView mAllCountTv;
    @ViewInject(R.id.tv_today_count)
    private TextView mTodayCountTv;
    @ViewInject(R.id.viewflow)
    private ViewFlow mViewFlow;
    @ViewInject(R.id.viewflowindic)
    private CircleFlowIndicator mFlowIndicator;
    @ViewInject(R.id.grid_common_utils)
    private GridView mCommonUtilsGrid;
    @ViewInject(R.id.grid_help_utils)
    private GridView mHelpUtilsGrid;
    @ViewInject(R.id.ll_text_consult)
    private RelativeLayout mTextConsultLl;
    @ViewInject(R.id.ll_phone_consult)
    private LinearLayout mPhoneConsultLl;
    @ViewInject(R.id.ll_video_consult)
    private LinearLayout mVideoConsultLl;
    @ViewInject(R.id.read_count_tv)
    private TextView mReadCountTv;
    @ViewInject(R.id.text_unread_tv)
    private TextView mTextCountTv;
    @ViewInject(R.id.v_img)
    private ImageView mVimg;
    @ViewInject(R.id.state_message_img)
    private ImageView mStateimg;

    private HomeGridAdapter mCommonUtilsAdapter;
    private HomeGridAdapter mHelpUtilsAdapter;

    //视频问诊的开启
    private boolean mIsOpen;
    private ServiceRecord mServiceRecord;
    private ConsultBean mConsultBean;
    private boolean mIsAuth = false;

    Observer<List<RecentContact>> messageObserver;
    ArrayList<TeamRead> teamReads=new ArrayList<>();
    List<RecentContact> mMessages = new ArrayList<>();

    private NetCallback mReadCountCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            //   closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<Integer>>() {
                }.getType();
                BaseResp<Integer> res = new Gson().fromJson(result, objectType);
                int count = res.value;
                if (count == 0) {
                    mReadCountTv.setVisibility(View.GONE);
                } else {
                    mReadCountTv.setText(count + "");
                    mReadCountTv.setVisibility(View.VISIBLE);
                }

                //获取图文咨询未读数
                BaseManager.getChatCount(mTextCountCallback);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mTextCountCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<UnreadCount>>() {
                }.getType();
                BaseArrayResp<UnreadCount> res = new Gson().fromJson(result, objectType);

                initUnread(res.value);
//                int count=res.value;
//                if(count==0){
//                    mTextCountTv.setVisibility(View.GONE);
//                }else{
//                    mTextCountTv.setText(count+"");
//                    mTextCountTv.setVisibility(View.VISIBLE);
//                }

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private void initUnread(ArrayList<UnreadCount> value) {
        for (UnreadCount readBean : value) {
            switch (readBean.source) {
                case "1"://图
                    if (readBean.count == 0) {
                        mTextCountTv.setVisibility(View.GONE);
                    } else {
                        mTextCountTv.setText(readBean.count + "");
                        mTextCountTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case "2"://免
                    ConstantValues.HOME_HELP_TOOLS_LIST.get(7).count = readBean.count;
                    break;

                case "4"://患者报道
                    ConstantValues.HOME_COMMON_TOOLS_LIST.get(3).count = readBean.count;
                    break;
                case "5"://医生通讯录
                    ConstantValues.HOME_HELP_TOOLS_LIST.get(5).count = readBean.count;
                    break;
            }
        }
        mCommonUtilsAdapter.setData(ConstantValues.HOME_COMMON_TOOLS_LIST);
        mHelpUtilsAdapter.setData(ConstantValues.HOME_HELP_TOOLS_LIST);
    }


    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                if (CommonUtils.isValueZero(result)) {
                    openVideoSetting();
                } else {
                    Type objectType = new TypeToken<BaseResp<ServiceRecord>>() {
                    }.getType();
                    BaseResp<ServiceRecord> res = new Gson().fromJson(result, objectType);
                    mServiceRecord = res.value;
                    Intent intent = new Intent(getActivity(), VideoConsultActivity.class);
                    intent.putExtra(ConstantValues.KEY_XTR_ID, mServiceRecord.xtrId);
                    startActivity(intent);
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
        initBanner();
        initGrid();
        initBaseView();
        initBaseListener();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        mMessages = recents;
                        getTeamCount();
                    }
                });
            }
        }, 250);
        messageObserver = new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(List<RecentContact> messages) {
                mMessages = messages;
                getTeamCount();
            }
        };
        registerService(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //已认证
        String state = mDoctor.Authenticated;
        mAuthTv.setText(state);
        if (mDoctor.state == 2) {
            mVimg.setSelected(true);
            mAuthTv.setSelected(true);
            mIsAuth = true;
        } else {
            mVimg.setSelected(false);
            mAuthTv.setSelected(false);
            mIsAuth = false;
        }
        mIsOpen = mDoctor.videoIsOpen.equals("True");

    }

    @Event(R.id.state_message_img)
    private void stateImg(View view) {
        startActivity(new Intent(getActivity(), StateMessageActivity.class));
    }

    @Event({R.id.ll_text_consult, R.id.ll_phone_consult, R.id.ll_video_consult})
    private void clickConsult(View view) {
        Intent intent = new Intent(getActivity(), OpenViedoActivity.class);
        switch (view.getId()) {
            case R.id.ll_text_consult:
                if (!mIsAuth) {
                    startAuthActivity(R.string.text_consult);
                    break;
                }
                boolean isOpenText = mDoctor.textIsOpen.equals("True");

                if (!isOpenText) {//未开通  跳转到开通提示界面
                    mConsultBean = new ConsultBean(R.drawable.home_text_consult, R.string.text_consult, SetTextConsultActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, mConsultBean);
                    startActivity(intent);
                    break;
                }

                startActivity(new Intent(getActivity(), TextConsultActivity.class));
                break;
            case R.id.ll_phone_consult:
                if (!mIsAuth) {
                    startAuthActivity(R.string.phone_consult);
                    break;
                }
                boolean isOpenPhone = mDoctor.phoneIsOpen.equals("True");
                if (!isOpenPhone) {//未开通  跳转到开通提示界面
                    mConsultBean = new ConsultBean(R.drawable.home_phone_consult, R.string.phone_consult, SetPhoneConsultActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, mConsultBean);
                    startActivity(intent);
                    break;
                }
                startActivity(new Intent(getActivity(), PhoneConsultActivity.class));
                break;
            case R.id.ll_video_consult:
                if (!mIsAuth) {
                    startAuthActivity(R.string.video_consult);
                    break;
                }
                boolean isOpenVideo = mDoctor.videoIsOpen.equals("True");
                if (!isOpenVideo) {//未开通  跳转到开通提示界面
                    mConsultBean = new ConsultBean(R.drawable.home_video_consult, R.string.video_consult, SetVideoConsultActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, mConsultBean);
                    startActivity(intent);
                    break;
                }

                showLoginDlg();
                BaseManager.getServiceRecord(mCallback);

                break;
        }

    }

    public void startAuthActivity(String title) {
        Intent intent = new Intent(getActivity(), CertificationActivity.class);
        intent.putExtra(ConstantValues.KEY_TITLE, title);
        startActivity(intent);
    }

    public void startAuthActivity(int titleId) {
        startAuthActivity(getActivity().getResources().getString(titleId));
    }

    public void initBaseView() {

        if (mDoctor.userName != null) {
            mNameTv.setText(mDoctor.userName);
        }
        if (!TextUtils.isEmpty(mDoctor.totalAppointment)) {
            mAllCountTv.setText(mDoctor.totalAppointment);
        }
        if (!TextUtils.isEmpty(mDoctor.appointment)) {
            mTodayCountTv.setText(mDoctor.appointment);
        }
        initHead();
        queryReadCount();
    }

    protected void initBaseListener() {
        mCommonUtilsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == COMMON_UTIL_ADD_PATIENT) {
                    startActivity(new Intent(getActivity(), AddPatientActivity.class));
                } else if (position == COMMON_UTIL_RECORD) {
                    if (!mIsAuth) {
                        startAuthActivity(R.string.out_patient_record);
                        return;
                    }
                    startActivity(new Intent(getActivity(), OutPatientRecordActivity.class));
                } else if (position == COMMON_UTIL_ADD_MANAGER) {
                    if (!mIsAuth) {
                        startAuthActivity(R.string.add_manager);
                        return;
                    }
                    Intent intent;
                    boolean isOpenVideo = mDoctor.addIsOpen.equals("True");
                    if (!isOpenVideo) {//未开通  跳转到开通提示界面
                        mConsultBean = new ConsultBean(R.drawable.ic_online_add, R.string.add_manager, SetAddConsultActivity.class);
                        intent = new Intent(getActivity(), OpenViedoActivity.class);
                        intent.putExtra(ConstantValues.KEY_DATA, mConsultBean);
                    } else {
                        intent = new Intent(getActivity(), PlusManagerActivity.class);
                    }
                    startActivity(intent);
                } else if (position == COMMON_UTIL_PATIENT_REPORT) {
                    if (!mIsAuth) {
                        startAuthActivity(R.string.patient_report);
                        return;
                    }
                    startActivity(new Intent(getActivity(), PatientReportActivity.class));
                } else if (position == COMMON_UTIL_CASE_DISCUSS) {
                    if (!mIsAuth) {
                        startAuthActivity(R.string.case_discuss);
                        return;
                    }

                    Intent intent=new Intent(getActivity(), CaseDiscussActivity.class);
                    intent.putParcelableArrayListExtra(ConstantValues.EXTRA_DATA,teamReads);
                    startActivity(intent);

                } else if (position == COMMON_UTIL_INTERNET) {
                    if (!mIsAuth) {
                        startAuthActivity(R.string.internet_consult);
                        return;
                    }
                    startActivity(new Intent(getActivity(), InternetConsultationActivity.class));

                } else if (position == COMMON_UTIL_TOOLBOX) {
                    startActivity(new Intent(getActivity(), ToolBoxActivity.class));
                } else if (position == COMMON_UTIL_ACADMIC) {
                    startActivity(new Intent(getActivity(), AcademicActivity.class));
                }

            }
        });
        mHelpUtilsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case HELP_UTIL_MASS_NOTICE:
                        if (!mIsAuth) {
                            startAuthActivity(R.string.mass_notice);
                            break;
                        }
                        startActivity(new Intent(getActivity(), MassNoticeActivity.class));
                        break;
                    case HELP_UTIL_STOP_VISIT:
                        if (!mIsAuth) {
                            startAuthActivity(R.string.stop_diagnose_notice);
                            break;
                        }
                        startActivity(new Intent(getActivity(), StopDiagnoseNoticeActivity.class));
                        break;
                    case HELP_UTIL_ELECTRONIC_RECORD:
                        if (!mIsAuth) {
                            startAuthActivity(R.string.electric_record);
                            break;
                        }
                        startActivity(new Intent(getActivity(), ElectricRecordActivity.class));
                        break;
                    case HELP_UTIL_DOCTOR_ADVICE://医嘱
                        if (!mIsAuth) {
                            startAuthActivity(R.string.doctor_ask);
                            break;
                        }
                        startActivity(new Intent(getActivity(), HospitalPatientListActivity.class));
                        break;
                    case HELP_UTIL_FREE_CLINIC://义诊发布
                        if (!mIsAuth) {
                            startAuthActivity(R.string.free_clinic_publish);
                            break;
                        }
                        startActivity(new Intent(getActivity(), FreeClinicActivity.class));
                        break;
                    case HELP_UTIL_DOCTOR_ASIST:
                        startActivity(new Intent(getActivity(), DoctorAssistActivity.class));
                        break;
                    case HELP_UTIL_CONTACT:
                        startActivity(new Intent(getActivity(), ContactActivity.class));
                        break;
                    case HELP_UTIL_FREE_CONSULT:
                        startActivity(new Intent(getActivity(), FreeConsultActivity.class));
                        break;
                }
            }
        });
    }

    private void initGrid() {
        mCommonUtilsAdapter = new HomeGridAdapter(getActivity(), ConstantValues.HOME_COMMON_TOOLS_LIST);
        mCommonUtilsGrid.setAdapter(mCommonUtilsAdapter);
        mHelpUtilsAdapter = new HomeGridAdapter(getActivity(), ConstantValues.HOME_HELP_TOOLS_LIST);
        mHelpUtilsGrid.setAdapter(mHelpUtilsAdapter);
    }

    private void initBanner() {
        ArrayList<Integer> mImgArray = new ArrayList<>();
        mImgArray.add(R.drawable.ad1);
        mImgArray.add(R.drawable.ad2);
        mImgArray.add(R.drawable.ad3);
        mImgArray.add(R.drawable.ad4);
        mViewFlow.setAdapter(new HomeBannerAdapter(getActivity(), mImgArray).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(mImgArray.size()); // 实际图片张数，
        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(mImgArray.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }

    private void initHead() {
        CommonUtils.loadImg(mDoctor.picFileName, mHeadImg);
    }

    private void openVideoSetting() {
        DialogManager.showConfimCancelDlg(getActivity(), R.string.open_video_now, new DlgClick() {
            @Override
            public boolean click() {
                Intent intent = new Intent(getActivity(), VideoSettingActivity.class);
                startActivity(intent);
                return false;
            }
        }, new DlgClick() {
            @Override
            public boolean click() {
                return false;
            }
        });
    }

    private void queryReadCount() {
        showLoginDlg();
        BaseManager.getMessageCount(mReadCountCallback);
    }

    private void getTeamCount() {
        int unreadNum = 0;
        for (RecentContact r : mMessages) {
            if (r.getSessionType() == SessionTypeEnum.Team) {
                unreadNum += r.getUnreadCount();
                teamReads.add(new TeamRead(r.getContactId(),r.getUnreadCount()));
            }
        }

        LogUtils.i("readcount:"+unreadNum);
        ConstantValues.HOME_COMMON_TOOLS_LIST.get(4).count = unreadNum;
        mCommonUtilsAdapter.setData(ConstantValues.HOME_COMMON_TOOLS_LIST);
    }

    private void registerService(boolean b) {
        //注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, b);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerService(false);
    }
}
