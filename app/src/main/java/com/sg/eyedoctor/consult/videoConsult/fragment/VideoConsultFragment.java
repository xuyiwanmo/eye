package com.sg.eyedoctor.consult.videoConsult.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.avchat.activity.AVChatActivity;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.NetworkUtil;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.consult.advice.activity.ConsultDetailActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticEditActivity;
import com.sg.eyedoctor.consult.advice.activity.DiagnosticShowActivity;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;
import com.sg.eyedoctor.consult.videoConsult.adapter.VideoConsultAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


/**
 * 视频咨询:待视频  已完成
 */
@ContentView(R.layout.fragment_consult_new)
public class VideoConsultFragment extends BaseFragment implements VideoConsultAdapter.DailCallback {
    public static final String TYPE = "TYPE";
    @ViewInject(R.id.lv_couslt)
    private ListView mConsultNewList;

    private ArrayList<Patient> mPatients;

    private VideoConsultAdapter mConsultAdapter;
    private Patient mPatient;

    private boolean mIsLoad = true;
    private boolean onResume = false;

    /**
     * (1:未回复|5:已回复|4：已完成)
     */
    private int mType = 1;
    private String mXtrId;

    private NetCallback mDataCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);

                mPatients = res.value;
                Collections.sort(mPatients, new VideoConsultSort());
                if (mPatients != null) {
                    mConsultAdapter.setData(mPatients);
                } else {
                    showToast(R.string.query_empty);
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


    public VideoConsultFragment(int fragmentType, String xtrId) {
        mXtrId = xtrId;
        mType = fragmentType;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPatients = new ArrayList<>();
        mConsultAdapter = new VideoConsultAdapter(getActivity(), mPatients, 0, this);
        mConsultNewList.setAdapter(mConsultAdapter);
        UiUtils.setEmptyText(getActivity(), mConsultNewList, getString(R.string.empty));

        initListeners();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
    }

    private void initListeners() {


        mConsultNewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mPatient = mPatients.get(position);

                Intent intent2 = new Intent(getActivity(), ConsultDetailActivity.class);
                intent2.putExtra(ConstantValues.KEY_PATIENT, mPatient);
                intent2.putExtra(ConstantValues.KEY_TYPE, 1);
                startActivity(intent2);

            }
        });
    }

    public void queryData() {

        showLoginDlg();
        BaseManager.queryConsult(mType + "", "3", "", mDataCallback);
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

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            queryData();
        }
    }

    @Override
    public void dail(int position) {
        Patient patient = mPatients.get(position);

        if (NetworkUtil.isNetAvailable(getActivity())) {
            if (!CommonUtils.isLogin()) {
                showToast(R.string.operation_not_open);
                return;
            }
            AVChatActivity.start(getActivity(), patient.patientIM, AVChatType.VIDEO.getValue(), AVChatActivity.FROM_INTERNAL, patient.id);

        } else {
            Toast.makeText(getActivity(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void editDiagnotice(int position) {
        Patient patient = mPatients.get(position);
        if (patient.state == 6) {
            return;
        }
        if (patient.writeState.equals("0")) {//未填写
            Intent intent = new Intent(getActivity(), DiagnosticEditActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);

        } else if (patient.writeState.equals("1")) {
            Intent intent = new Intent(getActivity(), DiagnosticShowActivity.class);
            intent.putExtra(ConstantValues.KEY_PATIENT, patient);
            intent.putExtra(ConstantValues.KEY_END, true);
            intent.putExtra(ConstantValues.KEY_IS_TEXT_CONSULT, false);
            startActivity(intent);
        }

    }

    class VideoConsultSort implements Comparator {

        public String getTime(Object p) {
            Patient patient=(Patient)p;
            if (patient.state == 1) {
                return patient.createDate;
            } else {
                return patient.modifyDate;
            }
        }

        @Override
        public int compare(Object time1, Object time2) {
            boolean flag = false;
            //2016/9/12 16:09:45
            String t1=getTime(time1);
            String t2=getTime(time2);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            if (t1== null || t2 == null) {
                return 1;
            }
            try {
                Date d1 = format.parse(t1);
                Date d2 = format.parse(t2);
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
