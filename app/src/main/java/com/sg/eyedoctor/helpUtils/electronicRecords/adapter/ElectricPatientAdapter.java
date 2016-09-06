package com.sg.eyedoctor.helpUtils.electronicRecords.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.helpUtils.electronicRecords.bean.ElectricHospital;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class ElectricPatientAdapter extends CommAdapter<ElectricHospital> {

    public ElectricPatientAdapter(Context context, ArrayList<ElectricHospital> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(ElectricHospital doctor, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_electric_patient, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mBedTv,doctor.bedNo);
        setTextView(holder.mIllnessNoTv,doctor.eventNo);
        setTextView(holder.mAgeTv, doctor.age+"岁");
        setTextView(holder.mOfficeTv, doctor.deptName);
        setTextView(holder.mNameTv, doctor.patientName);
        setTextView(holder.mPatientNoTv, doctor.patientId);


        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.patient_no_tv)
        TextView mPatientNoTv;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.illness_no_tv)
        TextView mIllnessNoTv;
        @ViewInject(R.id.bed_tv)
        TextView mBedTv;
        @ViewInject(R.id.age_tv)
        TextView mAgeTv;
        @ViewInject(R.id.office_tv)
        TextView mOfficeTv;
    }
}
