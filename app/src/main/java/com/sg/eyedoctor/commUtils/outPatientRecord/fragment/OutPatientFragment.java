package com.sg.eyedoctor.commUtils.outPatientRecord.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.addPatient.activity.AddPatientActivity;
import com.sg.eyedoctor.commUtils.outPatientRecord.adapter.OutPatientAdapter;
import com.sg.eyedoctor.commUtils.outPatientRecord.bean.AppointPatient;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.UiUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * A simple  subclass.
 */
@ContentView(R.layout.fragment_out_patient)
public class OutPatientFragment extends BaseFragment {

    @ViewInject(R.id.lv_my_patient)
    private ListView mListView;

    private OutPatientAdapter mPatientAdapter;
    private ArrayList<AppointPatient> mPatients;

    private boolean mIsvisable = true;

    public OutPatientFragment(boolean b) {
        mIsvisable = b;
    }

    @Override
    protected void initView() {
        mPatients = new ArrayList<>();
        mPatientAdapter = new OutPatientAdapter(getActivity(), mPatients, mIsvisable);
        mListView.setAdapter(mPatientAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mIsvisable) {
                    //跳转到
                    Intent intent = new Intent(getActivity(), AddPatientActivity.class);
                    intent.putExtra(ConstantValues.KEY_PATIENT, mPatients.get(position));
                    startActivity(intent);
                }
            }
        });
        UiUtils.setEmptyText(getActivity(), mListView, getString(R.string.empty));

    }

    @Override
    protected void initListener() {

    }

    public void setData(ArrayList<AppointPatient> patients) {

        this.mPatients = patients;
        mPatientAdapter.setData(mPatients);
    }
}
