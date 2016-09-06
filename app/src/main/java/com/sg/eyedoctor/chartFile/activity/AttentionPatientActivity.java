package com.sg.eyedoctor.chartFile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.adapter.AttentionPatientAdapter;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 病历夹-重点关注的病历
 */
@ContentView(R.layout.activity_attention_patient)
public class AttentionPatientActivity extends BaseActivity {

    @ViewInject(R.id.attention_lv)
    private ListView mAttentionLv;
    private AttentionPatientAdapter mAdapter;

    private ArrayList<PatientByGroup> mFocusPatients;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PatientByGroup>>(){}.getType();
                BaseArrayResp<PatientByGroup> res=new Gson().fromJson(result, objectType);
                mFocusPatients=res.value;
                mAdapter.setData(mFocusPatients);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    @Override
    protected void initView() {
        mFocusPatients=new ArrayList<>();
        mAdapter=new AttentionPatientAdapter(mContext,mFocusPatients,0);
        mAttentionLv.setAdapter(mAdapter);
        UiUtils.setEmptyText(mContext, mAttentionLv, getString(R.string.empty));

    }

    @Override
    protected void initListener() {
        mAttentionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientByGroup patient = mFocusPatients.get(position);

                Intent intent = new Intent(mContext, PatientCenterActivity.class);
                intent.putExtra(ConstantValues.KEY_PATIENT, patient);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseManager.getFocusPatient(mCallback);
    }
}
