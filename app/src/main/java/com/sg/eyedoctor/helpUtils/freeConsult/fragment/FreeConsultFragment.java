package com.sg.eyedoctor.helpUtils.freeConsult.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.helpUtils.freeConsult.adapter.FreeConsultAdapter;
import com.sg.eyedoctor.helpUtils.freeConsult.bean.FreePatient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * 图文咨询:新患者
 */
@ContentView(R.layout.fragment_consult_free)
public class FreeConsultFragment extends BaseFragment {
    public static final String TYPE = "TYPE";
    @ViewInject(R.id.lv_couslt)
    private ListView mConsultNewList;

    private ArrayList<FreePatient> mPatients;

    private FreeConsultAdapter mConsultAdapter;
    private FreePatient mPatient;

    /**
     * 1:新咨询|2:已回复|3:已完成
     */
    private int mType = 1;

    private NetCallback mDataCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseArrayResp<FreePatient>>() {
                }.getType();
                BaseArrayResp<FreePatient> res = new Gson().fromJson(result, objectType);
                mPatients = res.value;

                Collections.sort(mPatients, new FreeConsultSort());
                for (FreePatient patient : mPatients) {
                    LogUtils.i("日期:" + patient.modifyDate);
                }
                if (mPatients != null) {
                    mConsultAdapter.setData(mPatients);
                }

            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    //update订单状态
    private NetCallback mUpdateCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                if (mType == 1) {
                    showToast("修改订单状态成功!");
                    if (!CommonUtils.isLogin()) {
                        showToast(R.string.operation_not_open);
                        return;
                    }
                    P2PMessageActivity.start(getActivity(), mPatient.patientIM, SessionHelper.getFreeP2pCustomization(mPatient, mType), mPatient);
                }
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mReadCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @SuppressLint("ValidFragment")
    public FreeConsultFragment(int fragmentType) {
        mType = fragmentType;
    }

    public FreeConsultFragment() {
    }

    public static final FreeConsultFragment newInstance(int fragmentType) {
        FreeConsultFragment f = new FreeConsultFragment(fragmentType);
        Bundle bundle = new Bundle(2);
        bundle.putInt(TYPE, fragmentType);

        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(TYPE);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPatients = new ArrayList<>();
        mConsultAdapter = new FreeConsultAdapter(getActivity(), mPatients, 0,mType);
        mConsultNewList.setAdapter(mConsultAdapter);
        UiUtils.setEmptyText(getActivity(), mConsultNewList, getString(R.string.empty));
    }

    @Override
    protected void initView() {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, mDoctor.userName);
        fields.put(UserInfoFieldEnum.AVATAR, CommonUtils.formatUrl(mDoctor.picFileName));
        NIMClient.getService(UserService.class).updateUserInfo(fields);
    }

    @Override
    protected void initListener() {
        mConsultNewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //**************点击对应的联系人   跳转到  聊天界面
                if (!CommonUtils.isLogin()) {
                    showToast(R.string.operation_not_open);
                    return;
                }
                mPatient = mPatients.get(position);
                if (mPatient.newMessage != 0) {
                    BaseManager.setQuestionMessageIsRead(mPatient.questionId, mPatient.patientIM, "d" + mPatient.doctorId, mReadCallback);
                }
                if (mType == 1) {
                    //修改订单状态
                    showLoginDlg();
                    BaseManager.questionStateUpdate("2", mPatient.questionId, mUpdateCallback);

                } else {
                    P2PMessageActivity.start(getActivity(), mPatient.patientIM, SessionHelper.getFreeP2pCustomization(mPatient, mType), mPatient);
                }
            }
        });
    }


    public void queryData() {
        showLoginDlg();
        BaseManager.getQuestionList(mType + "", mDataCallback);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断该Fragment是否已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible()) {
            queryData(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint()) {
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }

    public boolean canFlushQuestion() {
        if (mType == 1 && mPatients.size() != 0) {//新咨询 有问题 不能刷题
            return false;
        }
        for (FreePatient patient : mPatients) {
            if (patient.newMessage != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            queryData();
        }
    }

    class FreeConsultSort implements Comparator {

        @Override
        public int compare(Object time1, Object time2) {
//            long flag =((FreePatient) time1).modifyDate.compareTo(((FreePatient) time2).modifyDate);
//            if (flag > 0) {
//                return -1;
//            } else {
//                return 0;
//            }
            boolean flag = false;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                Date d1 = format.parse(((FreePatient) time1).modifyDate);
                Date d2 = format.parse(((FreePatient) time2).modifyDate);
                if (d1.getTime() < d2.getTime()) {
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (flag) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
