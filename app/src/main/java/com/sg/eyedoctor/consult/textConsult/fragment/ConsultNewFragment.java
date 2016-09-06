package com.sg.eyedoctor.consult.textConsult.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.ConsultSort;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.consult.textConsult.adapter.ConsultPatientAdapter;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;


/**
 * 图文咨询:新患者
 */
@ContentView(R.layout.fragment_consult_new)
public class ConsultNewFragment extends BaseFragment {
    public static final String TYPE = "TYPE";
    @ViewInject(R.id.lv_couslt)
    private ListView mConsultNewList;

    private ArrayList<Patient> mPatients;

    private ConsultPatientAdapter mConsultAdapter;
    private Patient mPatient;


    /**
     * (1:未回复|5:已回复|4：已完成)
     */
    private int mType = 1;

    private NetCallback mDataCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Patient>>() {
                }.getType();
                BaseArrayResp<Patient> res = new Gson().fromJson(result, objectType);

                mPatients = res.value;
                Collections.sort(mPatients, new ConsultSort());
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

    public ConsultNewFragment(int fragmentType) {
        mType = fragmentType;
    }

    public ConsultNewFragment() {
    }

    public static final ConsultNewFragment newInstance(int fragmentType) {
        ConsultNewFragment f = new ConsultNewFragment(fragmentType);
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

        //  initData();
        mConsultAdapter = new ConsultPatientAdapter(getActivity(), mPatients, 0);
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
                //**************点击对应的联系人   跳转到  聊天界面

                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                   return;
                }

                mPatient = mPatients.get(position);
                if (!mPatient.newMessage.equals("0")) {
                    BaseManager.setQuestionMessageIsRead(mPatient.id, mPatient.patientIM, mPatient.doctorIM, mReadCallback);
                }

                P2PMessageActivity.start(getActivity(), mPatient.patientIM, SessionHelper.getTextP2pCustomization(mPatient, mType));

            }
        });
    }

    public void queryData() {
        showLoginDlg();
        BaseManager.queryConsult(mType + "", "1", "", mDataCallback);
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




}
